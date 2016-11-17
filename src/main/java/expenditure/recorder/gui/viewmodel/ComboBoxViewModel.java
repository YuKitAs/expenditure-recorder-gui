package expenditure.recorder.gui.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;

public class ComboBoxViewModel {
    private ComboBox<String> filterBox;

    private final ObservableList<String> timeRanges = FXCollections.observableArrayList(
            "All", "Today", "Last 7 Days", "Last 30 Days");

    private RadioButton filterBoxRadioButton;

    private CurrentTimeRangeUpdater currentTimeRangeUpdater;

    private RecordUpdater recordUpdater;

    public ComboBoxViewModel(ComboBox<String> filterBox, RadioButton filterBoxRadioButton, RecordUpdater recordUpdater) {
        this.filterBox = filterBox;
        this.filterBoxRadioButton = filterBoxRadioButton;
        this.recordUpdater = recordUpdater;
        currentTimeRangeUpdater = CurrentTimeRangeUpdater.getInstance();
    }

    public void initFilterBox() {
        filterBox.setItems(timeRanges);
        filterBox.getSelectionModel().selectFirst();
    }

    public void setFilterBoxOnAction() {
        filterBox.setOnAction(event -> {
            if (filterBoxRadioButton.isSelected()) {
                currentTimeRangeUpdater.setCurrentTimeRange(filterBox.getSelectionModel().getSelectedItem());

                recordUpdater.updateRecords(currentTimeRangeUpdater.getCurrentTimeRange());
            }
        });
    }
}
