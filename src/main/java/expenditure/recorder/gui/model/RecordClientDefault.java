package expenditure.recorder.gui.model;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.io.IOUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RecordClientDefault implements RecordClient {
    private static final String EXPENDITURE_RECORDER_URL = "http://localhost:8080/expenditure_recorder/expenditures";

    @Override
    public List<Record> getAllRecordsFromServer() throws IOException {
        HttpGet httpGet = new HttpGet(EXPENDITURE_RECORDER_URL);

        CloseableHttpResponse response = getHttpClientsWithAuthentication().execute(httpGet);

        String content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);

        return JsonMapper.getInstance().readValue(content, new TypeReference<List<Record>>() {
        });
    }

    @Override
    public void addRecordToServer(Record record) throws IOException {
        HttpPost httpPost = new HttpPost(EXPENDITURE_RECORDER_URL);
        StringEntity entity = new StringEntity(JsonMapper.getInstance().writeValueAsString(record));
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = getHttpClientsWithAuthentication().execute(httpPost);
    }

    private CloseableHttpClient getHttpClientsWithAuthentication() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope("localhost", 8080),
                new UsernamePasswordCredentials("testuser", "nopassword"));
        return HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
    }
}