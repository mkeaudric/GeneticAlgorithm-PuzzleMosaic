// sumber : http://ijcs.net/ijcs/index.php/ijcs/article/view/4596/934

import java.util.ArrayList;
import java.util.List;

public class Selection {
    // roullete wheel selection

    // rank selection

    // tournament selection
    public Individu tournamentSelection(Populasi populasi, int size) {
        List<Individu> participants = new ArrayList<>();
        List<Individu> allIndividu = populasi.getIndividuList();

        // pilih peserta random dari populasi
        for (int i = 0; i < size; i++) {
            int randomIndex = RNG.rand.nextInt(allIndividu.size());
            participants.add(allIndividu.get(randomIndex));
        }

        // cari yg terbaik di antara peserta turnamen
        Individu best = participants.get(0);
        for (int i = 1; i < participants.size(); i++) {
            if (participants.get(i).getFitness() > best.getFitness()) {
                best = participants.get(i);
            }
        }

        // return copy utk crossover/mutasi
        return best.copyForOffspring();
    }

    // boltzmann selection

    // stochastic universal sampling
}
