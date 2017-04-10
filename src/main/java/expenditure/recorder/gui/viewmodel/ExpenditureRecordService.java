package expenditure.recorder.gui.viewmodel;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.model.RecordClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
        Record record = new Record(null, recordTableItem.getItem(), recordTableItem.getAmountInCent(), recordTableItem.getDateInstant());

        try {
            recordTableItems.add(RecordTableItem.from(recordClient.addRecordToServer(record)));
        } catch (IOException e) {
            // Current do nothing.
        }
    }

    public void removeRecordTableItem(RecordTableItem recordTableItem) {
        recordTableItems.remove(recordTableItem);

        try {
            recordClient.deleteRecordOnServer(recordTableItem.getId());
        } catch (IOException e) {
            // Current do nothing.
        }
    }
}
