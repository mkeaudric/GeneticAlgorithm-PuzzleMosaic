import java.util.BitSet;

/**
 * Kelas Kromosom merepresentasikan struktur data genetik dari sebuah solusi Mosaic Puzzle.
 * Menggunakan {@link BitSet} untuk efisiensi memori dalam menyimpan status sel (Hitam/Putih).
 * Memiliki mekanisme 'Fixed Allele' untuk mengunci sel yang sudah dipastikan kebenarannya 
 * melalui teknik heuristik agar tidak berubah selama proses evolusi.
 * @author Kelompok AI Mosaic
 * @version 1.0
 */
public class Kromosom {
    private BitSet genes; //representasi bit kromosom
    private BitSet fixedAllele; //buat nandain alel yang udah pasti bagian solusi (1 artinya udah fix)
    private int length; //panjang kromosom (jumlah sel)
    private int size; //ukuran puzzle (row atau col)
    private MosaicPuzzle puzzle;

    /**
     * Konstruktor untuk menginisialisasi kromosom kosong dengan ukuran tertentu.
     * @param size Dimensi puzzle.
     * @param puzzle Referensi puzzle utama.
     */
    public Kromosom(int size, MosaicPuzzle puzzle) {
        this.size = size;
        this.length = size * size;
        this.genes = new BitSet(length);
        this.fixedAllele = new BitSet(length);
        this.puzzle = puzzle;
    }

    // item genes : 0 1 0 1 
    // fixed allele : 0 1 1 0 

    /**
     * Factory method untuk membuat kromosom acak berdasarkan template heuristik.
     * @param probabilitasHitam Peluang sebuah sel non-fixed diatur menjadi hitam.
     * @param puzzle Referensi puzzle utama.
     * @param heuristicTemplate Template yang mengandung gen-gen yang sudah dikunci oleh sistem.
     * @return Objek Kromosom baru yang sudah terinisialisasi.
     */
    public static Kromosom createRandomKromosom(double probabilitasHitam, MosaicPuzzle puzzle, Kromosom heuristicTemplate) {
        Kromosom kromosom = new Kromosom(puzzle.getSize(), puzzle);

        kromosom.fixedAllele = (BitSet)heuristicTemplate.fixedAllele.clone(); //salin fixed allele
        kromosom.genes = (BitSet)heuristicTemplate.genes.clone(); //salin warna yg udah fixed

        for (int i = 0; i < kromosom.length; i++) {
            if(!kromosom.fixedAllele.get(i)){
                //tentukan apakah sel ini hitam berdasarkan probabilitas
                if(RNG.rand.nextDouble() < probabilitasHitam) {
                    kromosom.setBit(i, true); //set sel jadi hitam
                } else{
                    kromosom.setBit(i, false); //gaperlu sih tapi buat jaga" aja
                }
            }
        }
        return kromosom;
    }

    /**
     * Set bit index ke...(array 1D).
     * @param index Indeks ke-n dalam BitSet.
     * @param value true untuk Hitam, false untuk Putih.
     */
    public void setBit(int index, boolean value) {
        genes.set(index, value);
    }

    /**
     * Mengatur nilai bit berdasarkan koordinat baris dan kolom (2D).
     * @param row Indeks baris.
     * @param col Indeks kolom.
     * @param value true untuk Hitam, false untuk Putih.
     */
    public void setBit(int row, int col, boolean value) {
        int index = row * size + col;
        genes.set(index, value);
    }

    /**
     * Mendapat bit index ke...(array 1D)
     * @param index Indeks linear.
     * @return Status sel (true/false).
     */
    public boolean getBit(int index) {
        return genes.get(index);
    }

    /**
     * Mendapatkan nilai bit pada koordinat baris dan kolom (2D).
     * @param row Indeks baris.
     * @param col Indeks kolom.
     * @return Status sel pada koordinat tersebut.
     */
    public boolean getBit(int row, int col) {
        int index = row * size + col;
        return genes.get(index);
    }

    //mengembalikan panjang kromosom
    public int getLength() {
        return length;
    }

    //mengembalikan ukuran puzzle (row/col)
    public int getSize() {
        return size;
    }

    /**
     * Mengambil salinan BitSet genes agar data asli tidak termodifikasi dari luar.
     * @return Salinan dari BitSet genes.
     */
    public BitSet getBitSet() {
        return (BitSet) genes.clone();
    }
    
    /**
     * Menandai alel sebagai 'Fixed' agar tidak berubah pada index 1D.
     * @param index Indeks linear.
     * @param isFixed true jika ingin dikunci.
     */
    public void setFixedAllele(int index, boolean isFixed) {
        fixedAllele.set(index, isFixed);
    }

     /**
     * Menandai alel sebagai 'Fixed' agar tidak berubah pada index 2D.
     * @param row index baris
     * @param col index kolom
     * @param isFixed true jika ingin dikunci.
     */
    public void setFixedAllele(int row, int col, boolean isFixed) {
        int index = row * size + col;
        fixedAllele.set(index, isFixed);
    }

     /**
     * Mendapatkan status 'Fixed' dari alel pada index 1D.
     * @param index Indeks linear.
     * @return true jika alel sudah fixed, false jika belum.
     */
    public boolean getFixedAllele(int index) {
        return fixedAllele.get(index);
    }

    /**
     * Mendapatkan status 'Fixed' dari alel pada index 1D.
     * @param row index baris
     * @param col index kolom
     * @return true jika alel sudah fixed, false jika belum.
     */
    public boolean getFixedAllele(int row, int col) {
        int index = row * size + col;
        return fixedAllele.get(index);
    }

    /**
     * Membuat deep-copy dari kromosom saat ini.
     * @return Objek Kromosom baru yang identik.
     */
    public Kromosom copy() {
        Kromosom copy = new Kromosom(this.size, this.puzzle);
        copy.genes = (BitSet) this.genes.clone();
        copy.fixedAllele = (BitSet) this.fixedAllele.clone();
        return copy;
    }

    public MosaicPuzzle getPuzzle(){
        return puzzle;
    }

    // method print kromosom yang terkena heuristic
    public void printCurrentFixedKromosom(){
        int i, j;
        for(i=0; i < size; i++){
            for(j=0; j < size; j++){
                if(!this.getFixedAllele(i, j)) System.out.print(". "); // Unknown, masih belum fixed
                else System.out.print(this.getBit(i, j) ? "B " : "W ");
            }
            System.out.println();
        }
    }

    // method print kromosom saat ini
    public void printCurrentKromosom(){
        int i, j;
        for(i=0; i < size; i++){
            for(j=0; j < size; j++){
                System.out.print(this.getBit(i, j) ? "B " : "W ");
            }
            System.out.println();
        }
    }
}
