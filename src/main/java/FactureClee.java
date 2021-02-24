public class FactureClee implements Comparable {
    String date;
    String montant;

    public FactureClee(String date, String montant) {
        this.date = date;
        this.montant = montant;
    }


    @Override
    public boolean equals(Object obj) {
        FactureClee factureComparee = (FactureClee) obj;
        return date.equals(factureComparee.date) && montant.equals(factureComparee.montant);
    }

    @Override
    public int compareTo(Object o) {
        FactureClee factureComparee = (FactureClee) o;
        if (montant.equals(factureComparee.montant)) {
            return -1 * date.compareTo(factureComparee.date);
        }
        return -1 * montant.compareTo(factureComparee.montant);
    }
}
