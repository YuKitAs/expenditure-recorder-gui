package expenditure.recorder.gui.view.configuration;

import java.util.Properties;

public class ExpenditureRecorderGuiConfiguration {
    private final RestApiClientConfiguration recordClientConfiguration;

    private ExpenditureRecorderGuiConfiguration(RestApiClientConfiguration recordClientConfiguration) {
        this.recordClientConfiguration = recordClientConfiguration;
    }

    public RestApiClientConfiguration getRecordClientConfiguration() {
        return recordClientConfiguration;
    }

    public static ExpenditureRecorderGuiConfiguration from(Properties properties) {
        return new ExpenditureRecorderGuiConfiguration(RestApiClientConfiguration.from(properties));
    }
}
