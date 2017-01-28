package expenditure.recorder.gui;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import expenditure.recorder.gui.model.Record;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yuka on 28.01.17.
 */
public class ExpenditureRecordFetcher {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static ObservableList<Record> records = FXCollections.observableArrayList();

    public static void run() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuthentication("testuser", "nopassword");

        HttpRequestFactory requestFactory =
                HTTP_TRANSPORT.createRequestFactory(request -> {
                    request.setHeaders(headers);
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                });

        ExpenditureRecordUrl url = ExpenditureRecordUrl.listRecords();
        HttpRequest request = requestFactory.buildGetRequest(url);
        parseResponse(request.execute());
    }

    public static void parseResponse(HttpResponse response) throws IOException {
        List<RecordObject> recordObjects = Arrays.asList(response.parseAs(RecordObject[].class));

        if (recordObjects.isEmpty()) {
            System.out.println("No records found.");
        } else {
            for (RecordObject record : recordObjects) {
                records.add(new Record(record.getItem(), record.getAmountInCent().toString(), record.getDate().toString().substring(0, 10)));
            }
        }
    }

    public static class ExpenditureRecordUrl extends GenericUrl {

        @SuppressWarnings("unused")
        public ExpenditureRecordUrl(String encodedUrl) {
            super(encodedUrl);
        }
        public static ExpenditureRecordUrl listRecords() {
            return new ExpenditureRecordUrl("http://localhost:8080/expenditure_recorder/all_expenditures");
        }

    }

    public ObservableList<Record> getRecords() {
        return records;
    }

    public static class RecordObject {
        @Key
        private String item;
        @Key
        private Integer amountInCent;
        @Key
        private DateTime date;

        public String getItem() {
            return item;
        }

        public Integer getAmountInCent() {
            return amountInCent;
        }

        public Instant getDate() {
            return Instant.ofEpochMilli(date.getValue());
        }
    }
}
