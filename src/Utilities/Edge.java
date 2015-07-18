package Utilities;

/**
 * Created by aaron on 5/15/15.
 */
public class Edge implements Comparable<Edge>{

    private int row;
    private int col;
    private long weight;

    public Edge(int row, int col, long weight) {
        this.row = row; this.col = col; this.weight = weight;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public int compareTo(Edge e) {
        return (int)(weight - e.weight);
    }
}
