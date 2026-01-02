public class NgecekHeuristik {
    public static void main(String[] args) throws Exception {
        String filePeta = "buatNgecekHeuristik2.txt";
        // String filePeta = "buatNgecekHeuristik3.txt";
        // String filePeta = "buatNgecekHeuristik4.txt";
        // String filePeta = "buatNgecekHeuristik5.txt";
        MosaicPuzzle puzzle = InputReader.bacaPuzzle(filePeta);

        Kromosom kromosom = new Kromosom(puzzle.getSize(), puzzle);

        // kromosom.setBit(0, 1, true);
        // kromosom.setFixedAllele(0, 1, true);

        // kromosom.setBit(3, 1, false); 
        // kromosom.setFixedAllele(3, 1, true);

        // kromosom.setBit(0, 1, true);
        // kromosom.setBit(0, 2, false);
        // kromosom.setBit(0, 3, false);
        // kromosom.setBit(3, 3, false);
        // kromosom.setBit(3, 1, false);
        // kromosom.setFixedAllele(0, 1, true);
        // kromosom.setFixedAllele(0, 2, true);
        // kromosom.setFixedAllele(0, 3, true);
        // kromosom.setFixedAllele(3, 3, true);
        // kromosom.setFixedAllele(3, 1, true);

        // kromosom.setBit(1, 1, false);
        // kromosom.setBit(2, 1, false);
        // kromosom.setBit(3, 1, false);
        // kromosom.setFixedAllele(1, 1, true);
        // kromosom.setFixedAllele(2, 1, true);
        // kromosom.setFixedAllele(3, 1, true);

        PenandaHeuristik.setFixedAllele(kromosom);

        kromosom.printCurrentFixedKromosom();
    }
}
