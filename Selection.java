import java.util.List;
import java.util.ArrayList;

public class Selection {

    public Individu select(Populasi populasi, GAParamater param) {
        String method = param.getSelectionMethod().toLowerCase();
        
        switch (method) {
            case "roulette":
                return rouletteWheelSelection(populasi);
            case "tournament":
                return tournamentSelection(populasi, param.getTournamentSize());
            case "rank":
                return null;
            case "random":
                return randomSelection(populasi);
            default:
                return tournamentSelection(populasi, param.getTournamentSize());
        }
    }

    // ROULETTE WHEEL SELECTION https://stackoverflow.com/questions/177271/roulette-selection-in-genetic-algorithms
    private Individu rouletteWheelSelection(Populasi populasi) {
        List<Individu> individuList = populasi.getIndividuList();
        double totalFitness = 0;
        for (Individu ind : individuList) totalFitness += ind.getFitness();

        double randomPoint = RNG.rand.nextDouble() * totalFitness;
        double runningSum = 0;
        for (Individu ind : individuList) {
            runningSum += ind.getFitness();
            if (runningSum >= randomPoint) return ind.copyForOffspring();
        }
        return individuList.get(individuList.size() - 1).copyForOffspring();
    }

    // TOURNAMENT SELECTION 
    private Individu tournamentSelection(Populasi populasi, int tournamentSize) { // (Tournamen Size pilih)
        List<Individu> populasiList = populasi.getIndividuList();
        Individu best = null;

        for (int i = 0; i < tournamentSize; i++) {
            Individu selected = populasiList.get(RNG.rand.nextInt(populasiList.size()));
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