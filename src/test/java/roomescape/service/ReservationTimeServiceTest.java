package roomescape.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.dto.app.ReservationTimeAppRequest;
import roomescape.exception.DuplicatedReservationTimeException;
import roomescape.exception.ReservationExistsException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String rawTime = "10:00";
    private final LocalTime localTime = LocalTime.parse(rawTime);

    @AfterEach
    void clear() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.update("TRUNCATE TABLE reservation_time");
        jdbcTemplate.update("TRUNCATE TABLE reservation");
        jdbcTemplate.update("TRUNCATE TABLE theme");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");

        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    @DisplayName("성공: 예약 시간을 저장하고, id 값과 함께 반환한다.")
    @Test
    void save() {
        ReservationTime saved = reservationTimeService.save(new ReservationTimeAppRequest(rawTime));
        assertThat(saved).isEqualTo(new ReservationTime(saved.getId(), localTime));
    }

    @DisplayName("실패: 잘못된 시간 포맷을 저장하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"24:00", "-1:00", "10:60"})
    @NullAndEmptySource
    void save_IllegalTimeFormat(String invalidRawTime) {
        assertThatThrownBy(
            () -> reservationTimeService.save(new ReservationTimeAppRequest(invalidRawTime))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("실패: 이미 존재하는 시간을 추가할 수 없다.")
    @Test
    void save_TimeAlreadyExists() {
        reservationTimeService.save(new ReservationTimeAppRequest(rawTime));
        assertThatThrownBy(
            () -> reservationTimeService.save(new ReservationTimeAppRequest(rawTime))
        ).isInstanceOf(DuplicatedReservationTimeException.class);
    }

    @DisplayName("실패: 시간을 사용하는 예약이 존재하는 경우 시간을 삭제할 수 없다.")
    @Test
    void delete_ReservationExists() {
        ReservationTime savedTime = reservationTimeService.save(new ReservationTimeAppRequest(rawTime));

        Theme savedTheme = themeRepository.save(
            new Theme("themeName", "themeDesc", "https://")
        );

        reservationRepository.save(
            new Reservation("name", LocalDate.parse("2060-01-01"), savedTime, savedTheme)
        );

        assertThatThrownBy(
            () -> reservationTimeService.delete(savedTime.getId())
        ).isInstanceOf(ReservationExistsException.class);
    }
}
