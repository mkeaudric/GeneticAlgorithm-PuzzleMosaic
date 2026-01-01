// heuristik ini dipakai untuk menandakan mana kotak yang udah pasti item/putih di awal generasi populasi
// jadi menandakan kotak yang sudah diketahui secara pasti, dan tidak akan terpengaruh mutasi atau crossover ke depannya

public class PenandaHeuristik {
    public static void setFixedAllele(Kromosom kromosom){

        // heuristik 1 : cek 0 dan 9, warnain sekitarnya
        heuristic0and9(kromosom); // DONE

        // heuristik 2 : cek angka 6 di tepi (dan juga di tepi 3 kotak yang udah pasti putih)
        heuristic6Edge(kromosom); // DONE
        
        // heuristik 3 : cek angka 4 di corner
        heuristic4Corner(kromosom); // DONE

        // heuristik 4 : dua angka selisih 2 di tepi peta atau tepi 2 kotak putih
        heuristicDiffBy2onEdge(kromosom); // DONE

        // heuristik 5 : dua angka bersebelahan secara diagonal dengan selisih 5
        heuristicDiffBy5Diag(kromosom); // DONE

        // heuristik 6 : dua angka selisih 6 yang bersebelahan vertikal/horizontal dengan jarak 1 kotak di antaranya
        heuristicDiffBy6Adjacent1Block(kromosom); // DONE

        // heuristik 7 : dua angka bersebelahan vertikal/horizontal dengan selisih 3
        // jika selisih 3, maka angka yang kecil bersebelahan dengan 3 kotak putih, angka yang besar bersebelahan dengan 3 kotak hitam 
        heuristicDiffBy3Adjacent0Block(kromosom);
        
        // heuristik 8 : dua angka bersebelahan vertikal/horizontal dengan selisih 2 + 1 clue
        // (tapi belum pasti posisi kotak hitamnya, jadi serahkan ke genetic algorithm saja kalo emg gaada clue)
        heuristicDiffBy2Adjacent0Block1Clue(kromosom); // RIP nama fungsi

        // heuristik 9 : dua angka bersebelahan vertikal/horizontal dengan selisih 1 + 2 clue
        heuristicDiffBy1Adjacent0Block2Clue(kromosom);

        // heuristik 10 : dua angka bersebelahan vertikal/horizontal yang sama (pair)
        // kalo pair, pasti 3 kotak yang berseberangan itu sama jumlah item/putihnya
        heuristicPairAdjacent0Block(kromosom);

        // heuristik terakhir : fill yang udah pasti, dari angka besar ke kecil (ga ngefek sih mau dari kecil ke besar juga)
        heuristicFillCertain(kromosom); // DONE
    }

    // heuristik 1
    public static void heuristic0and9(Kromosom kromosom){
        MosaicPuzzle puzzle = kromosom.getPuzzle();
        int i, j, size = puzzle.getSize();
        for(i=0; i < size; i++){
            for(j=0; j < size; j++){
                if(puzzle.getNumber(i, j) == 9){
                    // ngeset semua di sekitar angka 9 dan 9 itu sendiri jadi item, dan gaboleh diganti lagi ke depannya saat crossover & mutasi
                    fillSafe(i, j, kromosom, true);
                } else if (puzzle.getNumber(i, j) == 0){
                    // ngeset semua di sekitar angka 0 dan 0 itu sendiri jadi putih (bukan item), dan gaboleh diganti lagi ke depannya saat crossover & mutasi
                    fillSafe(i, j, kromosom, false);
                }
            }
        }
    }

