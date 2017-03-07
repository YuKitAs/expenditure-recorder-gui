package expenditure.recorder.gui.model;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;

import expenditure.recorder.gui.TestConfigurationReader;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

public class RecordClientDefaultTest {
    private RecordClientDefault recordClient;

    @Before
    public void setUp() throws URISyntaxException {
        recordClient = new RecordClientDefault(TestConfigurationReader.getConfiguration().getRecordClientConfiguration());
    }

    @Rule
    public WireMockRule wm = new WireMockRule(options().port(9000));

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

    @Test
    public void addRecordToServer() throws IOException {
        stubFor(post(urlPathEqualTo("/expenditure_recorder/expenditures")).withRequestBody(
                equalToJson(ResourceFileReader.read(RecordClientDefaultTest.class, "NewExpenditureRecord.json")))
                .willReturn(
                        aResponse().withBody(ResourceFileReader.read(RecordClientDefaultTest.class, "NewExpenditureRecordWithId.json"))));

        Record record = new Record(null, "Huge Ice Cream", 1000, Instant.ofEpochMilli(1488240000));
        recordClient.addRecordToServer(record);
    }

    @Test
    public void deleteRecordOnServer() throws Exception {
        stubFor(delete(urlPathEqualTo("/expenditure_recorder/expenditures/00000000-0000-0000-0000-000000000000")).willReturn(
                aResponse().withStatus(200)));

        recordClient.deleteRecordOnServer("00000000-0000-0000-0000-000000000000");
    }
}