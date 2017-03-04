package expenditure.recorder.gui;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import expenditure.recorder.gui.view.configuration.ConfigurationFileReader;
import expenditure.recorder.gui.view.configuration.ExpenditureRecorderGuiConfiguration;

public class TestConfigurationReader {
    public static ExpenditureRecorderGuiConfiguration getConfiguration() throws URISyntaxException {
        return ExpenditureRecorderGuiConfiguration.from(ConfigurationFileReader.getFile(
                Paths.get(ExpenditureRecorderGuiConfiguration.class.getResource("config.properties").toURI())));
    }
}
