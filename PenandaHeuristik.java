// heuristik ini dipakai untuk menandakan mana kotak yang udah pasti item/putih di awal generasi populasi
// jadi menandakan kotak yang sudah diketahui secara pasti, dan tidak akan terpengaruh mutasi atau crossover ke depannya

public class PenandaHeuristik {
    // atas, atas kanan, kanan, bawah kanan, dst (jarum jam)
    static int[] arahY = {-1, -1, 0, 1, 1, 1, 0, -1};
    static int[] arahX = {0, 1, 1, 1, 0, -1, -1, -1};

    public static void setFixedAllele(Kromosom kromosom){

        // heuristik 1 : cek 0 dan 9, warnain sekitarnya
        heuristic0and9(kromosom);

        // heuristik 2 : angka bersebelahan dengan selisih 3, 2, 1, atau sama
        // jika selisih 3, maka angka yang kecil bersebelahan dengan 3 kotak putih, angka yang besar bersebelahan dengan 3 kotak hitam 
        
        // jika selisih 2, maka angka yang kecil bersebelahan dengan 3 kotak putih, angka yang besar bersebelahan dengan 2 kotak hitam 
        // (tapi belum pasti posisi kotak hitamnya, jadi serahkan ke genetic algorithm saja)



        // heuristik 3 : cek angka 6 di tepi (dan juga di tepi 3 kotak yang udah pasti putih)
        heuristic6edge(kromosom);
        
        // heuristik 4 : cek angka 4 di corner (dan juga di corner 2 kotak yang udah pasti putih)
        heuristic4corner(kromosom);

        // heuristik terakhir : cek angka 1, 

    }

    public static void heuristic0and9(Kromosom kromosom){
        MosaicPuzzle puzzle = kromosom.getPuzzle();
        int i, j, k, size=puzzle.getSize();
        for(i=0; i < size; i++){
            for(j=0; j < size; j++){
                if(puzzle.getNumber(i, j) == 9 && !kromosom.getFixedAllele(i, j)){
                    // ngeset semua di sekitar angka 9 jadi item, dan gaboleh diganti lagi ke depannya saat crossover & mutasi
                    kromosom.setBit(i, j, true);
                    // mark the clue cell itself as fixed
                    kromosom.setFixedAllele(i, j, true);
                    for(k=0; k < 8; k++){
                        int arahYcur = arahY[k];
                        int arahXcur = arahX[k];
                        if(i + arahYcur >= 0 && i + arahYcur < size && j + arahXcur >= 0 && j + arahXcur < size){
                            kromosom.setBit(i + arahYcur, j + arahXcur, true);
                            kromosom.setFixedAllele(i + arahYcur, j + arahXcur, true);
                        }
                    }
                } else if (puzzle.getNumber(i, j) == 0 && !kromosom.getFixedAllele(i, j)){
                    // ngeset semua di sekitar angka 0 jadi putih (bukan item), dan gaboleh diganti lagi ke depannya saat crossover & mutasi
                    for(k=0; k < 8; k++){
                        int arahYcur = arahY[k];
                        int arahXcur = arahX[k];
                        if(i + arahYcur >= 0 && i + arahYcur < size && j + arahXcur >= 0 && j + arahXcur < size){
                            kromosom.setBit(i + arahYcur, j + arahXcur, false);
                            // mark neighbor as fixed (white)
                            kromosom.setFixedAllele(i + arahYcur, j + arahXcur, true);
                        }
                    }
                }
            }
        }
    }

    private static void heuristic6edge(Kromosom kromosom) {
        // sebenernya ga cuma tepi aja, tapi bisa kalau ada 3 kotak yang udah pasti putih (misal sekitar angka 0)
        // jadi gw ceknya pake loop seluruh papan yang bukan corner (6 gabisa di corner)
        MosaicPuzzle puzzle = kromosom.getPuzzle();
        int i, j, size=puzzle.getSize();
        for(i=0; i < size; i++){
            for(j=0; j < size; j++){

            }
        }
    }

    private static void heuristic4corner(Kromosom kromosom) {
        
    }

}
