public class MosaicPuzzle{
    private int[][] papan; //-1 untuk sel tanpa angka
    private int size; //ukuran mosaic (row atau col)

    //constructor
    public MosaicPuzzle(int size){
        this.size = size;
        this.papan = new int[size][size];

        //inisialisasi papan dengan -1 (sel tanpa angka)
        for(int r = 0; r < size; r++){
            for(int c = 0; c < size; c++){
                this.papan[r][c] = -1;
            }
        }
    }

    //getter
    public int getSize(){
        return this.size;
    }

    public int getNumber(int r, int c){
        return this.papan[r][c];
    }

    //setter
    public void setNumber(int row, int col, int angka) {
        papan[row][col] = angka;
    }
    
}