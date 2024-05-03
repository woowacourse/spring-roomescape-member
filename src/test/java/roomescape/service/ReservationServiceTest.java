package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.dto.app.ReservationAppRequest;
import roomescape.exception.DuplicatedReservationException;
import roomescape.exception.PastReservationException;
import roomescape.exception.ReservationTimeNotFoundException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String name = "tre";
    private final String rawDate = "2060-01-01";
    private final Long timeId = 1L;
    private final Long themeId = 1L;

    private final LocalDate date = LocalDate.parse(rawDate);
    private final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("10:00"));
    private final Theme theme = new Theme(1L, "theme1", "desc1", "https://");

    @BeforeEach
    void beforeEach() {
        jdbcTemplate.update("INSERT INTO theme(name, description, thumbnail) VALUES ('theme1', 'desc1', 'https://')");
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('10:00')");
    }

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

    @DisplayName("성공: 예약을 저장하고, 해당 예약을 id값과 함께 반환한다.")
    @Test
    void save() {
        Reservation saved = reservationService.save(new ReservationAppRequest(name, rawDate, timeId, themeId));
        assertThat(saved)
            .isEqualTo(new Reservation(saved.getId(), name, date, reservationTime, theme));
    }

    @DisplayName("실패: 빈 이름을 저장하면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void save_IllegalName(String invalidName) {
        assertThatThrownBy(
            () -> reservationService.save(new ReservationAppRequest(invalidName, rawDate, timeId, themeId))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("실패: 존재하지 않는 날짜 입력 시 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"2030-13-01", "2030-12-32"})
    void save_IllegalDate(String invalidRawDate) {
        assertThatThrownBy(
            () -> reservationService.save(new ReservationAppRequest(name, invalidRawDate, timeId, themeId))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("실패: 존재하지 않는 시간 ID 입력 시 예외가 발생한다.")
    @Test
    void save_TimeIdDoesntExist() {
        assertThatThrownBy(
            () -> reservationService.save(new ReservationAppRequest(name, rawDate, 2L, themeId))
        ).isInstanceOf(ReservationTimeNotFoundException.class);
    }

    @DisplayName("실패: 중복 예약을 생성하면 예외가 발생한다.")
    @Test
    void save_Duplication() {
        reservationService.save(new ReservationAppRequest(name, rawDate, timeId, themeId));

        assertThatThrownBy(
            () -> reservationService.save(new ReservationAppRequest(name, rawDate, timeId, themeId))
        ).isInstanceOf(DuplicatedReservationException.class);
    }

    @DisplayName("실패: 과거 날짜 예약 생성하면 예외 발생 -- 어제")
    @Test
    void save_PastDateReservation() {
        String yesterday = LocalDate.now().minusDays(1).toString();

        assertThatThrownBy(
            () -> reservationService.save(new ReservationAppRequest(name, yesterday, timeId, themeId))
        ).isInstanceOf(PastReservationException.class);
    }

    @DisplayName("실패: 같은 날짜, 과거 시간 예약 생성하면 예외 발생 -- 1분 전")
    @Test
    void save_TodayPastTimeReservation() {
        String today = LocalDate.now().toString();
        LocalTime oneMinuteAgo = LocalTime.now().minusMinutes(1);

        ReservationTime savedTime = reservationTimeRepository.save(new ReservationTime(oneMinuteAgo));

        assertThatThrownBy(
            () -> reservationService.save(new ReservationAppRequest(name, today, savedTime.getId(), themeId))
        ).isInstanceOf(PastReservationException.class);
    }
}