    // heuristik 2 
    private static void heuristic6Edge(Kromosom kromosom) {
        // jadi gw ceknya pake loop seluruh papan yang bukan corner (6 gabisa di corner)
        MosaicPuzzle puzzle = kromosom.getPuzzle();
        int i, j, size = puzzle.getSize();
        // atas kiri ke atas kanan
        for(j=1; j < size-1; j++){ // 1 sampai size-2 karena corner gabisa
            if(puzzle.getNumber(0, j) == 6) fillSafe(0, j, kromosom, true);
        }
        // atas kiri ke bawah kiri
        for(i=1; i < size-1; i++){
            if(puzzle.getNumber(i, 0) == 6) fillSafe(i, 0, kromosom, true);
        }
        // bawah kiri ke bawah kanan
        for(j=1; j < size-1; j++){
            if(puzzle.getNumber(size-1, j) == 6) fillSafe(size-1, j, kromosom, true);
        }
        // atas kanan ke bawah kanan
        for(i=1; i < size-1; i++){
            if(puzzle.getNumber(i, size-1) == 6) fillSafe(i, size-1, kromosom, true);
        }
    }

    // heuristik 3
    private static void heuristic4Corner(Kromosom kromosom) {
        MosaicPuzzle puzzle = kromosom.getPuzzle();
        int size = puzzle.getSize();
        if(puzzle.getNumber(0, 0) == 4) fillSafe(0, 0, kromosom, true);
        if(puzzle.getNumber(0, size-1) == 4) fillSafe(0, size-1, kromosom, true);
        if(puzzle.getNumber(size-1, 0) == 4) fillSafe(size-1, 0, kromosom, true);
        if(puzzle.getNumber(size-1, size-1) == 4) fillSafe(size-1, size-1, kromosom, true);
    }

    // heuristik 4 (pengecekan 2 kotak bersebalahan dengan selisih 2 gw pisah untuk yang edge dan yang di non-edge, beda sama yg 6 krn lebih gampang)
    private static void heuristicDiffBy2onEdge(Kromosom kromosom) {
        // ga cuma tepi tapi juga 4 kotak putih yang udah fixed juga bisa jadi kayak tepi, tapi saya implementasi di heuristik yang berbeda
        MosaicPuzzle puzzle = kromosom.getPuzzle();
        int i, j, size = puzzle.getSize();
        boolean setBlack;
        // atas kiri ke atas kanan
        for(j=0; j < size-1; j++){
            // kalau selisihnya 2 antara 2 kotak yang lagi di cek
            int leftNum = puzzle.getNumber(0, j);
            int rightNum = puzzle.getNumber(0, j+1);
            if(leftNum == -1 || rightNum == -1) continue; // kalo salah satunya ada -1, skip
            if(Math.abs(leftNum - rightNum) == 2){
                setBlack = (leftNum > rightNum); // kalau kotak yang kiri lebih besar, maka true, maka setBlack 3 kotak untuk yang di kiri
                fill3CellsAdjacentVert(0, j, kromosom, true, setBlack); 
                fill3CellsAdjacentVert(0, j+1, kromosom, false, !setBlack); 
            }
        }
        // atas kiri ke bawah kiri
        for(i=0; i < size-1; i++){
            int topNum = puzzle.getNumber(i, 0);
            int bottomNum = puzzle.getNumber(i+1, 0);
            if(topNum == -1 || bottomNum == -1) continue;
            if(Math.abs(topNum - bottomNum) == 2){
                setBlack = (topNum > bottomNum);
                fill3CellsAdjacentHor(i, 0, kromosom, true, setBlack); // atas (top = true)
                fill3CellsAdjacentHor(i+1, 0, kromosom, false, !setBlack); // bawah (top = false)
            }
        }
        // bawah kiri ke bawah kanan (literally sama kek yang atas kiri ke atas kanan, tapi indeks i nya di size-1)
        for(j=0; j < size-1; j++){
            int leftNum = puzzle.getNumber(size-1, j);
            int rightNum = puzzle.getNumber(size-1, j+1);
            if(leftNum == -1 || rightNum == -1) continue;
            if(Math.abs(leftNum - rightNum) == 2){
                setBlack = (leftNum > rightNum);
                fill3CellsAdjacentVert(size-1, j, kromosom, true, setBlack); 
                fill3CellsAdjacentVert(size-1, j+1, kromosom, false, !setBlack); 
            }
        }
        // atas kanan ke bawah kanan (juga sama kek yang atas kanan ke bawah kanan, tapi indeks j nya di size-1)
        for(i=0; i < size-1; i++){
            int topNum = puzzle.getNumber(i, size-1);
            int bottomNum = puzzle.getNumber(i+1, size-1);
            if(topNum == -1 || bottomNum == -1) continue; 
            if(Math.abs(topNum - bottomNum) == 2){
                setBlack = (topNum > bottomNum);
                fill3CellsAdjacentHor(i, size-1, kromosom, true, setBlack);
                fill3CellsAdjacentHor(i+1, size-1, kromosom, false, !setBlack);
            }
        }
    }

