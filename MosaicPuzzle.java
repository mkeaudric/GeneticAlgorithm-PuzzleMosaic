public class MosaicPuzzle{
    private int[][] papan; //-1 untuk sel tanpa angka
    private int size; //ukuran mosaic (row atau col)

    //constructor
    public MosaicPuzzle(int[][] papan){
        this.papan = papan;
        this.size = papan.length;
        this.size = papan[0].length;
    }

    public int getSize(){
        return this.size;
    }

    public int getNumber(int r, int c){
        return this.papan[r][c];
    }
    
}