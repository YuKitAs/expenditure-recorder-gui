package expenditure.recorder.gui.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceFileReader {
    public static String read(Class clazz, String filePath) {
        String fullPath = clazz.getResource(filePath).getPath();

        try {
            return new String(Files.readAllBytes(Paths.get(fullPath)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Cannot read file from path: %s", fullPath), e);
        }
    }
}
