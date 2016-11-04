import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ControllerTest {
    private final Controller controller = new Controller();

    private final ObservableList<Record> records = FXCollections.observableArrayList(
            new Record("foo", 1, "2012-01-01"),
            new Record("bar", 42, "2016-11-06"));

    private Method method;

    @Before
    public void setUp() throws NoSuchMethodException {
        method = Controller.class.getDeclaredMethod("convertToPlainList", ObservableList.class);

        method.setAccessible(true);
    }

    @Test
    public void convertToPlainList_WithCorrectRecords() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Record> plainRecordList = (ArrayList<Record>) method.invoke(controller, records);

        Record rec1 = plainRecordList.get(0);
        Record rec2 = plainRecordList.get(1);

        assertThat(rec1.getItem()).isEqualTo("foo");
        assertThat(rec1.getAmount()).isEqualTo(1);
        assertThat(rec1.getDate()).isEqualTo("2012-01-01");

        assertThat(rec2.getItem()).isEqualTo("bar");
        assertThat(rec2.getAmount()).isEqualTo(42);
        assertThat(rec2.getDate()).isEqualTo("2016-11-06");
    }
}
