package ua.edu.ucu.apps;
import java.io.IOException;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
public class CacheDocument implements Document {

    private final String path;
    private static final String SELECT_QUERY = "SELECT text FROM documents WHERE path = ?";
    private static final String INSERT_QUERY = "INSERT INTO documents (path, text) VALUES (?, ?)";

    @Override
    public String parse() throws SQLException, IOException {
        String text = getTextFromCache(path);

        if (text == null) {
            text = new SmartDocument(path).parse();
            saveTextToCache(text);
        }

        return text;
    }

    private String getTextFromCache(String path) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite");
             PreparedStatement selectStatement = conn.prepareStatement(SELECT_QUERY)) {
            selectStatement.setString(1, path);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                return resultSet.next() ? resultSet.getString("text") : null;
            }
        }
    }

    private void saveTextToCache(String text) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite");
             PreparedStatement insertStatement = conn.prepareStatement(INSERT_QUERY)) {
            insertStatement.setString(1, path);
            insertStatement.setString(2, text);
            insertStatement.executeUpdate();
        }
    }
}
