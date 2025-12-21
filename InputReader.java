import java.io.BufferedReader;
import java.io.FileReader;

public class InputReader {
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

            for(int col = 0; col < size; col++) {
                int angka = Integer.parseInt(data[col]);
                //set angka di puzzle
                puzzle.setNumber(row, col, angka);
            }
        }
        br.close();
        return puzzle;
    }
}
