public class InfoEntreprise {
    int nbFactures;
    int nbFacturesDupliquee;
    boolean fraudeur;

    public InfoEntreprise() {
        this.nbFactures = 1;
        this.nbFacturesDupliquee = 0;
        this.fraudeur = false;
    }

    public void addFacture() {
        this.nbFactures++;
    }

    public void addFactureDupliquee() {
        this.nbFacturesDupliquee++;
    }

    public void fraudeurAnalyse() {
        if (100 * nbFacturesDupliquee / nbFactures >= 10) {
            fraudeur = true;
        }
    }

    public boolean isFraudeur() {
        return fraudeur;
    }
}
