package roomescape.time.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationStatus;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.JdbcThemeRepository;
import roomescape.time.domain.ReservationTime;

@JdbcTest
class ReservationTimeRepositoryTest {
    private JdbcReservationTimeRepository jdbcReservationTimeRepository;
    private JdbcThemeRepository jdbcThemeRepository;
    private JdbcReservationRepository jdbcReservationRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcReservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        jdbcThemeRepository = new JdbcThemeRepository(jdbcTemplate);
        jdbcReservationRepository = new JdbcReservationRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("예약 가능한 시간을 조회한다.")
    void findAvailableTimes() {
        // given
        ReservationTime time1 = jdbcReservationTimeRepository.save(ReservationTime.create(LocalTime.of(12, 0)));
        ReservationTime time2 = jdbcReservationTimeRepository.save(ReservationTime.create(LocalTime.of(13, 0)));
        ReservationTime time3 = jdbcReservationTimeRepository.save(ReservationTime.create(LocalTime.of(14, 0)));
        LocalDate date = LocalDate.of(2099, 10, 10);
        Theme theme1 = Theme.create("테마1", "테마 설명", "테마 썸네일");
        Theme changedTheme1 = theme1.changeStatus(true);
        Theme theme2 = jdbcThemeRepository.save(changedTheme1);
        jdbcReservationRepository.save(Reservation.create("한다", date, time1.startAt(), theme2));
        jdbcReservationRepository.save(Reservation.create("한다", date, time2.startAt(), theme2));

        // when
        List<ReservationTime> availableTimes = jdbcReservationTimeRepository.findAvailableByDateAndThemeId(
                date, theme2.id(), ReservationStatus.RESERVED);

        // then
        assertThat(availableTimes)
                .hasSize(1);
    }
}
