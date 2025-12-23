// http://ijcs.net/ijcs/index.php/ijcs/article/view/4596/934
public class Crossover {
    private FitnessFunction fitnessFunction;

    public Crossover(FitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
    }

    // kita pake 2 parent, hasilnya 2 anak
    public Individu[] crossover(Individu parent1, Individu parent2, String crossoverMethod) {
        switch (crossoverMethod) {
            case "single-point":
                    return singlePointCrossover(parent1, parent2);
            case "uniform":
                    return uniformCrossover(parent1, parent2);
            case "two-point":
                    return twoPointCrossover(parent1, parent2);
            default:
                System.err.println("method crossover tidak dikenali: " + crossoverMethod + ". Menggunakan single-point crossover sebagai default.");
                return singlePointCrossover(parent1, parent2);
        }
    }
    
    private Individu[] singlePointCrossover(Individu parent1, Individu parent2) {
        int i, length = parent1.getKromosom().getlength();
        int crossoverPoint = RNG.rand.nextInt(length-1); // sumber : https://www.baeldung.com/java-generating-random-numbers-in-range
        
        Kromosom firstChild = parent1.getKromosom().copy();
        Kromosom secondChild = parent2.getKromosom().copy();

        for(i=crossoverPoint; i < length; i++){
            // ini karena kita udah setFixedAllele untuk satu populasi, kita gaperlu cek sebenernya karena pasti sama warnanya untuk yang fixed
            // tapi kita pake aja biar hemat resource komputasi (kalo udah pasti sama gaperlu nuker)
            // karena pasti sama yang fixed, saya cuma cek 1 anak aja
            if(!firstChild.getFixedAllele(i)){
                // tuker allele
                boolean temp = firstChild.getBit(i);
                firstChild.setBit(i, secondChild.getBit(i));
                secondChild.setBit(i, temp);
            }
        }

        return new Individu[] {new Individu(firstChild, fitnessFunction), new Individu(secondChild, fitnessFunction)};
    }

    private Individu[] uniformCrossover(Individu parent1, Individu parent2) {
        int i, length = parent1.getKromosom().getlength();

        Kromosom firstChild = parent1.getKromosom().copy();
        Kromosom secondChild = parent2.getKromosom().copy();

        for(i=0; i < length; i++){
            if(!firstChild.getFixedAllele(i)){
                // ada probabilitas 50% ketuker, ato engga ketuker
                if(RNG.rand.nextDouble() < 0.5){
                    // tuker allele
                    boolean temp = firstChild.getBit(i);
                    firstChild.setBit(i, secondChild.getBit(i));
                    secondChild.setBit(i, temp);
                }
            }
        }

        return new Individu[] {new Individu(firstChild, fitnessFunction), new Individu(secondChild, fitnessFunction)};
    }

    private Individu[] twoPointCrossover(Individu parent1, Individu parent2) {
        int length = parent1.getKromosom().getlength();

        Kromosom firstChild = parent1.getKromosom().copy();
        Kromosom secondChild = parent2.getKromosom().copy();

        int p1_idx = RNG.rand.nextInt(length);
        int p2_idx = RNG.rand.nextInt(length);
        int start = Math.min(p1_idx, p2_idx);
        int end = Math.max(p1_idx, p2_idx);

        for (int i = start; i <= end; i++) {
            if (!firstChild.getFixedAllele(i)) {
                boolean temp = firstChild.getBit(i);
                firstChild.setBit(i, secondChild.getBit(i));
                secondChild.setBit(i, temp);
            }
        }

        return new Individu[] {new Individu(firstChild, fitnessFunction), new Individu(secondChild, fitnessFunction)};
    }
}
