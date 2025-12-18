public class MosaicSolution {
    private boolean[][] solution;
    private int row;
    private int col;

    // constructor
    public MosaicSolution(int row, int col) {
        this.solution = new boolean[row][col];
        this.row = row;
        this.col = col;
    }

    public void setBlack(int r, int c) {
        this.solution[r][c] = true;
    }

    public boolean isBlack(int r, int c) {
        return this.solution[r][c];
    }

    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }
}
