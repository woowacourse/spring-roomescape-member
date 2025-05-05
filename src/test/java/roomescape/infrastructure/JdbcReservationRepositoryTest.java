package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.testFixture.Fixture.MEMBER_1;
import static roomescape.testFixture.Fixture.RESERVATION_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_1;
import static roomescape.testFixture.Fixture.THEME_1;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.infrastructure.JdbcReservationRepository;
import roomescape.testFixture.Fixture;
import roomescape.testFixture.JdbcHelper;

@JdbcTest
@Import(JdbcReservationRepository.class)
@ActiveProfiles("test")
class JdbcReservationRepositoryTest {

    @Autowired
    private JdbcReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE theme");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE members");
        jdbcTemplate.execute("ALTER TABLE members ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("save 후 생성된 id를 반환한다.")
    @Test
    void saveTest() {
        // given
        JdbcHelper.insertReservationTime(jdbcTemplate, RESERVATION_TIME_1);
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        JdbcHelper.insertMember(jdbcTemplate,  MEMBER_1);

        // when
        Long savedId = reservationRepository.save(RESERVATION_1);

        // then
        int count = getReservationCount();

        assertAll(
                () -> assertThat(savedId).isNotNull(),
                () -> assertThat(count).isEqualTo(1)
        );
    }

    @DisplayName("id로 예약을 삭제할 수 있다.")
    @Test
    void deleteByIdTest() {
        // given
        JdbcHelper.prepareAndInsertReservation(jdbcTemplate, RESERVATION_1);
        assertThat(getReservationCount()).isEqualTo(1);

        // when
        reservationRepository.deleteById(RESERVATION_1.getId());

        // then
        assertThat(getReservationCount()).isEqualTo(0);
    }

    @DisplayName("timeId, themeId, 날짜가 같은 예약이 존재함을 확인한다.")
    @Test
    void existsDuplicatedReservation() {
        // given
        JdbcHelper.prepareAndInsertReservation(jdbcTemplate, RESERVATION_1);

        LocalDate sameDate = RESERVATION_1.getReservationDate();
        long sameTimeId = RESERVATION_1.getReservationTime().getId();
        long sameThemeId = RESERVATION_1.getTheme().getId();

        // when
        boolean existsDuplicatedReservation = reservationRepository.existsDuplicatedReservation(sameDate, sameTimeId, sameThemeId);

        // then
        assertThat(existsDuplicatedReservation).isTrue();
    }

    @DisplayName("timeId가 다르면 중복된 예약이 존재하지 않음을 확인한다.")
    @Test
    void not_existsDuplicatedReservation_when_differentTimeId() {
        // given
        LocalDate date = LocalDate.of(2025, 1, 1);
        long timeId = 1L;
        long themeId = 1L;
        ReservationTime reservationTime = Fixture.createTimeById(timeId);
        Theme theme = Fixture.createThemeById(themeId);
        Reservation reservation = Fixture.createReservation(date, MEMBER_1.getId(), timeId, themeId);

        JdbcHelper.insertReservationTime(jdbcTemplate, reservationTime);
        JdbcHelper.insertTheme(jdbcTemplate, theme);
        JdbcHelper.prepareAndInsertReservation(jdbcTemplate, reservation);

        // when
        long differentTimeId = 2L;
        boolean existsDuplicatedReservation = reservationRepository.existsDuplicatedReservation(date, differentTimeId, themeId);

        // then
        assertThat(existsDuplicatedReservation).isFalse();
    }

    @DisplayName("themeId가 다르면 중복된 예약이 존재하지 않음을 확인한다.")
    @Test
    void not_existsDuplicatedReservation_when_differentThemeId() {
        // given
        LocalDate date = LocalDate.of(2025, 1, 1);
        long timeId = 1L;
        long themeId = 1L;
        ReservationTime reservationTime = Fixture.createTimeById(timeId);
        Theme theme = Fixture.createThemeById(themeId);
        Reservation reservation = Fixture.createReservation(date, 1L, timeId, themeId);

        JdbcHelper.insertReservationTime(jdbcTemplate, reservationTime);
        JdbcHelper.insertTheme(jdbcTemplate, theme);
        JdbcHelper.prepareAndInsertReservation(jdbcTemplate, reservation);

        // when
        long differentThemeId = 2L;
        boolean existsDuplicatedReservation = reservationRepository.existsDuplicatedReservation(date, timeId, differentThemeId);

        // then
        assertThat(existsDuplicatedReservation).isFalse();
    }

    @DisplayName("날짜가 다르면 중복된 예약이 존재하지 않음을 확인한다.")
    @Test
    void not_existsDuplicatedReservation_when_differentDate() {
        // given
        LocalDate date = LocalDate.of(2025, 1, 1);
        long timeId = 1L;
        long themeId = 1L;
        ReservationTime reservationTime = Fixture.createTimeById(timeId);
        Theme theme = Fixture.createThemeById(themeId);
        Reservation reservation = Fixture.createReservation(date, 1L, timeId, themeId);

        JdbcHelper.insertReservationTime(jdbcTemplate, reservationTime);
        JdbcHelper.insertTheme(jdbcTemplate, theme);
        JdbcHelper.prepareAndInsertReservation(jdbcTemplate, reservation);

        // when
        LocalDate differentDate = LocalDate.of(2025, 12, 1);
        boolean existsDuplicatedReservation = reservationRepository.existsDuplicatedReservation(differentDate, timeId, themeId);

        // then
        assertThat(existsDuplicatedReservation).isFalse();
    }

    private int getReservationCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT (*) FROM reservation", Integer.class);
    }
}
