// sumber : http://ijcs.net/ijcs/index.php/ijcs/article/view/4596/934

import java.util.ArrayList;
import java.util.List;

public class Selection {
    // roullete wheel selection

    // rank selection

    // tournament selection
    public Individu tournamentSelection(Populasi populasi, int tournamentSize) {
        List<Individu> tournamentParticipants = new ArrayList<>();
        List<Individu> allIndividu = populasi.getIndividuList();

        // Pilih peserta turnamen secara acak dari populasi
        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = RNG.rand.nextInt(allIndividu.size());
            tournamentParticipants.add(allIndividu.get(randomIndex));
        }

        // Cari yang terbaik di antara peserta turnamen
        Individu best = tournamentParticipants.get(0);
        for (int i = 1; i < tournamentParticipants.size(); i++) {
            if (tournamentParticipants.get(i).getFitness() > best.getFitness()) {
                best = tournamentParticipants.get(i);
            }
        }

        // Kembalikan salinan untuk diolah (crossover/mutasi)
        return best.copyForOffspring();
    }

    // boltzmann selection

    // stochastic universal sampling
}
