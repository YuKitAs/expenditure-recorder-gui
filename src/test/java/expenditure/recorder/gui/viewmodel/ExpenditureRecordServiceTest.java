package expenditure.recorder.gui.viewmodel;

import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.model.RecordDao;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpenditureRecordServiceTest {
    private RecordDao recordDao;
    private Record record;
    private List<Record> records = new ArrayList<>();
    private ExpenditureRecordService service;
    private ObjectProperty<ObservableList<RecordTableItem>> recordTable = new SimpleObjectProperty<>();
    private final RecordTableItem recordTableItem = RecordTableItem.from("some other item", "16", LocalDate.now());

    @Before
    public void SetUp() throws IOException {
        record = mock(Record.class);
        when(record.getItem()).thenReturn("some item");
        when(record.getAmountInCent()).thenReturn(42);
        when(record.getDate()).thenReturn(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        records.add(record);

        recordDao = mock(RecordDao.class);
        when(recordDao.getAllRecordsFromServer()).thenReturn(records);

        service = new ExpenditureRecordService(recordDao);

        recordTable.setValue(service.getInitialRecordTableItems());
    }

    @Test
    public void getInitialRecordTableItems() {
        ObservableList<RecordTableItem> recordTableItems = service.getInitialRecordTableItems();

        assertThat(recordTableItems.get(0).getItem()).isEqualTo("some item");
        assertThat(recordTableItems.get(0).getAmount()).isEqualTo("0.42");
        assertThat(recordTableItems.get(0).getDate()).isEqualTo(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @Test
    public void addRecordTableItem() {
        service.addRecordTableItem(recordTableItem);

        assertThat(recordTable.getValue().contains(recordTableItem)).isTrue();
    }

    @Test
    public void removeRecordTableItem() {
        RecordTableItem item = RecordTableItem.from(record);
        service.removeRecordTableItem(item);

        assertThat(recordTable.getValue().contains(item)).isFalse();
    }

    @Test
    public void getTotalAmountInCent() {
        if (recordTable.getValue().contains(recordTableItem)) {
            assertThat(service.getTotalAmountInCent()).isEqualTo(1642);
        } else {
            assertThat(service.getTotalAmountInCent()).isEqualTo(42);
        }
    }
}