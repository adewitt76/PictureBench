package PictureBenchSwing;

import Utilities.ImageUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *  This is the painted image you see in when you open the GUI.
 *  @author Aaron DeWitt
 *  @version 0.1 9-May-2015
 */
public class ImagePanel extends JPanel {

    private BufferedImage originalImage; // an un-altered copy of the original
    private Image image; // A copy of the original that can be manipulated by zooming
    private double imageWidth;
    private double imageHeight;
    private double zoom;

    public ImagePanel(){
        zoom = 1.0;
    }

    /**
     * This function fills Graphics g with the stored Image image.
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        Rectangle r = graphics.getClipBounds();
        double rWidth = r.getWidth();
        double rHeight = r.getHeight();
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(0,0,(int)rWidth,(int)rHeight);

        if( image != null) {
            int x = 0, y = 0;
            if(imageWidth > rWidth || imageHeight > rHeight)
                graphics.fillRect(0,0, imageWidth > rWidth ? (int)imageWidth : (int)rWidth, imageHeight > rHeight ? (int)imageHeight:(int)rHeight );
            if(rWidth > imageWidth) x = (int)((rWidth - imageWidth)/2.0);
            if(rHeight > imageHeight) y = (int)((rHeight - imageHeight)/2.0);
            graphics.drawImage(image, x, y, null);
        }
    }

    /**
     * Sets the image that is contained in this object. If the user
     * requested a zoom in or zoom out the zoom is processed here.
     * @param originalImage Image to be displayed
     */
    public void setImage(BufferedImage originalImage) {
        this.originalImage = originalImage;
        imageWidth = originalImage.getWidth();
        imageHeight = originalImage.getHeight();
        image = ImageUtilities.copy(originalImage);
        if(zoom > 1 || 1 > zoom) zoom();
        setPreferredSize(new Dimension((int)imageWidth, (int)imageHeight));
    }

    /**
     * Increase the size of image by 10% without changing the original image itself.
     * constraints: image must be smaller than 5000 pixels
     */
    public void zoomIn() {
        if(imageWidth < 5000 && imageHeight < 5000)
            zoom *= 1.1;
            zoom();
    }

    /**
     * Decreases the size of image by 10% without changing the original image itself.
     * constraints: image must be bigger than 10 pixels
     */
    public  void zoomOut() {
        if(imageWidth > 10 && imageHeight > 10){
            zoom *= .9;
            zoom();
        }
    }

    /**
     * Resets the image to its original size.
     */
    public void resetZoom(){
        imageWidth = originalImage.getWidth();
        imageHeight = originalImage.getHeight();
        image = ImageUtilities.copy(originalImage);
        setPreferredSize(new Dimension((int)imageWidth, (int)imageHeight));
    }

    /**
     * Does zoom calculations and sets image(copy) to new size. This
     * function works off of the original image so to minimize distortion.
     */
    private void zoom(){
        imageWidth = originalImage.getWidth() * zoom;
        imageHeight = originalImage.getHeight() * zoom;
        image = originalImage.getScaledInstance((int) imageWidth, (int) imageHeight, Image.SCALE_REPLICATE);
        setPreferredSize(new Dimension((int)imageWidth, (int)imageHeight));
    }
}