    // heuristik 5
    private static void heuristicDiffBy5Diag(Kromosom kromosom) {
        MosaicPuzzle puzzle = kromosom.getPuzzle();
        int i, j, size = puzzle.getSize();
        boolean setBlack;
        
        // ga mungkin di row 0, row n-1, col 0, col n-1
        // ngecek (kolom dari kiri ke kanan, baris dari atas ke bawah) diagonal kotak (i, j) & (i+1, j+1)
        // artinya hanya perlu iterasi sampai size-2 (untuk kolom dan baris)
        for(i = 1; i < size-2; i++){
            for(j = 1; j < size-2; j++){
                int topNum = puzzle.getNumber(i, j);
                int bottomNum = puzzle.getNumber(i+1, j+1);
                if(topNum == -1 || bottomNum == -1) continue; 
                if(Math.abs(topNum - bottomNum) == 5){
                    setBlack = (topNum > bottomNum);
                    fill3CellsAdjacentHor(i, j, kromosom, true, setBlack);
                    fill3CellsAdjacentVert(i, j, kromosom, true, setBlack);
                    fill3CellsAdjacentHor(i+1, j+1, kromosom, false, !setBlack);
                    fill3CellsAdjacentVert(i+1, j+1, kromosom, false, !setBlack);
                }
            }
        }

        // ngecek (kolom dari kiri ke kanan, baris dari atas ke bawah) diagonal kotak (i+1, j) & (i, j+1)
        // berarti kotak atasnya dari kolom 2 hingga size-1, dan barisnya tetep sama kayak sebelumnya dari 1 hingga size-2
        for(i = 2; i < size-1; i++){
            for(j = 1; j < size-2; j++){
                int topNum = puzzle.getNumber(i+1, j);
                int bottomNum = puzzle.getNumber(i, j+1);
                if(topNum == -1 || bottomNum == -1) continue; 
                if(Math.abs(topNum - bottomNum) == 5){
                    setBlack = (topNum > bottomNum);
                    fill3CellsAdjacentHor(i+1, j, kromosom, false, setBlack);
                    fill3CellsAdjacentVert(i+1, j, kromosom, true, setBlack);
                    fill3CellsAdjacentHor(i, j+1, kromosom, true, !setBlack);
                    fill3CellsAdjacentVert(i, j+1, kromosom, false, !setBlack);
                }
            }
        }
    }

