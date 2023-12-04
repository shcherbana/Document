package ua.edu.ucu.apps;
import java.io.IOException;
import java.sql.SQLException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TimedDocument implements Document {
    public String path;

    @Override
    public String parse() throws IOException, SQLException {
        String smartTxt = measureTime(new SmartDocument(path));
        String cachedTxt = measureTime(new CacheDocument(path));

        return cachedTxt != null ? cachedTxt : smartTxt;
    }

    private String measureTime(Document doc) throws IOException, SQLException {
        long startTime = System.currentTimeMillis();
        String result = doc.parse();
        long endTime = System.currentTimeMillis();

        logExecutionTime(doc, endTime - startTime);

        return result;
    }

    private void logExecutionTime(Document doc, long executionTime) {
        String type = (doc instanceof SmartDocument) ? "SmartDocument" : "CacheDocument";
        System.out.println("Time of " + type + ": " + executionTime / 1000.0);
    }
}
