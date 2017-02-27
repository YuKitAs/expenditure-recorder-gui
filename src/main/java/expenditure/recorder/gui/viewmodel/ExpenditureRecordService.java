package expenditure.recorder.gui.viewmodel;

import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.model.RecordClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenditureRecordService {
    private final RecordClient recordClient;
    private ObservableList<RecordTableItem> recordTableItems;

    public ExpenditureRecordService(RecordClient recordClient) {
        this.recordClient = recordClient;
        this.recordTableItems = FXCollections.observableArrayList();
    }

    public ObservableList<RecordTableItem> getInitialRecordTableItems() {
        try {
            List<Record> records = recordClient.getAllRecordsFromServer();
            recordTableItems = FXCollections.observableArrayList(records.stream().map(RecordTableItem::from).collect(Collectors.toList()));
        } catch (IOException e) {
            // Currently do nothing.
        }

        return recordTableItems;
    }

    public void addRecordTableItem(RecordTableItem recordTableItem) {
        recordTableItems.add(recordTableItem);

        try {
            uploadRecord(recordTableItem);
        } catch (IOException e) {
            // Current do nothing.
        }
    }

    public void removeRecordTableItem(RecordTableItem recordTableItem) {
        recordTableItems.remove(recordTableItem);
    }

    public Integer getTotalAmountInCent() {
        return recordTableItems.stream().mapToInt(RecordTableItem::getAmountInCent).sum();
    }

    private void uploadRecord(RecordTableItem recordTableItem) throws IOException {
        Record record = new Record(null, recordTableItem.getItem(), recordTableItem.getAmountInCent(), recordTableItem.getDateInstant());

        recordClient.addRecordToServer(record);
    }
}
