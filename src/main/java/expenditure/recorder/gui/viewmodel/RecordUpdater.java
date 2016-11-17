package expenditure.recorder.gui.viewmodel;

import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.model.TimeRange;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.ZoneId;

public class RecordUpdater {
    private TableView<Record> recordTable;

    private ObservableList<Record> records = FXCollections.observableArrayList();
    private ObservableList<Record> filteredRecords = FXCollections.observableArrayList();

    private Text totalAmountText;

    private int totalAmount;

    private CurrentTimeRangeUpdater currentTimeRangeUpdater;

    public RecordUpdater(TableView<Record> recordTable, ObservableList<Record> records, Text totalAmountText) {
        this.recordTable = recordTable;
        this.records = records;
        this.totalAmountText = totalAmountText;
        currentTimeRangeUpdater = CurrentTimeRangeUpdater.getInstance();
    }

    public void updateRecords(TimeRange currentTimeRange) {
        filteredRecords.clear();

        if (currentTimeRange.equals(TimeRange.ALL)) {
            recordTable.setItems(records);
            updateTotalAmount(records);
        } else if (currentTimeRange.equals(TimeRange.TODAY)) {
            ShowRecordsOfToday();
        } else if (currentTimeRange.equals(TimeRange.LAST_7_DAYS)) {
            ShowRecordsInLast7Days();
        } else if (currentTimeRange.equals(TimeRange.LAST_30_DAYS)) {
            ShowRecordsInLast30Days();
        } else if (currentTimeRange.equals(TimeRange.CUSTOM)) {
            showRecordsInCustomTimeRange();
        }
    }

    private void ShowRecordsOfToday() {
        records.forEach(record -> {
            LocalDate date = LocalDate.parse(record.getDate());

            if (date.equals(LocalDate.now(ZoneId.of("CET")))) {
                filteredRecords.add(record);
            }
        });

        recordTable.setItems(filteredRecords);

        updateTotalAmount(filteredRecords);
    }

    private void ShowRecordsInLast7Days() {
        records.forEach(record -> {
            LocalDate date = LocalDate.parse(record.getDate());

            if (date.isAfter(LocalDate.now().minusWeeks(1))) {
                filteredRecords.add(record);
            }
        });

        recordTable.setItems(filteredRecords);

        updateTotalAmount(filteredRecords);
    }

    private void ShowRecordsInLast30Days() {
        records.forEach(record -> {
            LocalDate date = LocalDate.parse(record.getDate());

            if (date.isAfter(LocalDate.now().minusMonths(1))) {
                filteredRecords.add(record);
            }
        });

        recordTable.setItems(filteredRecords);

        updateTotalAmount(filteredRecords);
    }

    private void showRecordsInCustomTimeRange() {
        LocalDate fromDate = currentTimeRangeUpdater.getFromDate();
        LocalDate toDate = currentTimeRangeUpdater.getToDate();

        if (fromDate == null && toDate == null) {
            recordTable.setItems(records);
            updateTotalAmount(records);

            return;
        }

        records.forEach(record -> {
            LocalDate date = LocalDate.parse(record.getDate());

            if (fromDate == null) {
                if (date.isBefore(toDate) || date.isEqual(toDate)) {
                    filteredRecords.add(record);
                }
            } else if (toDate == null) {
                if (date.isAfter(fromDate) || date.isEqual(fromDate)) {
                    filteredRecords.add(record);
                }
            } else {
                if ((date.isAfter(fromDate) || date.isEqual(fromDate)) && (date.isBefore(toDate) || date.isEqual(toDate))) {
                    filteredRecords.add(record);
                }
            }
        });

        recordTable.setItems(filteredRecords);

        updateTotalAmount(filteredRecords);
    }

    public void updateTotalAmount(ObservableList<Record> records) {
        totalAmount = 0;

        records.forEach(record -> totalAmount += Integer.parseInt(record.getAmount().substring(2)));

        totalAmountText.setText("€ " + Integer.toString(totalAmount));
    }
}
