// heuristik ini dipakai untuk menandakan mana kotak yang udah pasti item/putih di awal generasi populasi
// jadi menandakan kotak yang sudah diketahui secara pasti, dan tidak akan terpengaruh mutasi atau crossover ke depannya

public class PenandaHeuristik {
    public static void setFixedAllele(Kromosom kromosom){

        // heuristik 1 : cek 0 dan 9, warnain sekitarnya DONE
        heuristic0and9(kromosom);

        // heuristik 2 : dua angka selisih 2 di tepi peta atau tepi 2 kotak putih DONE
        heuristicDiffBy2onEdge(kromosom); // penamaannya gg sih

        // heuristik 3 : dua angka bersebelahan secara diagonal dengan selisih 5
        heuristicDiffBy5Diag(kromosom);

        // heuristik 4 : dua angka selisih 6 yang bersebelahan vertikal/horizontal dengan jarak 1 kotak di antaranya
        heuristicDiffBy6Adjacent1Block(kromosom); // plis cuma kepikiran ini namannya

        // heuristik 5 : dua angka bersebelahan vertikal/horizontal dengan selisih 3
        // jika selisih 3, maka angka yang kecil bersebelahan dengan 3 kotak putih, angka yang besar bersebelahan dengan 3 kotak hitam 
        heuristicDiffBy3Adjacent0Block(kromosom);
        
        // heuristik 6 : dua angka bersebelahan vertikal/horizontal dengan selisih 2 + 1 clue
        // (tapi belum pasti posisi kotak hitamnya, jadi serahkan ke genetic algorithm saja kalo emg gaada clue)
        heuristicDiffBy2Adjacent0Block1Clue(kromosom); // RIP nama fungsi

        // heuristik 7 : dua angka bersebelahan vertikal/horizontal dengan selisih 1 + 2 clue
        heuristicDiffBy1Adjacent0Block2Clue(kromosom);

        // heuristik 8 : dua angka bersebelahan vertikal/horizontal yang sama (pair)
        // kalo pair, pasti 3 kotak yang berseberangan itu sama jumlah item/putihnya
        heuristicPairAdjacent0Block(kromosom);

        // heuristik 9 : cek angka 6 di tepi (dan juga di tepi 3 kotak yang udah pasti putih) DONE
        heuristic6Edge(kromosom);
        
        // heuristik 10 : cek angka 4 di corner (dan juga di corner 2 kotak yang udah pasti putih)
        heuristic4Corner(kromosom);

        // heuristik terakhir : fill yang udah pasti, dari angka besar ke kecil (ga ngefek sih mau dari kecil ke besar juga) DONE
        heuristicFillCertain(kromosom);
    }

    // heuristik 1
    public static void heuristic0and9(Kromosom kromosom){
        MosaicPuzzle puzzle = kromosom.getPuzzle();
        int i, j, size = puzzle.getSize();
        for(i=0; i < size; i++){
            for(j=0; j < size; j++){
                if(puzzle.getNumber(i, j) == 9){
                    // ngeset semua di sekitar angka 9 dan 9 itu sendiri jadi item, dan gaboleh diganti lagi ke depannya saat crossover & mutasi
                    fillAll(i, j, kromosom, true);
                } else if (puzzle.getNumber(i, j) == 0){
                    // ngeset semua di sekitar angka 0 dan 0 itu sendiri jadi putih (bukan item), dan gaboleh diganti lagi ke depannya saat crossover & mutasi
                    fillAll(i, j, kromosom, false);
                }
            }
        }
    }

    // heuristik 2 (pengecekan 2 kotak bersebalahan dengan selisih 2 gw pisah untuk yang edge dan yang di non-edge, beda sama yg 6 krn lebih gampang)
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

    // heuristik 3
    private static void heuristicDiffBy5Diag(Kromosom kromosom) {
        
    }

    // heuristik 4
    private static void heuristicDiffBy6Adjacent1Block(Kromosom kromosom) {

    }

    // heuristik 5
    private static void heuristicDiffBy3Adjacent0Block(Kromosom kromosom) {

    }

    // heuristik 6
    private static void heuristicDiffBy2Adjacent0Block1Clue(Kromosom kromosom) {
        
    }

    // heuristik 7
    private static void heuristicDiffBy1Adjacent0Block2Clue(Kromosom kromosom) {
        
    }

    // heuristik 8
    private static void heuristicPairAdjacent0Block(Kromosom kromosom) {
        
    }

    // heuristik 9 (ini gw gabung pengecekan di edge sama yang di 3 kotak yang pasti putih, krn implementasinya cukup kecil)
    private static void heuristic6Edge(Kromosom kromosom) {
        // sebenernya ga cuma tepi aja, tapi bisa kalau ada barisan 3 kotak yang udah pasti putih (misal sekitar angka 0)
        // jadi gw ceknya pake loop seluruh papan yang bukan corner (6 gabisa di corner)
        MosaicPuzzle puzzle = kromosom.getPuzzle();
        int i, j, size = puzzle.getSize();
        for(i=0; i < size; i++){
            for(j=0; j < size; j++){
                if(checkCorner(i, j, size)) continue; // kalo corner, gamungkin (sebenernya gausah cek juga karena pasti ga ada 6 di corner)
                if(puzzle.getNumber(i, j) == 6){
                    // kalau 6 ada di edge, langsung warnain item sekitarnya
                    if(checkEdge(i, j, size)){
                        fillAll(i, j, kromosom, true);
                    }
                    // kalau 6 bukan di edge, lihat apakah ada barisan 3 kotak putih (yang sudah pasti) di sekitarnya
                    if(checkAdjacent3Whites(i, j, kromosom)){
                        fillAll(i, j, kromosom, true);
                    }
                }
            }
        }
    }

