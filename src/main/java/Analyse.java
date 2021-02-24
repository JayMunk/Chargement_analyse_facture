import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Analyse implements Runnable {
    static TreeMap<String, InfoEntreprise> entrepriseTreeMap = new TreeMap<>();
    private boolean running;
    private Thread thread;

    public void Analyse() {

    }

    void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    void interrompre() throws InterruptedException {
        thread.interrupt();
        thread.join();
    }

    void attendre() throws InterruptedException {
        thread.join();
    }

    void stop() {
        running = false;
    }

    @Override
    public void run() {
        for (Map.Entry<FactureClee, List<String>> entry : ChargerFichier.factureTreeMap.entrySet()) {
            List<String> value = entry.getValue();
            for (int i = 0; i < value.size(); i++) {
                if (!entrepriseTreeMap.containsKey(value.get(i))) {
                    InfoEntreprise infoEntreprise = new InfoEntreprise();
                    entrepriseTreeMap.put(value.get(i), infoEntreprise);
                    if (value.size() > 1) {
                        entrepriseTreeMap.get(value.get(i)).addFactureDupliquee();
                    }
                } else {
                    entrepriseTreeMap.get(value.get(i)).addFacture();
                    if (value.size() > 1) {
                        entrepriseTreeMap.get(value.get(i)).addFactureDupliquee();
                    }
                }
            }
        }
        for (Map.Entry<String, InfoEntreprise> entry : entrepriseTreeMap.entrySet()) {
            String key = entry.getKey();
            InfoEntreprise value = entry.getValue();
            entrepriseTreeMap.get(key).fraudeurAnalyse();
            if (entrepriseTreeMap.get(key).isFraudeur()) {
                try {
                    MainThread.bdFraudeur.ajouterFraudeur(key, value.nbFactures, value.nbFacturesDupliquee);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        stop();

    }

    public synchronized int getNbFraudeur() {
        int nbFraudeur = 0;
        for (Map.Entry<String, InfoEntreprise> entry : entrepriseTreeMap.entrySet()) {
            String key = entry.getKey();
            if (entrepriseTreeMap.get(key).isFraudeur()) {
                nbFraudeur++;
            }
        }
        return nbFraudeur;
    }

    public synchronized boolean getRunning() {
        return running;
    }
}
