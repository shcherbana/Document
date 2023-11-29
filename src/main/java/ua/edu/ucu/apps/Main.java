package ua.edu.ucu.apps;

import java.io.IOException;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {
        try {
            String res = parsering(new CacheDocument("gs://cv-examples/wiki.png"));
            System.out.println(res);

            parsering(new TimedDocument("gs://cv-examples/wiki.png"));
        }
        catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String parsering(Document document) throws SQLException, IOException {
        return document.parse();
    }
}