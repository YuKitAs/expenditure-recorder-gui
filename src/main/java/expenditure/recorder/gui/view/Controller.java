package expenditure.recorder.gui.view;

import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.viewmodel.*;
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

    private ObservableList<Record> records = FXCollections.observableArrayList();

    // private final RecordsPersistence recordsPersistence;

    private ButtonViewModel buttonViewModel;

    private ComboBoxViewModel comboBoxViewModel;

    private DatePickerViewModel datePickerViewModel;

    private TableViewViewModel tableViewViewModel;

    private RecordUpdater recordUpdater;

    private TestViewModel testViewModel = new TestViewModel();

    public Controller() {
        // recordsPersistence = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemField.textProperty().bindBidirectional(testViewModel.itemTextProperty());
        amountField.textProperty().bindBidirectional(testViewModel.amountTextProperty());
        datePicker.valueProperty().bindBidirectional(testViewModel.dateProperty());

        itemErrorText.textProperty().bind(testViewModel.itemErrorTextProperty());
        amountErrorText.textProperty().bind(testViewModel.amountErrorTextProperty());
        dateErrorText.textProperty().bind(testViewModel.dateErrorTextProperty());

        recordTable.itemsProperty().bind(testViewModel.recordTableProperty());

        initDatePicker();

        addButton.setOnAction(event -> {
            itemCol.setCellValueFactory(new PropertyValueFactory<Record, String>("item"));
            amountCol.setCellValueFactory(new PropertyValueFactory<Record, String>("amount"));
            dateCol.setCellValueFactory(new PropertyValueFactory<Record, String>("date"));

            testViewModel.addRecord();
        });

        clearButton.setOnAction(event -> testViewModel.clearInput());

        deleteButton.setOnAction(event -> testViewModel.deleteRecord(recordTable.getSelectionModel().getSelectedItem()));
        //deleteButton.disableProperty().bind(testViewModel.deleteButtonDisabledProperty());


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

        tableViewViewModel.setRecordTableOnAction();
        */
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

    private void persistentRecords() {
        List<Record> plainRecordList = convertToPlainList(records);

        // recordsPersistence.persistent(plainRecordList);
    }

    private List<Record> convertToPlainList(ObservableList<Record> records) {
        List<Record> plainRecordList = new ArrayList<>();

        plainRecordList.addAll(records);

        return plainRecordList;
    }
}
