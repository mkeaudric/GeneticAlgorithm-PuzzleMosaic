public class NgecekHeuristik {
    public static void main(String[] args) throws Exception {
        String filePeta = "GeneticAlgorithm-PuzzleMosaic/buatNgecekHeuristik2.txt";
        MosaicPuzzle puzzle = InputReader.bacaPuzzle(filePeta);

        Kromosom kromosom = new Kromosom(20, puzzle);
        PenandaHeuristik.setFixedAllele(kromosom);

        kromosom.printCurrentFixedKromosomAsGrid();
    }
}
