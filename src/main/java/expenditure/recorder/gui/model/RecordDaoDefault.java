package expenditure.recorder.gui.model;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RecordDaoDefault implements RecordDao {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    @Override
    public List<Record> getAllRecordsFromServer() throws IOException {
        HttpResponse response = requestServerForRecords();
        return Arrays.asList(response.parseAs(Record[].class));
    }

    private HttpResponse requestServerForRecords() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuthentication("testuser", "nopassword");

        HttpRequestFactory requestFactory =
                HTTP_TRANSPORT.createRequestFactory(request -> {
                    request.setHeaders(headers);
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                });

        ExpenditureRecordUrl url = ExpenditureRecordUrl.listRecords();
        HttpRequest request = requestFactory.buildGetRequest(url);
        return request.execute();
    }

    private static class ExpenditureRecordUrl extends GenericUrl {
        private ExpenditureRecordUrl(String encodedUrl) {
            super(encodedUrl);
        }

        private static ExpenditureRecordUrl listRecords() {
            return new ExpenditureRecordUrl("http://localhost:8080/expenditure_recorder/all_expenditures");
        }
    }
}