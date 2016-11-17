package expenditure.recorder.gui.viewmodel;

import expenditure.recorder.gui.model.Record;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class TableViewViewModel {
    private TableView<Record> recordTable;
    private Button deleteButton;

    public TableViewViewModel(TableView<Record> recordTable, Button deleteButton) {
        this.recordTable = recordTable;
        this.deleteButton = deleteButton;
    }

    public void setRecordTableOnAction() {
        recordTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                deleteButton.setDisable(false);
            }
        });
    }
}
