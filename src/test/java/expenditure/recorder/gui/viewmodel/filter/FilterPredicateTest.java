package expenditure.recorder.gui.viewmodel.filter;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FilterPredicateTest {
    private FilterPredicate filterPredicate;
    private final Instant TODAY = Instant.now();
    private final Instant IN_LAST_7_DAYS = TODAY.minus(Duration.ofDays(6));
    private final Instant IN_LAST_30_DAYS = TODAY.minus(Duration.ofDays(29));
    private final Instant BEFORE_LAST_30_DAYS = TODAY.minus(Duration.ofDays(30));

    @Test
    public void filterAllTimeRange() {
        filterPredicate = new FilterPredicate(TimeRange.ALL);

        assertThat(filterPredicate.timeRangeFilter(TODAY)).isTrue();
        assertThat(filterPredicate.timeRangeFilter(IN_LAST_7_DAYS)).isTrue();
        assertThat(filterPredicate.timeRangeFilter(IN_LAST_30_DAYS)).isTrue();
    }

    @Test
    public void filterTodayTimeRange() {
        filterPredicate = new FilterPredicate(TimeRange.TODAY);

        assertThat(filterPredicate.timeRangeFilter(TODAY)).isTrue();
        assertThat(filterPredicate.timeRangeFilter(IN_LAST_7_DAYS)).isFalse();
    }

    @Test
    public void filterLastSevenDaysTimeRange() {
        filterPredicate = new FilterPredicate(TimeRange.LAST_7_DAYS);

        assertThat(filterPredicate.timeRangeFilter(IN_LAST_7_DAYS)).isTrue();
        assertThat(filterPredicate.timeRangeFilter(IN_LAST_30_DAYS)).isFalse();
    }

    @Test
    public void filterLastThirtyDaysTimeRange() {
        filterPredicate = new FilterPredicate(TimeRange.LAST_30_DAYS);

        assertThat(filterPredicate.timeRangeFilter(IN_LAST_30_DAYS)).isTrue();
        assertThat(filterPredicate.timeRangeFilter(BEFORE_LAST_30_DAYS)).isFalse();
    }
}