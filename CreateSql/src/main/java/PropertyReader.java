import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    private static final String SRC_RESOURCE_PATH = "/src/main/resources/application.properties";

    public static Properties getProperties(String dir) throws IOException {
        final Properties prop = new Properties();
        final InputStream inputStream = new FileInputStream(dir + SRC_RESOURCE_PATH);
        prop.load(inputStream);
        return prop;
    }
}
