package expenditure.recorder.gui.view.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class ConfigurationFileReader {
    public static Properties getFile(Path path) {
        try {
            FileInputStream fileInputStream = new FileInputStream(path.toFile());
            Properties properties = new Properties();
            properties.load(fileInputStream);

            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
