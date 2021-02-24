public class Facture {
    private final String noEntreprise;
    private final String date;
    private final String montant;

    public Facture(String noEntreprise, String date, String montant) {
        this.noEntreprise = noEntreprise;
        this.date = date;
        this.montant = montant;
    }

    public String getNoEntreprise() {
        return this.noEntreprise;
    }

    public String getDate() {
        return this.date;
    }

    public String getMontant() {
        return this.montant;
    }
}