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
                withBody(ResourceFileReader.read(RecordClientDefaultTest.class, "ExpenditureRecorderServerResponse.json"))));

        assertThat(recordClient.getAllRecordsFromServer()).hasSize(2);

        assertThat(recordClient.getAllRecordsFromServer().get(0).getDate()).isEqualTo(Instant.ofEpochMilli(1470639600));
        assertThat(recordClient.getAllRecordsFromServer().get(0).getAmountInCent()).isEqualTo(200);
        assertThat(recordClient.getAllRecordsFromServer().get(0).getItem()).isEqualTo("Ice Cream");

        assertThat(recordClient.getAllRecordsFromServer().get(1).getDate()).isEqualTo(Instant.ofEpochMilli(1475798400));
        assertThat(recordClient.getAllRecordsFromServer().get(1).getAmountInCent()).isEqualTo(600);
        assertThat(recordClient.getAllRecordsFromServer().get(1).getItem()).isEqualTo("Big Ice Cream");
    }
}