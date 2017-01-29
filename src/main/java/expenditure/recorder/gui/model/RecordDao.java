package expenditure.recorder.gui.model;

import java.io.IOException;
import java.util.List;

public interface RecordDao {
    List<Record> getAllRecordsFromServer() throws IOException;
}
