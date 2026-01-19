import java.util.List;
import java.util.ArrayList;

/**
 * Kelas Selection, bertanggung jawab untuk memilih individu dari populasi yang akan digunakan sebagai parent untuk proses reproduksi.
 * Mendukung berbagai metode seleksi standar dalam Algoritma Genetik.
 * Sumber Utama: 
 * @link https://www.tutorialspoint.com/genetic_algorithms/genetic_algorithms_parent_selection.htm
 * @author Kelompok AI Mosaic
 * @version 1.0
 */

public class Selection {

    /**
     * Metode utama untuk memilih individu berdasarkan parameter yang ditentukan.
     * @param populasi Objek populasi yang berisi daftar individu saat ini.
     * @param param Objek parameter GA yang menentukan metode seleksi yang digunakan.
     * @return Individu hasil copy
     */
    public Individu select(Populasi populasi, GAParamater param) {
        String method = param.getSelectionMethod().toLowerCase();
        
        switch (method) {
            case "roulette":
                return rouletteWheelSelection(populasi);
            case "tournament":
                return tournamentSelection(populasi, param.getSelectionSize());
            case "rank":
                return rankSelection(populasi);
            case "random":
                return randomSelection(populasi);
            default:
                return randomSelection(populasi);
        }
    }

    // ROULETTE WHEEL SELECTION 
    /**
     * Individu dengan fitness lebih tinggi memiliki peluang lebih besar untuk terpilih
     * @link https://www.woodruff.dev/day-6-roulette-tournaments-and-elites-exploring-selection-strategies/
     * @param populasi Daftar populasi yang tersedia.
     * @return Individu terpilih.
     */
    private Individu rouletteWheelSelection(Populasi populasi) {
        List<Individu> individuList = populasi.getIndividuList();
        double totalFitness = 0;
        for (Individu ind : individuList) totalFitness += ind.getFitness();

        double spin = RNG.rand.nextDouble() * totalFitness;
        double cumulative  = 0;
        for (Individu ind : individuList) {
            cumulative  += ind.getFitness();
            if (cumulative >= spin) return ind.copyForOffspring();
        }
        return individuList.get(individuList.size() - 1).copyForOffspring();
    }

    // RANK SELECTION  linear rank selection
    /**
     * Mengurangi bias pada individu yang terlalu dominan dengan cara memberikan peluang. 
     * berdasarkan urutan (rank), bukan nilai fitness absolut.
     * @link https://stackoverflow.com/questions/13659815/ranking-selection-in-genetic-algorithm-code
     * @param populasi Daftar populasi yang tersedia.
     * @return Individu terpilih.
     */    
    private Individu rankSelection(Populasi populasi) {
        List<Individu> individuList = new ArrayList<>(populasi.getIndividuList());

        int n = individuList.size();
        int totalRankSum = n * (n + 1) / 2; // Rumus deret: 1+2+3...+N

        int spin = RNG.rand.nextInt(totalRankSum) + 1; // 1,2,3,etc
        int cumulativeRank = 0;

        for (int i = 0; i < n; i++) {
            int rank = n - i; // Rank tertinggi untuk individu terbaik
            cumulativeRank += rank;
            if (cumulativeRank >= spin) {
                return individuList.get(i).copyForOffspring();
            }
        }
        return individuList.get(0).copyForOffspring();
    }

    // TOURNAMENT SELECTION 
    /**
     * Memilih beberapa individu secara acak dan mengambil yang terbaik di antaranya.
     * @param populasi Daftar populasi yang tersedia.
     * @param tournamentSize Jumlah kontestan terpilih dalam satu turnamen.
     * @return Individu dengan fitnesss terbaik dari hasil turnamen.
     */
    private Individu tournamentSelection(Populasi populasi, int tournamentSize) { // (Tournamen Size pilih)
        List<Individu> individuList = populasi.getIndividuList();
        Individu best = null;

        for (int i = 0; i < tournamentSize; i++) {
            Individu selected = individuList.get(RNG.rand.nextInt(individuList.size()));
            // Simpan nilai fitness terbaik
            if (best == null || selected.getFitness() > best.getFitness()) {
                best = selected;
            }
        }
        return best.copyForOffspring();
    }

    //RANDOM SELECTION
    /**
     * Memilih individu secara acak tanpa memperhatikan nilai fitness.
     * @param populasi Daftar populasi yang tersedia.
     * @return Individu terpilih secara acak.
     */
    private Individu randomSelection(Populasi populasi) {
        List<Individu> individuList = populasi.getIndividuList();
        return individuList.get(RNG.rand.nextInt(individuList.size())).copyForOffspring();
    }
}