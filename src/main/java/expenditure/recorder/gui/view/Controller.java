package expenditure.recorder.gui.view;

import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.model.TimeRange;
import expenditure.recorder.gui.viewmodel.*;
import expenditure.recorder.gui.viewmodel.helper.CurrentTimeRangeUpdater;
import expenditure.recorder.gui.viewmodel.helper.RecordUpdater;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextField itemField;

    @FXML
    private TextField amountField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button addButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ComboBox<String> filterBox;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private TableView<Record> recordTable;

    @FXML
    private TableColumn itemCol;

    @FXML
    private TableColumn amountCol;

    @FXML
    private TableColumn dateCol;

    @FXML
    private Text totalAmountText;

    @FXML
    private Text itemErrorText;

    @FXML
    private Text amountErrorText;

    @FXML
    private Text dateErrorText;

    @FXML
    private RadioButton filterBoxRadioButton;

    @FXML
    private RadioButton filterPickerRadioButton;

    private final ToggleGroup radioButtonGroup = new ToggleGroup();

    // private final RecordsPersistence recordsPersistence;

    private final ObservableList<String> timeRanges = FXCollections.observableArrayList("All", "Today", "Last 7 Days",
            "Last 30 Days");

    private ButtonViewModel buttonViewModel;

    private ComboBoxViewModel comboBoxViewModel;

    private DatePickerViewModel datePickerViewModel;

    private MyViewModel myViewModel = new MyViewModel();

    private CurrentTimeRangeUpdater currentTimeRangeUpdater = CurrentTimeRangeUpdater.getInstance();

    public Controller() {
        // recordsPersistence = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemField.textProperty().bindBidirectional(myViewModel.itemTextProperty());
        amountField.textProperty().bindBidirectional(myViewModel.amountTextProperty());
        datePicker.valueProperty().bindBidirectional(myViewModel.dateProperty());

        itemErrorText.textProperty().bind(myViewModel.itemErrorTextProperty());
        amountErrorText.textProperty().bind(myViewModel.amountErrorTextProperty());
        dateErrorText.textProperty().bind(myViewModel.dateErrorTextProperty());

        recordTable.itemsProperty().bind(myViewModel.recordTableProperty());

        totalAmountText.textProperty().bindBidirectional(myViewModel.totalAmountTextProperty());

        initRadioButton();
        filterBoxRadioButton.setOnAction(event -> {
            myViewModel.filterBoxRadioButtonOnAction();
        });
        filterPickerRadioButton.setOnAction(event -> {
            myViewModel.filterPickerRadioButtonOnAction();
        });

        initFilterBox();
        filterBox.selectionModelProperty().bindBidirectional(myViewModel.filterBoxProperty());
        filterBox.setOnAction(event -> {
            if (filterBoxRadioButton.isSelected()) {
                currentTimeRangeUpdater.setCurrentTimeRange(filterBox.getSelectionModel().getSelectedItem());

                myViewModel.updateRecords();
            }
        });

        initDatePicker();

        addButton.setOnAction(event -> {
            itemCol.setCellValueFactory(new PropertyValueFactory<Record, String>("item"));
            amountCol.setCellValueFactory(new PropertyValueFactory<Record, String>("amount"));
            dateCol.setCellValueFactory(new PropertyValueFactory<Record, String>("date"));

            myViewModel.addRecord();
        });

        clearButton.setOnAction(event -> myViewModel.clearInput());

        deleteButton.setOnAction(event -> myViewModel.deleteRecord(recordTable.getSelectionModel().getSelectedItem()));
        //deleteButton.disableProperty().bind(myViewModel.deleteButtonDisabledProperty());

        fromDatePicker.setOnAction(event -> {
            currentTimeRangeUpdater.setFromDate(fromDatePicker.getValue());
            currentTimeRangeUpdater.setCurrentTimeRange("Custom");

            if (filterPickerRadioButton.isSelected()) {

                myViewModel.updateRecords();
            }
        });

        toDatePicker.setOnAction(event -> {
            currentTimeRangeUpdater.setToDate(toDatePicker.getValue());
            currentTimeRangeUpdater.setCurrentTimeRange("Custom");

            if (filterPickerRadioButton.isSelected()) {
                myViewModel.updateRecords();
            }
        });

        /*
        recordUpdater = new RecordUpdater(recordTable, records, totalAmountText);

        datePickerViewModel = new DatePickerViewModel(datePicker, fromDatePicker, toDatePicker, filterPickerRadioButton, recordUpdater);

        comboBoxViewModel = new ComboBoxViewModel(filterBox, filterBoxRadioButton, recordUpdater);

        buttonViewModel = new ButtonViewModel(addButton, clearButton, deleteButton, filterBoxRadioButton, filterPickerRadioButton,
                recordTable, records, itemField, amountField, datePicker, itemErrorText, amountErrorText, dateErrorText, filterBox,
                recordUpdater);

        tableViewViewModel = new TableViewViewModel(recordTable, deleteButton);

        datePickerViewModel.initDatePicker();
        datePickerViewModel.setFromDatePickerOnAction();
        datePickerViewModel.setToDatePickerOnAction();

        comboBoxViewModel.initFilterBox();
        comboBoxViewModel.setFilterBoxOnAction();

        buttonViewModel.initRadioButton();
        buttonViewModel.setAddButtonOnAction(itemCol, amountCol, dateCol);
        buttonViewModel.setClearButtonOnAction();
        buttonViewModel.setDeleteButtonOnAction();
        buttonViewModel.setRadioButtonOnAction();
        */
    }

    private void initFilterBox() {
        filterBox.setItems(timeRanges);
        filterBox.getSelectionModel().selectFirst();
    }

    private void initDatePicker() {
        filterDateInDatePicker(datePicker, LocalDate.now(), false);
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

    private void initRadioButton() {
        filterBoxRadioButton.setToggleGroup(radioButtonGroup);
        filterPickerRadioButton.setToggleGroup(radioButtonGroup);
    }

    /*private void persistentRecords() {
        List<Record> plainRecordList = convertToPlainList(records);

        // recordsPersistence.persistent(plainRecordList);
    }

    private List<Record> convertToPlainList(ObservableList<Record> records) {
        List<Record> plainRecordList = new ArrayList<>();

        plainRecordList.addAll(records);

        return plainRecordList;
    }*/
}
