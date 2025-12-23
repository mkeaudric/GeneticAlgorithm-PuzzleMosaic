import java.util.BitSet;

public class Kromosom {
    private BitSet genes; //representasi bit kromosom
    private BitSet fixedAllele; //buat nandain alel yang udah pasti bagian solusi (1 artinya udah fix)
    private int length; //panjang kromosom (jumlah sel)
    private int size; //ukuran puzzle (row atau col)
    private MosaicPuzzle puzzle;

    //konstruktor
    public Kromosom(int size, MosaicPuzzle puzzle) {
        this.size = size;
        this.length = size * size;
        this.genes = new BitSet(length);
        this.fixedAllele = new BitSet(length);
        this.puzzle = puzzle;
    }

    // item genes : 0 1 0 1 
    // fixed allele : 0 1 1 0 

    public static Kromosom createRandomKromosom(double probabilitasHitam, MosaicPuzzle puzzle) {
        Kromosom kromosom = new Kromosom(puzzle.getSize(), puzzle);

        //cek apakah ada fixed allele yang diset
        try{
            PenandaHeuristik.setFixedAllele(kromosom);
        } catch(Exception e){
            System.out.println("Tidak ada fixed allele yang diset");
        }
        

        for (int i = 0; i < kromosom.length; i++) {
            //tentukan apakah sel ini hitam berdasarkan probabilitas
            if(RNG.rand.nextDouble() < probabilitasHitam && !kromosom.fixedAllele.get(i)) {
                kromosom.setBit(i, true); //set sel jadi hitam
            }
        }
        return kromosom;
    }

    //set bit index ke...(array 1D)
    public void setBit(int index, boolean value) {
        genes.set(index, value);
    }

    //set bit pada posisi (row, col)
    public void setBit(int row, int col, boolean value) {
        int index = row * size + col;
        genes.set(index, value);
    }

    //get bit index ke...(array 1D)
    public boolean getBit(int index) {
        return genes.get(index);
    }

    //get bit pada posisi (row, col)
    public boolean getBit(int row, int col) {
        int index = row * size + col;
        return genes.get(index);
    }

    //mengembalikan panjang kromosom
    public int getlength() {
        return length;
    }

    public int getSize() {
        return size;
    }

    //mengembalikan salinan BitSet genes
    public BitSet getBitSet() {
        return (BitSet) genes.clone();
    }
    
    //set alel yang sudah pasti pada index 1D
    public void setFixedAllele(int index, boolean isFixed) {
        fixedAllele.set(index, isFixed);
    }

    //set alel yang sudah pasti pada posisi (row,col)
    public void setFixedAllele(int row, int col, boolean isFixed) {
        int index = row * size + col;
        fixedAllele.set(index, isFixed);
    }

    //check whether allele at index is fixed
    public boolean getFixedAllele(int index) {
        return fixedAllele.get(index);
    }

    //check whether allele at (row,col) is fixed
    public boolean getFixedAllele(int row, int col) {
        int index = row * size + col;
        return fixedAllele.get(index);
    }

    //deep-copy kromosom
    public Kromosom copy() {
        Kromosom copy = new Kromosom(this.size, this.puzzle);
        copy.genes = (BitSet) this.genes.clone();
        copy.fixedAllele = (BitSet) this.fixedAllele.clone();
        return copy;
    }

    public MosaicPuzzle getPuzzle(){
        return puzzle;
    }

    // method buat bantu ngecek heuristik aja, print si kromosom yang udah fixed saat ini
    public void printCurrentFixedKromosomAsGrid(){
        int i, j;
        for(i=0; i < size; i++){
            for(j=0; j < size; j++){
                if(!this.getFixedAllele(i, j)) System.out.print(". "); // Unknown, masih belum fixed
                else System.out.print(this.getBit(i, j) ? "B " : "W ");
            }
            System.out.println();
        }
    }
}
