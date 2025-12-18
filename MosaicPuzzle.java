public class MosaicPuzzle{
    private int[][] papan; //-1 untuk sel tanpa angka
    private int row;
    private int col;

    //constructor
    public MosaicPuzzle(int[][] papan){
        this.papan = papan;
        this.row = papan.length;
        this.col = papan[0].length;
    }

    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }

    public int getNumber(int r, int c){
        return this.papan[r][c];
    }
    
}