    // heuristik 10
    private static void heuristic4Corner(Kromosom kromosom) {
        
    }

    // heuristik 11
    private static void heuristicFillCertain(Kromosom kromosom){
        MosaicPuzzle puzzle = kromosom.getPuzzle();
        int i, j, size = puzzle.getSize();
        
        int numberOfNeighbours;
        // corner <= 4, tepi <= 6, lainnya <= 9
        
        for(i=0; i < size; i++){
            for(j=0; j < size; j++){
                int curNum = puzzle.getNumber(i, j); // angka di kotak (i, j)

                if(checkCorner(i, j, size)) numberOfNeighbours = 4;
                else if(checkEdge(i, j, size)) numberOfNeighbours = 6;
                else numberOfNeighbours = 9;

                // kalo udah ada n hitam yg fixed disekitarnya, sisa kotaknya pasti putih
                if(curNum != -1 && countNeighbours(i, j, kromosom, true) == curNum) fillAll(i, j, kromosom, false);
                // atau kalo udah ada 9 - n putih yang fixed disekitarnya, sisa kotaknya pasti hitam
                if(curNum != -1 && countNeighbours(i, j, kromosom, false) == (numberOfNeighbours - curNum)) fillAll(i, j, kromosom, true);
            }
        }
    }


    // FUNGSI UTILITAS

    // fill semua di sekitar kotak (i, j) dan kotak (i, j) itu sendiri (yang belum fixed)
    private static void fillAll(int i, int j, Kromosom kromosom, Boolean black){ // Boolean black -> true : black, false : white
        int k, size = kromosom.getSize();
        int[] arahY = {-1, -1, 0, 1, 1, 1, 0, -1};
        int[] arahX = {0, 1, 1, 1, 0, -1, -1, -1};

        // isi kotak itu sendiri
        if(!kromosom.getFixedAllele(i, j)){
            kromosom.setBit(i, j, black);
            kromosom.setFixedAllele(i, j, true);
        }

        // isi kotak di sekitarnya
        for(k=0; k < 8; k++){
            int arahYcur = i + arahY[k];
            int arahXcur = j + arahX[k];
            if(arahYcur >= 0 && arahYcur < size && arahXcur >= 0 && arahXcur < size && !kromosom.getFixedAllele(arahYcur, arahXcur)){
                kromosom.setBit(arahYcur, arahXcur, black);
                kromosom.setFixedAllele(arahYcur, arahXcur, true);
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

    private static boolean checkCorner(int i, int j, int size){
        return ((i == 0 && j == 0) || (i == 0 && j == size-1) || (i == size-1 && j == 0) || (i == size-1 && j == size-1));
    }

    private static boolean checkEdge(int i, int j, int size){
        return(!checkCorner(i, j, size) && (i == 0 || i == size-1 || j == 0 || j == size-1));
    }

    private static boolean checkAdjacent3Whites(int i, int j, Kromosom kromosom){
        int[] arahY = {-1, 0, 1, 0};
        int[] arahX = {0, 1, 0, -1};
        int k, size = kromosom.getSize();
        for(k=0; k < 4; k++){
            int arahYcur = i + arahY[k];
            int arahXcur = j + arahX[k];
            // kalau dia ga melebihi constraint, dan kalau dia allele nya udah fixed, dan warnanya putih
            if(arahYcur >= 0 && arahYcur < size && arahXcur >= 0 && arahXcur < size
                && kromosom.getFixedAllele(arahYcur, arahXcur) && !kromosom.getBit(arahYcur, arahXcur)){
                // cek ke samping dari si kotak putih itu (apakah ada kotak putih di kiri dan kanannya)
                if(k%2 == 0){ // kalau k nya genap (artinya lagi cek yang arah atas/bawah, jadi cek nya horizontal)
                    boolean kiri = false, kanan = false;
                    if(arahXcur - 1 >= 0 && kromosom.getFixedAllele(arahYcur, arahXcur - 1) && !kromosom.getBit(arahYcur, arahXcur - 1))
                        kiri = true;
                    if(arahXcur + 1 < size && kromosom.getFixedAllele(arahYcur, arahXcur + 1) && !kromosom.getBit(arahYcur, arahXcur + 1))
                        kanan = true;
                    // kalau kiri dan kanannya putih, jadi ada 3 kotak putih sebaris, return true
                    return kiri && kanan; 
                } else{ // k nya ganjil (artinya lagi cek yang kiri/kanan, jadi cek nya vertikal)
                    boolean atas = false, bawah = false;
                    if(arahYcur - 1 >= 0 && kromosom.getFixedAllele(arahYcur - 1, arahXcur) && !kromosom.getBit(arahYcur - 1, arahXcur))
                        atas = true;
                    if(arahYcur + 1 < size && kromosom.getFixedAllele(arahYcur + 1, arahXcur) && !kromosom.getBit(arahYcur + 1, arahXcur))
                        bawah = true;
                    // kalau atas dan bawahnya putih, kado ada 3 kotak putih sebaris, return true
                    return atas && bawah;
                }
            }
        }
        return false;
    }
}
