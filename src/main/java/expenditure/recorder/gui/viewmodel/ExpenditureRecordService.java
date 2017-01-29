package expenditure.recorder.gui.viewmodel;

import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.model.RecordDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenditureRecordService {
    private final RecordDao recordDao;
    private ObservableList<RecordTableItem> recordTableItems;

    public ExpenditureRecordService(RecordDao recordDao) {
        this.recordDao = recordDao;
    }

    public ObservableList<RecordTableItem> getInitialRecordTableItems() {
        try {
            List<Record> records = recordDao.getAllRecordsFromServer();
            recordTableItems = FXCollections.observableArrayList(records.stream().map(RecordTableItem::from).collect(Collectors.toList()));
        } catch (IOException e) {
            return FXCollections.emptyObservableList();
        }

        return recordTableItems;
    }

    public void addRecordTableItem(RecordTableItem recordTableItem) {
        recordTableItems.add(recordTableItem);
    }

    public void removeRecordTableItem(RecordTableItem recordTableItem) {
        recordTableItems.remove(recordTableItem);
    }

    public Integer getTotalAmountInCent() {
        return recordTableItems.stream().mapToInt(RecordTableItem::getAmountInCent).sum();
    }
}
