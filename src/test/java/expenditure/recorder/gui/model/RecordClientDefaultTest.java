package expenditure.recorder.gui.model;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

public class RecordClientDefaultTest {
    private final RecordClientDefault recordClient = new RecordClientDefault();
    @Rule
    public WireMockRule wm = new WireMockRule(options().port(8080));

    @Test
    public void getAllRecordsFromServer_WithCorrectRecords() throws IOException {
        stubFor(get(urlEqualTo("/expenditure_recorder/expenditures")).willReturn(aResponse().
                withBody(
                        "[{\"id\":\"00000000-0000-0000-0000-000000000000\",\"date\":1488233591,\"amountInCent\":100,\"item\":\"Hoho\"}]")));

        assertThat(recordClient.getAllRecordsFromServer()).hasSize(1);
        assertThat(recordClient.getAllRecordsFromServer().get(0).getDate()).isEqualTo(Instant.ofEpochMilli(1488233591));
        assertThat(recordClient.getAllRecordsFromServer().get(0).getAmountInCent()).isEqualTo(100);
        assertThat(recordClient.getAllRecordsFromServer().get(0).getItem()).isEqualTo("Hoho");
    }
}