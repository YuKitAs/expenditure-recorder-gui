package expenditure.recorder.gui.model;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RecordClientDefault implements RecordClient {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    @Override
    public List<Record> getAllRecordsFromServer() throws IOException {
        HttpResponse response = requestServerForRecords();
        return Arrays.asList(response.parseAs(Record[].class));
    }

    @Override
    public void addRecordToServer(Record record) throws IOException {
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(request -> request.setHeaders(getRequestHeaders()));
        HttpContent content = new JsonHttpContent(JSON_FACTORY, record);
        HttpRequest request = requestFactory.buildPostRequest(ExpenditureRecordUrl.expenditures(), content);

        request.execute();
    }

    private HttpResponse requestServerForRecords() throws IOException {
        HttpRequestFactory requestFactory =
                HTTP_TRANSPORT.createRequestFactory(request -> {
                    request.setHeaders(getRequestHeaders());
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                });

        HttpRequest request = requestFactory.buildGetRequest(ExpenditureRecordUrl.expenditures());
        return request.execute();
    }

    private static class ExpenditureRecordUrl extends GenericUrl {
        private ExpenditureRecordUrl(String encodedUrl) {
            super(encodedUrl);
        }

        private static ExpenditureRecordUrl expenditures() {
            return new ExpenditureRecordUrl("http://localhost:8080/expenditure_recorder/expenditures");
        }

    }

    private HttpHeaders getRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuthentication("testuser", "nopassword");

        return headers;
    }
}