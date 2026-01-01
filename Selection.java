import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Selection {

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

    // ROULETTE WHEEL SELECTION https://www.woodruff.dev/day-6-roulette-tournaments-and-elites-exploring-selection-strategies/
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

    // RANK SELECTION  linear rank selection     https://stackoverflow.com/questions/13659815/ranking-selection-in-genetic-algorithm-code
    private Individu rankSelection(Populasi populasi) {
        List<Individu> individuList = new ArrayList<>(populasi.getIndividuList());
        
        // Urutkan fitness dari terendah ke tertinggi 
        Collections.sort(individuList, (a, b) -> Double.compare(a.getFitness(), b.getFitness()));

        int n = individuList.size();
        int totalRankSum = n * (n + 1) / 2; // Rumus deret: 1+2+3...+N

        int spin = RNG.rand.nextInt(totalRankSum) + 1; // 1,2,3,etc
        int cumulativeRank = 0;

        for (int i = 0; i < n; i++) {
            int rank = i + 1; // Peringkat 1 untuk index 0, dst.
            cumulativeRank += rank;
            if (cumulativeRank >= spin) {
                return individuList.get(i).copyForOffspring();
            }
        }
        return individuList.get(n - 1).copyForOffspring();
    }

    // TOURNAMENT SELECTION 
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

    // RANDOM SELECTION 
    private Individu randomSelection(Populasi populasi) {
        List<Individu> individuList = populasi.getIndividuList();
        return individuList.get(RNG.rand.nextInt(individuList.size())).copyForOffspring();
    }
}