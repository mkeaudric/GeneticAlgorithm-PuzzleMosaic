public class NgecekHeuristik {
    public static void main(String[] args) throws Exception {
        String filePeta = "buatNgecekHeuristik3.txt";
        MosaicPuzzle puzzle = InputReader.bacaPuzzle(filePeta);

        Kromosom kromosom = new Kromosom(5, puzzle);
        PenandaHeuristik.setFixedAllele(kromosom);

        kromosom.printCurrentFixedKromosomAsGrid();
    }
}
