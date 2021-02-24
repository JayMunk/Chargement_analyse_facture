import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class ChargerFichier implements Runnable {
    static TreeMap<FactureClee, List<String>> factureTreeMap = new TreeMap<>();
    private boolean running;
    private Thread thread;
    static int nbFactureCharger = 0;

    public void ChargerFichier() {
    }

    void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    void attendre() throws InterruptedException {
        thread.join();
    }

    void interrompre() throws InterruptedException {
        thread.interrupt();
        thread.join();
    }

    void stop() {
        running = false;
    }

    @Override
    public void run() {
        readFromCSV("factures.csv");
        stop();

    }

    private static void readFromCSV(String fileName) {
        Path pathToFile = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                String noEntreprise = attributes[0];
                String date = attributes[1];
                String montant = attributes[2];
                FactureClee factureClee = new FactureClee(date, montant);

                ajouteFacture(noEntreprise, factureClee);

                line = br.readLine();
                nbFactureCharger++;

            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    static void ajouteFacture(String noEntreprise, FactureClee factureClee) {
        if (!factureTreeMap.containsKey(factureClee)) {
            List<String> myListe = new LinkedList<>();
            myListe.add(noEntreprise);
            factureTreeMap.put(factureClee, myListe);
        } else {
            List<String> myListe = factureTreeMap.get(factureClee);
            myListe.add(noEntreprise);
            factureTreeMap.put(factureClee, myListe);
        }
    }

    public synchronized boolean getRunning() {
        return running;
    }

    public synchronized int getNbFactureCharger() {
        return nbFactureCharger;
    }
}
