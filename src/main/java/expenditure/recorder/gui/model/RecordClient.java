package expenditure.recorder.gui.model;

import java.io.IOException;
import java.util.List;

public interface RecordClient {
    List<Record> getAllRecordsFromServer() throws IOException;

    Record addRecordToServer(Record record) throws IOException;

    void deleteRecordOnServer(String id) throws IOException;
}
