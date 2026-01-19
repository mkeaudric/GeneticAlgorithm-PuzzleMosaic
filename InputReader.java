import java.io.BufferedReader;
import java.io.FileReader;
/**
 * Kelas InputReader berfungsi untuk membaca data dari file eksternal.
 * @author Kelompok AI Mosaic
 * @version 1.0
 */
public class InputReader {

    /**
     * Membaca file teks yang berisi representasi angka-angka Mosaic Puzzle.
     * Format file yang diharapkan: Baris pertama adalah ukuran (N), 
     * baris berikutnya adalah matriks N x N berisi angka (atau -1 jika kosong).
     * @param namaFile Path atau nama file yang akan dibaca.
     * @return Objek MosaicPuzzle yang telah terisi angka.
     * @throws Exception Jika file tidak ditemukan, format ukuran salah, atau baris data kurang.
     */
    public static MosaicPuzzle bacaPuzzle(String namaFile) throws Exception {
        // buka file
        BufferedReader br = new BufferedReader(new FileReader(namaFile));

        // baca ukuran puzzle
        int size = Integer.parseInt(br.readLine().trim());

        // buat mosaic puzzle kosong
        MosaicPuzzle puzzle = new MosaicPuzzle(size);

        // baca isi puzzle
        for(int row = 0; row < size; row++) {
            // baca satu baris
            String line = br.readLine();

            // cek apakah baris ada
            if(line == null) {
                br.close();
                throw new Exception("baris kurang");
            }

            //pisah angka-angka di baris
            String[] data = line.trim().split("\\s+");

            //cek jumlah angka sesuai ukuran puzzle
            if(data.length != size) {
                br.close();
                throw new Exception("jumlah angka tidak sesuai ukuran puzzle di baris " + (row + 1));
            }

            //buat pengisian angka di puzzle
            for(int col = 0; col < size; col++) {
                int angka = Integer.parseInt(data[col]);
                //set angka di puzzle
                puzzle.setNumber(row, col, angka);
            }
        }
        br.close();
        return puzzle;
    }

    /**
     * Membaca file parameter untuk konfigurasi Algoritma Genetik.
     * Diharapkan file berisi 9 baris data parameter secara berurutan.
     * @param namaFile Path file parameter (ctjh: "parameter.txt").
     * @return Objek GAParamater yang berisi konfigurasi lengkap.
     * @throws Exception Jika baris parameter kurang atau format angka salah.
     */
    public static GAParamater bacaParameter(String namaFile) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(namaFile));

        try{
            //array string untuk nampung 9 parameter
            String[] baris = new String[9];

            //loop untuk baca 9 baris
            for(int i = 0; i < 9; i++) {
                baris[i] = br.readLine();
                //cek apakah baris ada
                if(baris[i] == null) {
                    br.close();
                    throw new Exception("Parameter kurang di baris " + (i + 1));
                }
                //trim spasi
                baris[i] = baris[i].trim();
            }
            //parsing parameter dari string ke tipe data yang sesuai
            int totalGeneration = Integer.parseInt(baris[0]);
            int populationSize = Integer.parseInt(baris[1]);
            double crossoverRate = Double.parseDouble(baris[2]);
            double mutationRate = Double.parseDouble(baris[3]);
            double elitismPercent = Double.parseDouble(baris[4]);
            int tournamentSize = Integer.parseInt(baris[5]);
            String selectionMethod = baris[6];
            String crossoverMethod = baris[7];
            double probabilitasHitam = Double.parseDouble(baris[8]);

            return new GAParamater(totalGeneration, populationSize, crossoverRate, mutationRate, elitismPercent, tournamentSize, selectionMethod, crossoverMethod, probabilitasHitam);
        }
        
        finally {
            br.close();
        }
    }
}
