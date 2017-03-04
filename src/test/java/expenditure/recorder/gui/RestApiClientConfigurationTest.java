package expenditure.recorder.gui;

import org.junit.Test;

import java.util.Properties;

import expenditure.recorder.gui.view.configuration.RestApiClientConfiguration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RestApiClientConfigurationTest {
    @Test
    public void convertFromProperties() throws Exception {
        Properties prop = new Properties();

        assertThatThrownBy(() -> RestApiClientConfiguration.from(prop)).isInstanceOf(NumberFormatException.class);

        prop.setProperty("port", "8080");

        assertThatThrownBy(() -> RestApiClientConfiguration.from(prop)).isInstanceOf(NullPointerException.class);
    }
}