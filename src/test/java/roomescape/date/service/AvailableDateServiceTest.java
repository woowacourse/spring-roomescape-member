package roomescape.date.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.closeddate.service.ClosedDateService;

@SpringBootTest
@Transactional
class AvailableDateServiceTest {

    @Autowired
    private AvailableDateService availableDateService;

    @Autowired
    private ClosedDateService closedDateService;

    @Test
    @DisplayName("오늘부터 30일치 날짜를 반환한다.")
    void findAvailableDates_returns_30_days() {
        // given & when
        List<LocalDate> actual = availableDateService.findAvailableDates();

        // then
        assertThat(actual).hasSize(30);
    }

    @Test
    @DisplayName("휴무일은 예약 가능 날짜에서 제외된다.")
    void findAvailableDates_excludes_closed_dates() {
        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        closedDateService.register(tomorrow);

        // when
        List<LocalDate> actual = availableDateService.findAvailableDates();

        // then
        assertThat(actual).hasSize(29);
        assertThat(actual).doesNotContain(tomorrow);
    }

    @Test
    @DisplayName("휴무일이 여러개이면 모두 제외된다.")
    void findAvailableDates_excludes_multiple_closed_dates() {
        // given
        closedDateService.register(LocalDate.now().plusDays(1));
        closedDateService.register(LocalDate.now().plusDays(2));
        closedDateService.register(LocalDate.now().plusDays(3));

        // when
        List<LocalDate> actual = availableDateService.findAvailableDates();

        // then
        assertThat(actual).hasSize(27);
    }
}
