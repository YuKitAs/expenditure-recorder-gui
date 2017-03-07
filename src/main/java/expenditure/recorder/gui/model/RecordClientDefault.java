package expenditure.recorder.gui.model;

import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.commons.io.IOUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import expenditure.recorder.gui.view.configuration.RestApiClientConfiguration;

public class RecordClientDefault implements RecordClient {
    private static final String EXPENDITURE_RECORDER_URL_PATH = "/expenditure_recorder/expenditures";
    private final RestApiClientConfiguration restApiClientConfiguration;

    public RecordClientDefault(RestApiClientConfiguration restApiClientConfiguration) {
        this.restApiClientConfiguration = restApiClientConfiguration;
    }

    @Override
    public List<Record> getAllRecordsFromServer() throws IOException {
        HttpGet httpGet = new HttpGet(buildUrlWithPath(EXPENDITURE_RECORDER_URL_PATH));
        CloseableHttpResponse response = getHttpClientsWithAuthentication().execute(httpGet);
        String content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);

        return JsonMapper.getInstance().readValue(content, new TypeReference<List<Record>>() {
        });
    }

    @Override
    public Record addRecordToServer(Record record) throws IOException {
        HttpPost httpPost = new HttpPost(buildUrlWithPath(EXPENDITURE_RECORDER_URL_PATH));
        StringEntity entity = new StringEntity(JsonMapper.getInstance().writeValueAsString(record));
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = getHttpClientsWithAuthentication().execute(httpPost);
        String content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);

        return JsonMapper.getInstance().readValue(content, Record.class);
    }

    @Override
    public void deleteRecordOnServer(String id) throws IOException {
        HttpDelete httpDelete = new HttpDelete(buildUrlWithPath(EXPENDITURE_RECORDER_URL_PATH + "/" + id));

        getHttpClientsWithAuthentication().execute(httpDelete);
    }

    private CloseableHttpClient getHttpClientsWithAuthentication() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(restApiClientConfiguration.getHost(), restApiClientConfiguration.getPort()),
                new UsernamePasswordCredentials(restApiClientConfiguration.getUsername(), restApiClientConfiguration.getPassword()));

        return HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
    }

    private String buildUrlWithPath(String urlPath) {
        return String.format("http://%s:%d%s", restApiClientConfiguration.getHost(), restApiClientConfiguration.getPort(), urlPath);
    }
}