    // heuristik 6
    private static void heuristicDiffBy6Adjacent1Block(Kromosom kromosom) {
        MosaicPuzzle puzzle = kromosom.getPuzzle();
        int i, j, size = puzzle.getSize();
        boolean setBlack;
        
        // - x - y - (cek yang horizontal dulu, kolom dari kiri ke kanan, baris dari atas ke bawah) 
        // jadi dari kolom 1 hingga size-4 (< size-3)
        // dari baris 1 hingga size-2 (< size-1)
        for(i = 1; i < size-1; i++){
            for(j = 1; j < size-3; j++){
                int leftNum = puzzle.getNumber(i, j);
                int rightNum = puzzle.getNumber(i, j+2);
                if(leftNum == -1 || rightNum == -1) continue; 
                if(Math.abs(leftNum - rightNum) == 6){
                    setBlack = (leftNum > rightNum);
                    // isi, c/: kalo leftNum > rightNum
                    // B B - W W
                    // B 7 - 1 W
                    // B B - W W
                    fill3CellsAdjacentVert(i, j, kromosom, true, setBlack);
                    fill3CellsMiddle(i, j, kromosom, false, setBlack);
                    fill3CellsAdjacentVert(i, j+2, kromosom, false, !setBlack);
                    fill3CellsMiddle(i, j+2, kromosom, false, !setBlack);
                }
            }
        }

        // -
        // x
        // -
        // y
        // -
        // kebalikannya aja untuk loop i dan j nya
        // B B B
        // B 7 B
        // - - - 
        // W 1 W
        // W W W
        for(i = 1; i < size-3; i++){
            for(j = 1; j < size-1; j++){
                int topNum = puzzle.getNumber(i, j);
                int bottomNum = puzzle.getNumber(i+2, j);
                if(topNum == -1 || bottomNum == -1) continue; 
                if(Math.abs(topNum - bottomNum) == 6){
                    setBlack = (topNum > bottomNum);
                    fill3CellsAdjacentHor(i, j, kromosom, true, setBlack);
                    fill3CellsMiddle(i, j, kromosom, true, setBlack);
                    fill3CellsAdjacentHor(i+2, j, kromosom, false, !setBlack);
                    fill3CellsMiddle(i+2, j, kromosom, true, !setBlack);
                }
            }
        }
    }

    // heuristik 7
    private static void heuristicDiffBy3Adjacent0Block(Kromosom kromosom) {

    }

    // heuristik 8
    private static void heuristicDiffBy2Adjacent0Block1Clue(Kromosom kromosom) {
        
    }

    // heuristik 9
    private static void heuristicDiffBy1Adjacent0Block2Clue(Kromosom kromosom) {
        
    }

    // heuristik 10
    private static void heuristicPairAdjacent0Block(Kromosom kromosom) {
        
    }

    // heuristik 11
    // private static void heuristicFillCertain(Kromosom kromosom){
    //     MosaicPuzzle puzzle = kromosom.getPuzzle();
    //     int i, j, size = puzzle.getSize();

    //     int numberOfNeighbours;
    //     // corner <= 4, tepi <= 6, lainnya <= 9

    //     for(i=0; i < size-1; i++){
    //         for(j=0; j < size-1; j++){
    //             int curNum = puzzle.getNumber(i, j); // angka di kotak (i, j)

    //             if(checkCorner(i, j, size)) numberOfNeighbours = 4;
    //             else if(checkEdge(i, j, size)) numberOfNeighbours = 6;
    //             else numberOfNeighbours = 9;

    //             // kalo udah ada n hitam yg fixed disekitarnya, sisa kotaknya pasti putih
    //             if(curNum != -1 && countNeighbours(i, j, kromosom, true) == curNum) fillAll(i, j, kromosom, false);
    //             // atau kalo udah ada 9 - n putih yang fixed disekitarnya, sisa kotaknya pasti hitam
    //             if(curNum != -1 && countNeighbours(i, j, kromosom, false) == (numberOfNeighbours - curNum)) fillAll(i, j, kromosom, true);
    //         }
    //     }
    // }

