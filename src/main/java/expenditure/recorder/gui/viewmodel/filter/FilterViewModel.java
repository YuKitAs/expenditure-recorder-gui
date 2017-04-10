package expenditure.recorder.gui.viewmodel.filter;

import expenditure.recorder.gui.viewmodel.RecordTableItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FilterViewModel {
    private ObservableList<RecordTableItem> records;
    private ObservableList<RecordTableItem> filteredRecords = FXCollections.observableArrayList();

    private CurrentTimeRangeManager currentTimeRangeManager = CurrentTimeRangeManager.getInstance();

    public FilterViewModel(ObservableList<RecordTableItem> records) {
        this.records = records;
    }

    public void filterRecords() {
        filteredRecords.clear();

        TimeRange currentTimeRange = currentTimeRangeManager.getCurrentTimeRange();

        FilterPredicate filterPredicate = new FilterPredicate(currentTimeRange);

        if (currentTimeRange.equals(TimeRange.CUSTOM)) {
            records.forEach(record -> {
                if (filterPredicate.customTimeRangeFilter(currentTimeRangeManager.getFromDate(), currentTimeRangeManager.getToDate(),
                        record.getDateInstant())) {
                    filteredRecords.add(record);
                }
            });
        } else {
            records.forEach(record -> {
                if (filterPredicate.timeRangeFilter(record.getDateInstant())) {
                    filteredRecords.add(record);
                }
            });
        }

        updateTotalAmount();
    }

    public ObservableList<RecordTableItem> getFilteredRecords() {
        return filteredRecords;
    }

    private void updateTotalAmount() {

    }
}
