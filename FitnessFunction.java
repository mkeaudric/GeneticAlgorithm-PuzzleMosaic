public class FitnessFunction {
    private MosaicPuzzle puzzle;
    
    public FitnessFunction(MosaicPuzzle puzzle) {
        this.puzzle = puzzle;
    }

    //hitung fitness dengan melihat sel yang memiliki angka dan membandingkannya dengan jumlah sel hitam di sekitarnya
    public double calculateFitness(Kromosom kromosom) {
       int totalError = 0;

       //ambil ukuran mosaic dari kromosom
       int size = kromosom.getSize();

       for (int row = 0; row < size; row++) {
           for (int col = 0; col < size; col++) {

                //ambil angka target dari puzzle
                int target = puzzle.getNumber(row, col);

                //lewati sel tanpa angka
                if(target == -1) {
                   continue;
                } 

                //hitung jumlah sel hitam di sekitar
                int actual = countBlackNeighbors(kromosom, row, col, size);

                //hitung selisih antara angka target dengan jumlah kotak hitam di sekitar
                totalError += Math.abs(target - actual);
           }
       }

    //fitness berada di rentang (0, 1], semakin kecil error semakin mendekati 1
       return 1.0 / (1 + totalError); 
   }

   private int countBlackNeighbors(Kromosom kromosom, int row, int col, int size) {
       int count = 0;

       //cek 8 tetangga dan sel itu sendiri
       for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
           for (int deltaCol = -1; deltaCol <= 1; deltaCol++) {
               int newRow = row + deltaRow;
               int newCol = col + deltaCol;

               //cek batas papan
               if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size) {
                   if (kromosom.getBit(newRow, newCol)) {
                       count++;
                   }
               }
           }
       }
       return count;
   }
}