    public static void heuristicFillCertain(Kromosom kromosom) {
        MosaicPuzzle puzzle = kromosom.getPuzzle();
        int size = puzzle.getSize();

        // Loop harus sampai size, bukan size-1
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int curNum = puzzle.getNumber(i, j);
                if (curNum == -1) continue;

                // Hitung berapa banyak tetangga yang SUDAH kita kunci sebagai HITAM
                int fixedBlack = 0;
                int unknown = 0;
                
                // Cek 9 tetangga
                for (int r = i-1; r <= i+1; r++) {
                    for (int c = j-1; c <= j+1; c++) {
                        if (r >= 0 && r < size && c >= 0 && c < size) {
                            if (kromosom.getFixedAllele(r, c)) {
                                if (kromosom.getBit(r, c)) fixedBlack++;
                            } else {
                                unknown++; // Kotak yang belum diputuskan
                            }
                        }
                    }
                }

                // LOGIKA 1: Jika jumlah HITAM yang sudah fix == angka puzzle
                // Maka semua yang 'unknown' harus jadi PUTIH
                if (fixedBlack == curNum && unknown > 0) {
                    fillSafe(i, j, kromosom, false);
                }

                // LOGIKA 2: Jika (Hitam yang fix + Unknown) == angka puzzle
                // Maka semua yang 'unknown' harus jadi HITAM
                if (fixedBlack + unknown == curNum && unknown > 0) {
                    fillSafe(i, j, kromosom, true);
                }
            }
        }
    }


    // FUNGSI UTILITAS

    // fill semua kotak di sekitar (r,c) termasuk (r,c) itu sendiri
    // * * *
    // * c *
    // * * *
    private static void fillSafe(int r, int c, Kromosom kromosom, boolean isBlack) {
        int size = kromosom.getSize();
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                if (i >= 0 && i < size && j >= 0 && j < size) {
                    if (!kromosom.getFixedAllele(i, j)) { // Kunci Utama: Jangan timpa yang sudah fix
                        kromosom.setBit(i, j, isBlack);
                        kromosom.setFixedAllele(i, j, true);
                    }
                }
            }
        }
    }

    // ini mirip spt yang di FitnessFunction, tapi gw kasih parameter black buat cek warna hitam atau putih (bisa lgs masukin parameter)
    private static int countNeighbours(int i, int j, Kromosom kromosom, Boolean black){
        int ct = 0, size = kromosom.getSize();

        //cek 8 tetangga dan sel itu sendiri
        for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
            for (int deltaCol = -1; deltaCol <= 1; deltaCol++) {
                int newRow = i + deltaRow;
                int newCol = j + deltaCol;
                if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size) {
                    if ((black && kromosom.getFixedAllele(newRow, newCol) && kromosom.getBit(newRow, newCol)) 
                        || (!black && kromosom.getFixedAllele(newRow, newCol) && !kromosom.getBit(newRow, newCol))) ct++;
                }
            }
        }
        return ct;
    }

    // pembantu untuk ngisi 3 cell lurus tetangganya

    // * * * 
    //   c
    // ATAU
    //   c
    // * * *
    private static void fill3CellsAdjacentHor(int i, int j, Kromosom kromosom, Boolean top, Boolean black){
        int k, size = kromosom.getSize();
        if(top){
            // kalau ga melebihi batas atas
            if(i-1 >= 0){
                // fill 3 kotak secara horizontal
                for(k=j-1; k <= j+1; k++){
                    if(k >= 0 && k < size){
                        kromosom.setBit(i-1, k, black);
                        kromosom.setFixedAllele(i-1, k, true);
                    }
                }
            }
        } else{
            // kalau ga melebihi batas bawah
            if(i+1 < size){
                // fill 3 kotak secara horizontal
                for(k=j-1; k <= j+1; k++){
                    if(k >= 0 && k < size){
                        kromosom.setBit(i+1, k, black);
                        kromosom.setFixedAllele(i+1, k, true);
                    }
                }
            }
        }
    }

    // *          *
    // * c ATAU c *
    // *          *
    private static void fill3CellsAdjacentVert(int i, int j, Kromosom kromosom, Boolean left, Boolean black){
        int k, size = kromosom.getSize();
        if(left){
            // kalau ga melebihi batas kiri
            if(j-1 >= 0){
                // fill 3 kotak secara vertikal
                for(k=i-1; k <= i+1; k++){
                    if(k >= 0 && k < size){
                        kromosom.setBit(k, j-1, black);
                        kromosom.setFixedAllele(k, j-1, true);
                    }
                }
            }
        } else{
            // kalau ga melebihi batas kanan
            if(j+1 < size){
                // fill 3 kotak secara vertikal
                for(k=i-1; k <= i+1; k++){
                    if(k >= 0 && k < size){
                        kromosom.setBit(k, j+1, black);
                        kromosom.setFixedAllele(k, j+1, true);
                    }
                }
            }
        }
    }

    // pembantu untuk isi 3 cell lurus di tengah (termasuk cell itu sendiri)

    // * c *
    // ATAU
    // *
    // c
    // *
    private static void fill3CellsMiddle(int i, int j, Kromosom kromosom, Boolean horizontal, Boolean black){
        int k, size = kromosom.getSize();
        if(horizontal){
            for(k=j-1; k <= j+1; k++){
                if(k >= 0 && k < size){
                    kromosom.setBit(i, k, black);
                    kromosom.setFixedAllele(i, k, true);
                }
            }
        } else{
            for(k=i-1; k <= i+1; k++){
                if(k >= 0 && k < size){
                    kromosom.setBit(k, j, black);
                    kromosom.setFixedAllele(k, j, true);
                }
            }
        }
    }
    
    private static boolean checkCorner(int i, int j, int size){
        return ((i == 0 && j == 0) || (i == 0 && j == size-1) || (i == size-1 && j == 0) || (i == size-1 && j == size-1));
    }

    private static boolean checkEdge(int i, int j, int size){
        return(!checkCorner(i, j, size) && (i == 0 || i == size-1 || j == 0 || j == size-1));
    }

    // private static boolean checkAdjacent3Whites(int i, int j, Kromosom kromosom){
    //     int[] arahY = {-1, 0, 1, 0};
    //     int[] arahX = {0, 1, 0, -1};
    //     int k, size = kromosom.getSize();
    //     for(k=0; k < 4; k++){
    //         int arahYcur = i + arahY[k];
    //         int arahXcur = j + arahX[k];
    //         // kalau dia ga melebihi constraint, dan kalau dia allele nya udah fixed, dan warnanya putih
    //         if(arahYcur >= 0 && arahYcur < size && arahXcur >= 0 && arahXcur < size
    //             && kromosom.getFixedAllele(arahYcur, arahXcur) && !kromosom.getBit(arahYcur, arahXcur)){
    //             // cek ke samping dari si kotak putih itu (apakah ada kotak putih di kiri dan kanannya)
    //             if(k%2 == 0){ // kalau k nya genap (artinya lagi cek yang arah atas/bawah, jadi cek nya horizontal)
    //                 boolean kiri = false, kanan = false;
    //                 if(arahXcur - 1 >= 0 && kromosom.getFixedAllele(arahYcur, arahXcur - 1) && !kromosom.getBit(arahYcur, arahXcur - 1))
    //                     kiri = true;
    //                 if(arahXcur + 1 < size && kromosom.getFixedAllele(arahYcur, arahXcur + 1) && !kromosom.getBit(arahYcur, arahXcur + 1))
    //                     kanan = true;
    //                 // kalau kiri dan kanannya putih, jadi ada 3 kotak putih sebaris, return true
    //                 return kiri && kanan; 
    //             } else{ // k nya ganjil (artinya lagi cek yang kiri/kanan, jadi cek nya vertikal)
    //                 boolean atas = false, bawah = false;
    //                 if(arahYcur - 1 >= 0 && kromosom.getFixedAllele(arahYcur - 1, arahXcur) && !kromosom.getBit(arahYcur - 1, arahXcur))
    //                     atas = true;
    //                 if(arahYcur + 1 < size && kromosom.getFixedAllele(arahYcur + 1, arahXcur) && !kromosom.getBit(arahYcur + 1, arahXcur))
    //                     bawah = true;
    //                 // kalau atas dan bawahnya putih, kado ada 3 kotak putih sebaris, return true
    //                 return atas && bawah;
    //             }
    //         }
    //     }
    //     return false;
    // }
}