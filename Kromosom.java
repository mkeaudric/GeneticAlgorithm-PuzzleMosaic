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

        PenandaHeuristik.setFixedAllele(kromosom);

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
    public int Getlength() {
        return length;
    }

    public int getSize() {
        return size;
    }

    //mengembalikan salinan BitSet genes
    public BitSet getBitSet() {
        return (BitSet) genes.clone();
    }

    //set fixed allele index ke...(array 1D)
    public void setFixedAllele(int index, boolean value) {
        genes.set(index, value);
    }

    //set fixed allele pada posisi (row, col)
    public void setFixedAllele(int row, int col, boolean value) {
        int index = row * size + col;
        genes.set(index, value);
    }

    //get fixed allele index ke...(array 1D)
    public boolean getFixedAllele(int index) {
        return genes.get(index);
    }

    //get fixed allele pada posisi (row, col)
    public boolean getFixedAllele(int row, int col) {
        int index = row * size + col;
        return genes.get(index);
    }

    public MosaicPuzzle getPuzzle(){
        return puzzle;
    }
}
