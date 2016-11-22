package expenditure.recorder.gui.viewmodel;

import expenditure.recorder.gui.viewmodel.helper.CurrentTimeRangeUpdater;
import expenditure.recorder.gui.viewmodel.helper.RecordUpdater;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;

import java.time.LocalDate;

public class DatePickerViewModel {
    private DatePicker datePicker;
    private DatePicker fromDatePicker;
    private DatePicker toDatePicker;

    private RecordUpdater recordUpdater;
    private CurrentTimeRangeUpdater currentTimeRangeUpdater;

    private RadioButton filterPickerRadioButton;

    public DatePickerViewModel(DatePicker datePicker, DatePicker fromDatePicker, DatePicker toDatePicker,
                               RadioButton filterPickerRadioButton, RecordUpdater recordUpdater) {
        this.datePicker = datePicker;
        this.fromDatePicker = fromDatePicker;
        this.toDatePicker = toDatePicker;
        this.recordUpdater = recordUpdater;
        this.filterPickerRadioButton = filterPickerRadioButton;
        currentTimeRangeUpdater = CurrentTimeRangeUpdater.getInstance();
    }

    public void initDatePicker() {
        filterDateInDatePicker(datePicker, LocalDate.now(), false);
        filterDateInDatePicker(fromDatePicker, currentTimeRangeUpdater.getToDate(), false);
        filterDateInDatePicker(toDatePicker, currentTimeRangeUpdater.getFromDate(), true);
    }

    private void filterDateInDatePicker(DatePicker filteredDatePicker, LocalDate dateLimit, boolean dateAfterAllowed) {
        if (dateLimit == null) {
            return;
        }

        filteredDatePicker.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                if (item.isAfter(dateLimit) && !dateAfterAllowed) {
                    this.setDisable(true);
                } else if (item.isBefore(dateLimit) && dateAfterAllowed) {
                    this.setDisable(true);
                }
            }
        });
    }

    public void setFromDatePickerOnAction() {
        fromDatePicker.setOnAction(event -> {
            currentTimeRangeUpdater.setFromDate(fromDatePicker.getValue());

            if (filterPickerRadioButton.isSelected()) {
                recordUpdater.updateRecords(currentTimeRangeUpdater.getCurrentTimeRange());
            }
        });
    }

    public void setToDatePickerOnAction() {
        toDatePicker.setOnAction(event -> {
            currentTimeRangeUpdater.setToDate(toDatePicker.getValue());

            if (filterPickerRadioButton.isSelected()) {
                recordUpdater.updateRecords(currentTimeRangeUpdater.getCurrentTimeRange());
            }
        });
    }
}
