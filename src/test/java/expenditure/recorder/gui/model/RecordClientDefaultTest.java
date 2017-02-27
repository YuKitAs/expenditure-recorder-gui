package expenditure.recorder.gui.model;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RecordClientDefaultTest {
    private final RecordClientDefault recordClient = new RecordClientDefault();

    @Test
    public void getAllRecordsFromServer_WithCorrectRecords() throws IOException {
        List<Record> records = recordClient.getAllRecordsFromServer();

        assertThat(records).extracting(Record::getAmountInCent).doesNotContainNull();
    }
}