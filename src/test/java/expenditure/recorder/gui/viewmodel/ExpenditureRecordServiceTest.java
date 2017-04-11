package expenditure.recorder.gui.viewmodel;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.model.RecordClient;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpenditureRecordServiceTest {
    private RecordClient recordClient;

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

        recordClient = mock(RecordClient.class);
        when(recordClient.getAllRecordsFromServer()).thenReturn(records);
        service = new ExpenditureRecordService(recordClient);

        recordTable.setValue(service.getAllRecordTableItemsFromServer());

        SimpleListProperty<RecordTableItem> filteredRecordItemsProperty = new SimpleListProperty<>(
                service.getAllRecordTableItemsFromServer());

        recordTable.bind(filteredRecordItemsProperty);
    }

    @Test
    public void getAllRecordTableItemsFromServer_WithCorrectRecordItem() throws IOException {
        ObservableList<RecordTableItem> recordTableItems = service.getAllRecordTableItemsFromServer();

        assertThat(equalsDefaultRecordTableItem(recordTableItems.get(0))).isTrue();
    }

    @Test
    public void setStartDate() throws Exception {
        service.setStartDate(LocalDate.now());
        assertThat(equalsDefaultRecordTableItem(recordTable.get().get(0))).isTrue();

        service.setStartDate(LocalDate.now().plusDays(1));
        assertThat(recordTable.get().isEmpty()).isTrue();
    }

    @Test
    public void setEndDate() throws Exception {
        service.setEndDate(LocalDate.now());
        assertThat(equalsDefaultRecordTableItem(recordTable.get().get(0))).isTrue();

        service.setEndDate(LocalDate.now().minusDays(1));
        assertThat(recordTable.get().isEmpty()).isTrue();
    }

    @Test
    @Ignore
    public void addRecordTableItem() throws IOException {
        when(recordClient.addRecordToServer(record)).thenReturn(
                new Record("0000", "some item", 42, LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        service.addRecordTableItem(recordTableItem);

        assertThat(recordTable.getValue().contains(recordTableItem)).isTrue();
        assertThat(recordTable.getValue().get(0).getId()).isEqualTo("0000");
    }

    @Test
    public void removeRecordTableItem() {
        RecordTableItem item = RecordTableItem.from(record);
        service.removeRecordTableItem(item);

        assertThat(recordTable.getValue().contains(item)).isFalse();
    }

    private Boolean equalsDefaultRecordTableItem(RecordTableItem recordTableItem) {
        if (recordTableItem.getItem().equals("some item") && recordTableItem.getAmount().equals("0.42") && recordTableItem.getDate()
                .equals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
            return true;
        }

        return false;
    }
}