package expenditure.recorder.gui.viewmodel;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.model.RecordClient;
import expenditure.recorder.gui.viewmodel.utilities.DateConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class ExpenditureRecordService {
    private final RecordClient recordClient;
    private final ObservableList<RecordTableItem> recordTableItems;
    private final FilteredList<RecordTableItem> filteredRecordTableItems;

    private LocalDate startDate = null;
    private LocalDate endDate = null;
    private String keyword = null;

    public ExpenditureRecordService(RecordClient recordClient) {
        this.recordClient = recordClient;
        this.recordTableItems = FXCollections.observableArrayList();
        this.filteredRecordTableItems = new FilteredList<>(recordTableItems, recordTableItem -> true);
    }

    public ObservableList<RecordTableItem> getAllRecordTableItemsFromServer() {
        try {
            List<Record> records = recordClient.getAllRecordsFromServer();
            recordTableItems.addAll(records.stream().map(RecordTableItem::from).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filteredRecordTableItems;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        updatePredicate();
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        updatePredicate();
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
        updatePredicate();
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
        try {
            recordTableItems.remove(recordTableItem);
            recordClient.deleteRecordOnServer(recordTableItem.getId());
        } catch (IOException e) {
            // Current do nothing.
        }
    }

    private void updatePredicate() {
        setPredicate(Optional.ofNullable(startDate).map(DateConverter::convertLocalDateToInstant),
                Optional.ofNullable(endDate).map(date -> date.plusDays(1)).map(DateConverter::convertLocalDateToInstant),
                Optional.ofNullable(keyword));

    }

    private void setPredicate(Optional<Instant> startTime, Optional<Instant> endTime, Optional<String> keyword) {
        filteredRecordTableItems.setPredicate(recordTableItem -> startTime.map(
                time -> time.isBefore(recordTableItem.getDateInstant()) || time.equals(recordTableItem.getDateInstant()))
                .orElse(true) && endTime.map(time -> time.isAfter(recordTableItem.getDateInstant()))
                .orElse(true) && recordTableItem.getItem().toLowerCase().contains(keyword.map(String::toLowerCase).orElse("")));
    }
}
