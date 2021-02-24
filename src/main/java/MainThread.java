import java.sql.SQLException;
import java.util.Scanner;

public class MainThread {
    static jdbc bdFraudeur = new jdbc();

    public static void main(String[] args) throws SQLException, InterruptedException {
        String value = "";
        ChargerFichier loading = new ChargerFichier();
        Analyse analyse = new Analyse();

        System.out.println("c: charger le fichier factures.csv");
        System.out.println("a: analyser les factures");
        System.out.println("f: afficher les fraudeurs");
        System.out.println("s: afficher le statut");
        System.out.println("i: interrompre");
        System.out.println("q: terminer et quitter");


        while (!value.equals("q")) {
            System.out.print("Entrez votre choix (c,a,f,s,i ou q) :");
            Scanner userInput = new Scanner(System.in);
            value = userInput.nextLine();

            switch (value) {
                case "c":
                    if (analyse.getRunning()) {
                        System.out.println("Patientez, une opération est déjà en cours : Analyse");
                    } else if (loading.getRunning()) {
                        System.out.println("Patientez, une opération est déjà en cours : Chargement");
                    } else {
                        loading.start();
                    }
                    break;
                case "a":
                    if (loading.getRunning()) {
                        System.out.println("Patientez, une opération est déjà en cours : Chargement");
                    } else if (analyse.getRunning()) {
                        System.out.println("Patientez, une opération est déjà en cours : Analyse");
                    } else {
                        analyse.start();
                    }
                    break;
                case "f":
                    if (loading.getRunning()) {
                        System.out.println("Patientez, une opération est déjà en cours : Chargement");
                    } else if (analyse.getRunning()) {
                        System.out.println("Patientez, une opération est déjà en cours : Analyse");
                    } else {
                        System.out.println("-----------------");
                        System.out.println("Fraudeur");
                        System.out.println("-----------------");
                        bdFraudeur.afficherFraudeur();
                        System.out.println("-----------------");
                    }
                    break;
                case "s":
                    System.out.println("-----------------");
                    System.out.println("Status");
                    System.out.println("-----------------");
                    System.out.print("Etat: ");
                    if (!loading.getRunning() && !analyse.getRunning()) {
                        System.out.println("Pret");
                    } else if (loading.getRunning()) {
                        System.out.println("Chargement");
                    } else if (analyse.getRunning()) {
                        System.out.println("Analyse");
                    }
                    System.out.println("Factures chargees: " + loading.getNbFactureCharger());
                    System.out.println("Fraudeurs trouves: " + analyse.getNbFraudeur());
                    break;
                case "i":
                    //arrêter le chargement ou l’analyse sans quitter l’application
                    if (loading.getRunning()) {
                        loading.interrompre();
                        loading.attendre();
                    } else if (analyse.getRunning()) {
                        analyse.interrompre();
                        analyse.attendre();
                    }
                    System.out.println("Opération arrêté");
                    break;
                case "q":
                    //compléter le traitement courant et quitter.
                    loading.stop();
                    loading.attendre();
                    analyse.stop();
                    analyse.attendre();
                    System.out.println("Au revoir");
                    break;
                default:
                    System.out.println("Commande invalide");
            }
        }
    }

}
