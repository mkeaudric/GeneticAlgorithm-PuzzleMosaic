import java.util.Scanner;

/**
 * Berfungsi untuk mencetak hasil validasi dari teka-teki Mosaic.
 * Membaca input manual dari pengguna dan membandingkannya dengan solusi 
 * @author Kelompok AI Mosaic
 * @version 1.0
 */
public class MosaicResultPrint {

    /**
     * Menampilkan hasil validasi betul dan berwarna di terminal.
     * @param args args[0] harus berisi jalur file peta puzzle (.txt).
     * @throws Exception Jika terjadi kesalahan saat pembacaan file atau pemrosesan input.
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java MosaicResultPrint <filePeta.txt>");
            return;
        }

        //baca puzzle dari file
        String filePeta = args[0];
        MosaicPuzzle puzzle = InputReader.bacaPuzzle(filePeta);
        int size = puzzle.getSize();


        Kromosom manualSolution = new Kromosom(size, puzzle);
        
        Scanner sc = new Scanner(System.in);
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (sc.hasNext()) {
                    String token = sc.next().trim().toUpperCase();
                    
                    if (token.contains("B")) {
                        manualSolution.setBit(i, j, true);
                    } else if (token.contains("W")) {
                        manualSolution.setBit(i, j, false);
                    } else if (token.equals(".")) {
                        manualSolution.setBit(i, j, false);
                    }
                }
            }
        }

        printValidation(manualSolution);
    }

    /**
     * Menghitung jumlah sel berwarna hitam.
     * Perhitungan mencakup area 3x3 (sel itu sendiri dan 8 tetangganya).
     * @param kromosom Objek kromosom yang merepresentasikan grid solusi.
     * @param row      Indeks baris dari sel yang diperiksa.
     * @param col      Indeks kolom dari sel yang diperiksa.
     * @param size     Ukuran sisi grid (asumsi grid persegi).
     * @return Jumlah total sel hitam di area 3x3 sekitar (row, col).
     */
    // copy dari Fitness
    private static int countBlackNeighbors(Kromosom kromosom, int row, int col, int size) {
        int count = 0;
        for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
            int newRow = row + deltaRow;
            if (newRow < 0 || newRow >= size) continue;

            for (int deltaCol = -1; deltaCol <= 1; deltaCol++) {
                int newCol = col + deltaCol;
                if (newCol >= 0 && newCol < size) {
                    if (kromosom.getBit(newRow, newCol)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Print representasi visual dari grid ke terminal dengan validasi warna.
     * Sel yang memiliki angka petunjuk (clue) akan berwarna biru jika jumlah tetangga hitam sudah tepat, atau merah jika masih salah.
     * * @param kromosom Objek kromosom yang akan divalidasi dan dicetak hasilnya.
     */
    public static void printValidation(Kromosom kromosom) {
        MosaicPuzzle puzzle = kromosom.getPuzzle();
        int size = puzzle.getSize();

        // Kode warna ANSI
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_RED = "\u001B[31m";   // Merah (Salah)
        final String ANSI_BLUE = "\u001B[34m";  // Biru (Benar)
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // 1. Ambil angka clue
                int targetNumber = puzzle.getNumber(i, j);
                boolean isBlack = kromosom.getBit(i, j);
                String symbol = isBlack ? "B" : "W";
                String color = ANSI_RESET;
                String cellContent;

                // 2. Cek Validitas & Format String
                if (targetNumber != -1) {
                    // Hitung realisasi saat ini
                    int currentCount = countBlackNeighbors(kromosom, i, j, size);

                    if (currentCount == targetNumber) {
                        color = ANSI_BLUE; // BENAR
                    } else {
                        color = ANSI_RED;  // SALAH
                    }
                    
                    // Format: Angka + Huruf + Spasi (Total 3 char) -> "2B "
                    cellContent = targetNumber + symbol + " ";
                } else {
                    // Format: Huruf + 2 Spasi (Total 3 char) -> "B  "
                    cellContent = symbol + "  ";
                }

                // 3. Print
                System.out.print(color + cellContent + ANSI_RESET);
            }
            System.out.println(); // Pindah baris
        }
    }
}