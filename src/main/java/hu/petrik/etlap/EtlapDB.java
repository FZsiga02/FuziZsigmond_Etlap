package hu.petrik.etlap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtlapDB {
    Connection conn;

    public static String DB_DRIVER = "mysql";
    public static String DB_HOST = "localhost";
    public static String DB_PORT = "3306";
    public static String DB_DBNAME = "etlapdb";
    public static String DB_USER = "root";
    public static String DB_PASS = "";

    public EtlapDB() throws SQLException {
        String url = String.format("jdbc:%s://%s:%s/%s", DB_DRIVER, DB_HOST, DB_PORT, DB_DBNAME);
        conn = DriverManager.getConnection(url, DB_USER, DB_PASS);
    }

    public List<Etlap> etlapBeolvas() throws SQLException {
        List<Etlap> etelek = new ArrayList<>();
        String sql = "SELECT * FROM etlap";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(sql);
        while (result.next()) {
            int id = result.getInt("id");
            String nev = result.getString("nev");
            String leiras = result.getString("leiras");
            int ar = result.getInt("ar");
            String kategoria = result.getString("kategoria");
            Etlap etel = new Etlap(id, nev, leiras, ar, kategoria);
            etelek.add(etel);
        }
        return etelek;
    }
}
