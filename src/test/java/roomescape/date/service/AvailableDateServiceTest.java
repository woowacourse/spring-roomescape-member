package roomescape.date.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.closeddate.domain.ClosedDate;
import roomescape.closeddate.repository.JdbcClosedDateRepository;

@JdbcTest
class AvailableDateServiceTest {

    private JdbcClosedDateRepository closedDateRepository;
    private AvailableDateService availableDateService;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        closedDateRepository = new JdbcClosedDateRepository(jdbcTemplate);
        availableDateService = new AvailableDateService(closedDateRepository);
    }

    @Test
    @DisplayName("오늘부터 30일치 날짜를 반환한다.")
    void readAvailableDates_returns_30_days() {
        // given & when
        List<LocalDate> actual = availableDateService.readAvailableDates();

        // then
        assertThat(actual).hasSize(30);
    }

    @Test
    @DisplayName("휴무일은 예약 가능 날짜에서 제외된다.")
    void readAvailableDates_excludes_closed_dates() {
        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        closedDateRepository.save(ClosedDate.create(tomorrow));

        // when
        List<LocalDate> actual = availableDateService.readAvailableDates();

        // then
        assertThat(actual).hasSize(29);
        assertThat(actual).doesNotContain(tomorrow);
    }

    @Test
    @DisplayName("휴무일이 여러개이면 모두 제외된다.")
    void readAvailableDates_excludes_multiple_closed_dates() {
        // given
        closedDateRepository.save(ClosedDate.create(LocalDate.now().plusDays(1)));
        closedDateRepository.save(ClosedDate.create(LocalDate.now().plusDays(2)));
        closedDateRepository.save(ClosedDate.create(LocalDate.now().plusDays(3)));

        // when
        List<LocalDate> actual = availableDateService.readAvailableDates();

        // then
        assertThat(actual).hasSize(27);
    }
}
