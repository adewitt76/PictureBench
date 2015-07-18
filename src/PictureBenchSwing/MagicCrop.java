package PictureBenchSwing;

import Utilities.Edge;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.PriorityQueue;

/**
 * The MagicCrop object is an BufferedImage object that is used
 * to seam carve an image. Seam carving an image allows for the
 * reduction of the size of an image with out much distortion to
 * objects in the image. The methods that this class implements
 * where dictated by my professor.
 * @author Aaron DeWitt
 * @version 0.1 10-May-2015
 */
public class MagicCrop {

    private BufferedImage image; // the stored image to be carved
    private int[][] energyMap;
    /* The variable energyMap is a calculation that is essentially used as the weight
     * in the graph. The energy map is essential to finding the best seam. */
    private int width; // the current width of the image
    private int height; // the current height of the image

    /**
     * Creates a new MagicCrop object out of a supplied image.
     * @param image the image that needs to be reduced
     */
    public MagicCrop(BufferedImage image){
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        energyMap = new int[width][height];

        buildEnergyMap();
    }

    /**
     * Returns the energy of the pixel at location (x,y)
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @return the energy of the pixel at location (x,y)
     */
    public int energy(int x, int y){
        return energyMap[x][y];
    }


    /**
     * This method finds a horizontal seam that can be removed from
     * the image. The method works by using a modified version of Dijkstra's
     * algorithm. It essentially runs through every pixel in the left column of the
     * image and finds the shortest path to every pixel in the right column. The
     * method then searches through the right column for the pixel with the shortest
     * path and returns its path.
     *** This function was laid out as a requirement by the professor. ***
     * @return A horizontal seem that can be removed.
     */
    public int[] findHorizontalSeam() {

        int[] path = new int[width]; // the return path

        boolean[][] visited = new boolean[width][height]; // whether or not the pixel has been visited
        int[][] paths = new int[width][height]; // stores the shortest path to each pixel
        long[][] dist = new long[width][height]; // the min-distance(energy wise) to each pixel

        PriorityQueue<Edge> nextPixel = new PriorityQueue<>(); // keeps track of the next pixel to evaluate

        /*  Initializes the distance-to map and visited map. All distances
         *  need to be set to the max value and visited to false. */
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                dist[i][j] = Long.MAX_VALUE;
                visited[i][j] = false;
            }
        }

        /* Starting from the top row the for loop is going to work its
         * way through each row finding the shortest path to every pixel
         * within its reach.*/
        for (int row = 0; row < height; row++) {
            int col =0;
            dist[col][row] = energyMap[col][row]; // initialize first column for the row
            paths[col][row] = row; // initialize the path to itself for first column
            nextPixel.add(new Edge(row, col, energyMap[col][row])); // initialize the priority queue

            while (!nextPixel.isEmpty()) {
                Edge edge = nextPixel.poll();
                if(!visited[edge.getCol()][edge.getRow()]){
                    visited[edge.getCol()][edge.getRow()] = true;
                    long cWeight = dist[edge.getCol()][edge.getRow()]; // current weight at this pixel
                    /* Search three the adjacent pixels in the next column and
                     * set the paths if they are the shortest. */
                    int nCol = edge.getCol() + 1; // sets the next column
                    if(nCol >= width) break; //reached the end of the image - break
                    int top = edge.getRow() - 1;
                    int center = edge.getRow();
                    int bottom = edge.getRow() + 1;
                    if(top < 0) top = 0; // at top can't go above top
                    if(bottom >= height) bottom = height -1; // at bottom cant go below
                   /* Check each adjacent pixel and if not visited(if visited it is already the shortest path)
                    * and is the shortest path set the path to pixel */
                    if(!visited[nCol][top] && dist[nCol][top] >= energyMap[nCol][top] + cWeight){
                        paths[nCol][top] = edge.getRow(); dist[nCol][top] = energyMap[nCol][top] + cWeight;
                        nextPixel.add(new Edge(top,nCol,dist[nCol][top]));
                    }
                    if(!visited[nCol][center] && dist[nCol][center] >= energyMap[nCol][center] + cWeight){
                        paths[nCol][center] = edge.getRow(); dist[nCol][center] = energyMap[nCol][center] + cWeight;
                        nextPixel.add(new Edge(center,nCol,dist[nCol][center]));
                    }
                    if(!visited[nCol][bottom] && dist[nCol][bottom] >= energyMap[nCol][bottom] + cWeight){
                        paths[nCol][bottom] = edge.getRow(); dist[nCol][bottom] = energyMap[nCol][bottom] + cWeight;
                        nextPixel.add(new Edge(bottom,nCol,dist[nCol][bottom]));
                    }

                }
            }

            /* Cycle through all pixels and reset to false for next row
            * *** There maybe a more efficient way of doing this. *** */
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) visited[i][j] = false;
            }
        }
        /* Starting with the first row las column cycle through each pixel and
         * find the shortest distance */
        int lowestRow = 0;
        long lowestColValue = dist[width-1][0];
        for (int i = 1; i < height; i++) {
            if(dist[width-1][i] < lowestColValue) {
                lowestColValue = dist[width-1][i];
                lowestRow = i;
            }
        }
        /* When the shortest distance is found find the path to that pixel. The
         * path is found by working backwards through the paths matrix. */
        for (int i = width - 1; i >= 0; i--) {
            path[i] = lowestRow;
            lowestRow = paths[i][lowestRow];
        }
        return path;
    }

    /**
     * This method finds a vertical seam that can be removed from
     * the image. The method works by using a modified version of Dijkstra's
     * algorithm. It essentially runs through every pixel in the top row of the
     * image and finds the shortest path to every pixel in the bottom row. The
     * method then searches through the bottom row for the pixel with the shortest
     * path and returns that path.
     *** This function was laid out as a requirement by the professor. ***
     * @return A horizontal seem that can be removed.
     */
    public int[] findVerticalSeam() {

        int[] path = new int[height]; // the return path

        boolean[][] visited = new boolean[width][height]; // whether or not the pixel has been visited
        int[][] paths = new int[width][height]; // stores the shortest path to each pixel
        long[][] dist = new long[width][height]; // the min-distance(energy wise) to each pixel

        PriorityQueue<Edge> nextPixel = new PriorityQueue<>(); // keeps track of the next pixel to evaluate

        /*  Initializes the distance-to map and visited map. All distances
         *  need to be set to the max value and visited to false. */
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                dist[i][j] = Long.MAX_VALUE;
                visited[i][j] = false;
            }
        }

        /* Starting from the left column the for loop is going to work its
         * way through each column finding the shortest path to every pixel
         * within its reach.*/
        for (int col = 0; col < width; col++) {
            int row =0;
            dist[col][row] = energyMap[col][row]; // initialize first row for the column
            paths[col][row] = col; // initialize the path to itself for first row
            nextPixel.add(new Edge(row, col, energyMap[col][row])); // initialize the priority queue

            while (!nextPixel.isEmpty()) {
                Edge edge = nextPixel.poll();
                if(!visited[edge.getCol()][edge.getRow()]){
                    visited[edge.getCol()][edge.getRow()] = true;
                    long cWeight = dist[edge.getCol()][edge.getRow()]; // current weight at this pixel
                    /* Search three the adjacent pixels in the next row and
                     * set the paths if they are the shortest. */
                    int nRow = edge.getRow() + 1; // sets the next row
                    if(nRow >= height) break; // reached the end of the image - break
                    int left = edge.getCol() - 1;
                    int center = edge.getCol();
                    int right = edge.getCol() + 1;
                    if(left < 0) left = 0;  // at the farthest point left
                    if(right >= width) right = width -1; // at farthest point right cant go below
                    /* Check each adjacent pixel and if not visited(if visited it is already the shortest path)
                     * and is the shortest path set the path to pixel */
                    if(!visited[left][nRow] && dist[left][nRow] >= energyMap[left][nRow] + cWeight){
                        paths[left][nRow] = edge.getCol(); dist[left][nRow] = energyMap[left][nRow] + cWeight;
                        nextPixel.add(new Edge(nRow,left,dist[left][nRow]));
                    }
                    if(!visited[center][nRow] && dist[center][nRow] >= energyMap[center][nRow] + cWeight){
                        paths[center][nRow] = edge.getCol(); dist[center][nRow] = energyMap[center][nRow] + cWeight;
                        nextPixel.add(new Edge(nRow,center,dist[center][nRow]));
                    }
                    if(!visited[right][nRow] && dist[right][nRow] >= energyMap[right][nRow] + cWeight){
                        paths[right][nRow] = edge.getCol(); dist[right][nRow] = energyMap[right][nRow] + cWeight;
                        nextPixel.add(new Edge(nRow,right,dist[right][nRow]));
                    }

                }
            }

            /* Cycle through all pixels and reset to false for next row
            * *** There maybe a more efficient way of doing this. *** */
            for (int i = 0; i < width; i++)
                for (int j = 0; j < height; j++) visited[i][j] = false;
        }
        /* Starting with the first row las column cycle through each pixel and
         * find the shortest distance */
        int lowestCol = 0; long lowestRowValue = dist[0][height - 1];
        for (int i = 1; i < width; i++) {
            if(dist[i][height-1] < lowestRowValue) {
                lowestRowValue = dist[i][height-1];
                lowestCol = i;
            }
        }
        /* When the shortest distance is found find the path to that pixel. The
         * path is found by working backwards through the paths matrix. */
        for (int i = height - 1; i >= 0; i--) {
            path[i] = lowestCol;
            lowestCol = paths[lowestCol][i];
        }
        return path;
    }

    /**
     * This function removes the given horizontal seam. The horizontal
     * seam to remove should be furnished by the findHorizontalSeamFunction.
     * @param seam remove a seam from the MagicCrop image.
     */
    public void removeHorizontalSeam(int[] seam){
        /* Build a new image and new energy map that is on pixel shorter then the original */
        BufferedImage tempImage = new BufferedImage(width, height - 1, BufferedImage.TYPE_INT_RGB);
       // int[][] tempEnergyMap = new int[width][height-1];

        /* Copy all pixels that are not in the removal seam in both the image and the energy map */
        for (int c = 0; c < tempImage.getWidth(); c++) {
            for (int r = 0; r < tempImage.getHeight(); r++) {
                if(r < seam[c]) tempImage.setRGB(c,r,image.getRGB(c,r));
                else if (r >= seam[c]) tempImage.setRGB(c, r, image.getRGB(c, r + 1));
            }
        }
        /* Swap all permanent fields with temps */
        image = tempImage;
        width = image.getWidth();
        height = image.getHeight();
        /* Recalculate the energy map */
        buildEnergyMap();
    }

    /**
     * This function removes the given vertical seam. The vertical
     * seam to remove should be furnished by the findVerticalSeamFunction.
     * @param seam remove a seam from the MagicCrop image.
     */
    public void removeVerticalSeam(int[] seam){
        /* Build a new image and new energy map that is on pixel shorter then the original */
        BufferedImage tempImage = new BufferedImage(width - 1, height, BufferedImage.TYPE_INT_RGB);
        //int[][] tempEnergyMap = new int[width-1][height];

        /* Copy all pixels that are not in the removal seam in both the image and the energy map */
        for (int r = 0; r < tempImage.getHeight(); r++) {
            for (int c = 0; c < tempImage.getWidth(); c++) {
                if(c < seam[r]) tempImage.setRGB(c,r,image.getRGB(c,r));
                else if ( c >= seam[r]) tempImage.setRGB(c, r, image.getRGB(c + 1, r));
            }
        }
        /* Swap all permanent fields with temps */
        image = tempImage;
        width = image.getWidth();
        height = image.getHeight();
        /* Recalculate the energy map */
        buildEnergyMap();
    }

    /**
     * This method calculates the energy in each pixel. The energy
     * at each pixel (x,y) is dx^2(x,y)+dy^2(x,y). Where
     * dx^2(x,y)=dRx(x,y)^2+dGx(x,y)^2+dBx(x,y)^2. So, for each color
     * the difference is calculated by the pixel to the immediate right
     * left, that is  dRx(x,y)^2=(R(x-1,y)-R(x+1,y))^2. This calculation
     * is repeated for all colors. The y is then calculated in the same
     * way except vertically.
     */
    private void buildEnergyMap(){
        boolean cBorder; // needed because energy at the border is a result from a wrap
        boolean rBorder;

        /* Traverse each pixel setting its energy level. */
        for (int c = 0; c < width; c++) {
            if(c == 0 || c == width - 1) cBorder = true;
            else cBorder = false;
            for (int r = 0; r < height; r++) {
                if(r == 0 || r == height - 1) rBorder = true;
                else rBorder = false;

                /* *** Calculate dx^2 *** */
                int deltaX; // end result - change in X
                Color cRight, cLeft; // colors right and left
                if(cBorder){ // if at the borders use the color from opposite end of the image
                    if(c == 0) {
                        cRight = new Color(image.getRGB(1, r));
                        cLeft = new Color(image.getRGB(width - 1, r));
                    } else {
                        cRight = new Color(image.getRGB(0, r));
                        cLeft = new Color(image.getRGB(width - 2, r));
                    }
                }
                else { // else use the colors to immediate right and left
                    cRight = new Color(image.getRGB(c + 1, r));
                    cLeft = new Color(image.getRGB(c - 1, r));
                }
                int dR = cRight.getRed() - cLeft.getRed(); // calculate change in red
                int dG = cRight.getGreen() - cLeft.getGreen(); // calculate change in green
                int dB = cRight.getBlue() - cLeft.getBlue(); // calculate change in blue
                deltaX = (int)(Math.pow(dR,2) + Math.pow(dG,2) + Math.pow(dB,2)); // calculate dx

                /* *** Calculate dy^2 *** */
                int deltaY; // end result - change in Y
                Color rBottom, rTop; // colors above and bellow
                if(rBorder){ // if at the border ust the color from the opposite end of the image
                    if(r == 0) {
                        rBottom = new Color(image.getRGB(c, 1));
                        rTop = new Color(image.getRGB(c, height - 1));
                    } else {
                        rBottom = new Color(image.getRGB(c, 0));
                        rTop = new Color(image.getRGB(c, height - 2));
                    }
                }
                else { // else use the colors immediately above and below the current pixel
                    rBottom = new Color(image.getRGB(c, r + 1));
                    rTop = new Color(image.getRGB(c, r - 1));
                }
                dR = rBottom.getRed() - rTop.getRed(); // calculate change in red
                dG = rBottom.getGreen() - rTop.getGreen(); // calculate change in green
                dB = rBottom.getBlue() - rTop.getBlue(); // calculate change in blue
                deltaY = (int)(Math.pow(dR,2) + Math.pow(dG,2) + Math.pow(dB,2)); // calculate dy

                /* *** Calculate final energy and place on map *** */
                energyMap[c][r] = deltaX + deltaY;
            }
        }
    }

    // Getters and setters follow
    public BufferedImage getImage() {
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}























