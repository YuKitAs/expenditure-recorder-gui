package expenditure.recorder.gui;

import expenditure.recorder.gui.model.Record;

import java.util.List;

public interface RecordsPersistence {
    void persistent(List<Record> plainRecordList);
}
