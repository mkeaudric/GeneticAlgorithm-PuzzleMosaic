import java.io.PrintWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateProblem {
    // jadi generate kotak item dulu
    // baru generate angka disekitarnya
    // beberapa constraint angka (ga kepake ternyata, krn udah kebates pas nyari item di sekitar kotak) :
    // - corner : angka <= 4
    // - tepi : angka <= 6
    // - sisanya : angka <= 9
    // probabilitas angka : angka 9 paling jarang muncul, 8 dan 0 kedua paling jarang, dst
    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        double threshold = Double.parseDouble(args[1]);
        
        generatePuzzle(size, threshold);
    }

    public static void generatePuzzle(int size, double threshold){
        int[][] papan = new int[size][size];
        Random rand = new Random(); // ini random nya ga pake RNG karena ga hubungan langsung sama GA, jadi gpp pake new Random sendiri

        boolean[][] kotakItem = new boolean[size][size];
        int i, j, k;

        // inisialisasi papan dengan kosong (-1)
        for (i=0; i < size; i++)
            for(j=0; j < size; j++)
                papan[i][j] = -1;

        // generate kotak item;
        for (i=0; i < size; i++)
            for(j=0; j < size; j++)
                if(rand.nextDouble() < 0.6)
                    kotakItem[i][j] = true; // true nandain item

        // debug(kotakItem);

        // atas, atas kanan, kanan, bawah kanan, dst (jarum jam)
        int[] arahY = {-1, -1, 0, 1, 1, 1, 0, -1};
        int[] arahX = {0, 1, 1, 1, 0, -1, -1, -1};

        // generate angka di sekitar kotak item
        for (i=0; i < size; i++) {
            for(j=0; j < size; j++) {
                // jadi akan skip penempatan angka secara random
                // note makin hard si puzzlenya, angkanya makin banyak, jadi besarin si threshold
                if(rand.nextDouble() >= threshold) continue;

                int num = 0; // ini buat ngitung berapa kotak item di sekitarnya
                // ada 8 arah
                if(kotakItem[i][j]) num++; // kalo kotak itu sendiri item
                for(k=0; k < 8; k++){
                    int arahYcur = arahY[k];
                    int arahXcur = arahX[k];
                    if(i + arahYcur >= 0 && i + arahYcur < size && j + arahXcur >= 0 && j + arahXcur < size)
                        if(kotakItem[i + arahYcur][j + arahXcur]) num++;
                }

                // munculin angka nya sesuai yg constraint probabilitas (angkanya ngasal)
                if(num == 9) if(rand.nextDouble() >= 0.1) continue;
                else if (num == 8 || num == 0) if(rand.nextDouble() >= 0.25) continue;
                else if (num == 7 || num == 1) if(rand.nextDouble() >= 0.5) continue;
                else if (num == 6 || num == 2) if(rand.nextDouble() >= 0.7) continue;
                else if (num == 5 || num == 3) if(rand.nextDouble() >= 0.8) continue;
                else if(rand.nextDouble() >= 0.95) continue;

                papan[i][j] = num;
            }
        }

        // debug(papan);
        print(papan, threshold);
    }

    public static void debug(int[][] papan){
        int i, j, size=papan.length;
        for (i=0; i < size; i++) {
            for (j=0; j < size; j++) {
                System.out.print(papan[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("-------------------");
    }

    public static void debug(boolean[][] kotakItem){
        int i, j, size=kotakItem[0].length;
        for (i=0; i < size; i++) {
            for (j=0; j < size; j++) {
                System.out.print(kotakItem[i][j] ? 1 + " " : 0 + " ");
            }
            System.out.println();
        }
        System.out.println("-------------------");
    }

    public static void print(int[][] papan, double threshold){
        int i, j, size=papan.length;
        String difficulty = (threshold > 0.5) ? "hard" : "easy";
        // mosaic-sizexsize-difficulty (c/: mosaic-5x5-easy)
        String fileName = "mosaic-" + size + "x" + size + "-" + difficulty + ".txt";

        try(PrintWriter writer = new PrintWriter(fileName)){
            writer.println(size);
            for(i=0; i < size; i++) {
                for(j=0; j < size; j++){
                    writer.print(papan[i][j] + " ");
                }
                writer.println();
            }
        } catch (IOException e){
            System.err.println("Gagal eaaa");
        }
    }
}
