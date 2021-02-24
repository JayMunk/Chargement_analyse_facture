import java.sql.*;

public class jdbc {
    private Connection connection;

    public jdbc() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tpf", "test", "test");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void ajouterFraudeur(String noCommerce, int nbFacture, int nbFactureDupliquer) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "insert into fraudeur (noCommerce, nbFacture, nbFactureDupliquer) " + "values ('" + noCommerce
                + "','" + nbFacture + "','" + nbFactureDupliquer + "')";
        statement.executeUpdate(sql);
        statement.close();
    }

    public double rechercheFraudeur(String noCommerce) throws SQLException {
        int nbFacture = 0;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement
                .executeQuery("select * from fraudeur" + " where noCommerce='" + noCommerce + "'");
        if (resultSet.next()) {
            nbFacture = resultSet.getInt("nbFacture");
        }
        statement.close();
        return nbFacture;
    }

    public void afficherFraudeur() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from fraudeur");
        while (resultSet.next()) {
            int nbFacture = resultSet.getInt("nbFacture");
            int nbFactureDupliquer = resultSet.getInt("nbFactureDupliquer");
            int pourcentage = 100 * nbFactureDupliquer / nbFacture;
            System.out.println(resultSet.getString("noCommerce") + " " + pourcentage + "% de factures dupliquees");
        }
    }

    public void supprimerFraudeur(String noCommerce) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "delete from fraudeur where noCommerce='" + noCommerce + "'";
        statement.executeUpdate(sql);
        statement.close();
    }
}
