package expenditure.recorder.gui.view;

import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.viewmodel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
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

    public Controller() {
        // recordsPersistence = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
