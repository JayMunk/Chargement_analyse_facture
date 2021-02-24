import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class appMainTests {
    private final Facture FACTURE1 = new Facture("1", "2019-12-13", "843.02");
    private final Facture FACTURE2 = new Facture("2", "2019-12-13", "843.02");
    private final Facture FACTURE3 = new Facture("3", "2019-10-9", "2");
    private final String NUMENTREPRISE = "1234";
    private final int NBFACTURE = 10;
    private final int NBFACTUREDUPLIQUER = 5;

    // 1
    @Test
    void chargementFacturesDupliquees() {
        ChargerFichier.factureTreeMap.clear();
        FactureClee facture1Clee = new FactureClee(FACTURE1.getDate(), FACTURE1.getMontant());
        FactureClee facture2Clee = new FactureClee(FACTURE2.getDate(), FACTURE2.getMontant());
        ChargerFichier.ajouteFacture(FACTURE1.getNoEntreprise(), facture1Clee);
        ChargerFichier.ajouteFacture(FACTURE2.getNoEntreprise(), facture2Clee);

        List<String> expected = new LinkedList<>();
        expected.add(FACTURE1.getNoEntreprise());
        expected.add(FACTURE2.getNoEntreprise());

        assertEquals(expected, ChargerFichier.factureTreeMap.get(facture1Clee));
        assertEquals(expected, ChargerFichier.factureTreeMap.get(facture2Clee));

    }

    // 2
    @Test
    void chargementFacturesDifferentes() {
        ChargerFichier.factureTreeMap.clear();
        FactureClee facture1Clee = new FactureClee(FACTURE1.getDate(), FACTURE1.getMontant());
        FactureClee facture3Clee = new FactureClee(FACTURE3.getDate(), FACTURE3.getMontant());
        ChargerFichier.ajouteFacture(FACTURE1.getNoEntreprise(), facture1Clee);
        ChargerFichier.ajouteFacture(FACTURE3.getNoEntreprise(), facture3Clee);

        List<String> expected = new LinkedList<>();
        expected.add(FACTURE1.getNoEntreprise());
        assertEquals(expected, ChargerFichier.factureTreeMap.get(facture1Clee));

        expected.clear();

        expected.add(FACTURE3.getNoEntreprise());
        assertEquals(expected, ChargerFichier.factureTreeMap.get(facture3Clee));
    }

    // 3
    @Test
    void analyseFacturesDupliquees() throws SQLException {
        FactureClee facture1Clee = new FactureClee(FACTURE1.getDate(), FACTURE1.getMontant());
        FactureClee facture2Clee = new FactureClee(FACTURE2.getDate(), FACTURE2.getMontant());
        ChargerFichier.ajouteFacture(FACTURE1.getNoEntreprise(), facture1Clee);
        ChargerFichier.ajouteFacture(FACTURE2.getNoEntreprise(), facture2Clee);
        Analyse analyse = new Analyse();
        analyse.run();

        assertEquals(2, analyse.getNbFraudeur());

        jdbc crud = new jdbc();
        crud.supprimerFraudeur(FACTURE1.getNoEntreprise());
        crud.supprimerFraudeur(FACTURE2.getNoEntreprise());
    }

    // 4
    @Test
    void analyseFacturesDifferentes() throws SQLException {
        FactureClee facture1Clee = new FactureClee(FACTURE1.getDate(), FACTURE1.getMontant());
        FactureClee facture3Clee = new FactureClee(FACTURE3.getDate(), FACTURE3.getMontant());
        ChargerFichier.ajouteFacture(FACTURE1.getNoEntreprise(), facture1Clee);
        ChargerFichier.ajouteFacture(FACTURE3.getNoEntreprise(), facture3Clee);
        Analyse analyse = new Analyse();
        analyse.run();

        assertEquals(0, analyse.getNbFraudeur());

        jdbc crud = new jdbc();
        crud.supprimerFraudeur(FACTURE1.getNoEntreprise());
        crud.supprimerFraudeur(FACTURE3.getNoEntreprise());

    }

    // 5
    @Test
    void ajouterFraudeur() throws SQLException {
        jdbc crud = new jdbc();
        crud.ajouterFraudeur(NUMENTREPRISE, NBFACTURE, NBFACTUREDUPLIQUER);

        assertEquals(NBFACTURE, crud.rechercheFraudeur(NUMENTREPRISE));

        crud.supprimerFraudeur(NUMENTREPRISE);
    }

    // 6
    @Test
    void supprimerFraudeurParNum() throws SQLException {
        jdbc crud = new jdbc();
        crud.ajouterFraudeur(NUMENTREPRISE, NBFACTURE, NBFACTUREDUPLIQUER);
        crud.supprimerFraudeur(NUMENTREPRISE);

        assertEquals(0, crud.rechercheFraudeur(NUMENTREPRISE));
    }

    // 7
    @Test
    void rechercheFraudeur() throws SQLException {
        jdbc crud = new jdbc();
        crud.ajouterFraudeur(NUMENTREPRISE, NBFACTURE, NBFACTUREDUPLIQUER);

        assertEquals(NBFACTURE, crud.rechercheFraudeur(NUMENTREPRISE));

        crud.supprimerFraudeur(NUMENTREPRISE);
    }

    // 8
    //@Test
    void afficherFraudeur() throws SQLException {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        final PrintStream originalErr = System.err;

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        jdbc crud = new jdbc();
        crud.ajouterFraudeur(FACTURE1.getNoEntreprise(), 3, 2);
        crud.ajouterFraudeur(FACTURE2.getNoEntreprise(), 5, 2);
        crud.afficherFraudeur();

        String expected = "1 66% de factures dupliquees" + "\n" + "2 40% de factures dupliquees" + "\n";

        assertEquals(expected, outContent.toString());

        crud.supprimerFraudeur(FACTURE1.getNoEntreprise());
        crud.supprimerFraudeur(FACTURE2.getNoEntreprise());
        System.setOut(originalOut);
        System.setErr(originalErr);

    }
}