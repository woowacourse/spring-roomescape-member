package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.Fixtures.JUNK_RESERVATION;
import static roomescape.Fixtures.JUNK_RESERVATION_1;
import static roomescape.Fixtures.JUNK_RESERVATION_2;
import static roomescape.Fixtures.JUNK_RESERVATION_3;
import static roomescape.Fixtures.JUNK_RESERVATION_4;
import static roomescape.Fixtures.JUNK_RESERVATION_5;
import static roomescape.Fixtures.JUNK_RESERVATION_6;
import static roomescape.Fixtures.JUNK_THEME_1;
import static roomescape.Fixtures.JUNK_THEME_2;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.model.Reservation;
import roomescape.model.Theme;

@Sql(scripts = {"/test-schema.sql", "/test-data.sql"})
@JdbcTest
class ReservationJdbcRepositoryTest {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationJdbcRepositoryTest(DataSource dataSource) {
        this.reservationRepository = new ReservationJdbcRepository(dataSource);
    }

    @Test
    @DisplayName("모든 예약 목록을 조회한다.")
    void findAll() {
        // given
        var reservations = List.of(
                JUNK_RESERVATION_1, JUNK_RESERVATION_2, JUNK_RESERVATION_3,
                JUNK_RESERVATION_4, JUNK_RESERVATION_5, JUNK_RESERVATION_6
        );

        // when
        final List<Reservation> found = reservationRepository.findAll();

        // then
        assertThat(found).containsExactlyInAnyOrderElementsOf(reservations);
    }

    @Test
    @DisplayName("타임 슬롯 ID에 해당하는 모든 예약 목록을 조회한다.")
    void findAllByTimeSlotId() {
        // given
        var timeSlotId = JUNK_RESERVATION_1.timeSlot().id();
        var reservations = List.of(
                JUNK_RESERVATION_1, JUNK_RESERVATION_4
        );

        // when
        final List<Reservation> found = reservationRepository.findAllByTimeSlotId(timeSlotId);

        // then
        assertThat(found).containsExactlyInAnyOrderElementsOf(reservations);
    }

    @Test
    @DisplayName("테마 ID에 해당하는 모든 예약 목록을 조회한다.")
    void findAllByThemeId() {
        // given
        var themeId = JUNK_RESERVATION_6.theme().id();
        var reservations = List.of(
                JUNK_RESERVATION_5, JUNK_RESERVATION_6
        );

        // when
        final List<Reservation> found = reservationRepository.findAllByThemeId(themeId);

        // then
        assertThat(found).containsExactlyInAnyOrderElementsOf(reservations);
    }

    @Test
    @DisplayName("날짜와 테마 ID에 해당하는 모든 예약 목록을 조회한다.")
    void findAllByDateAndThemeId() {
        // given
        var date = JUNK_RESERVATION_1.date();
        var themeId = JUNK_RESERVATION_1.theme().id();
        var reservations = List.of(
                JUNK_RESERVATION_1, JUNK_RESERVATION_2, JUNK_RESERVATION_3
        );

        // when
        final List<Reservation> found = reservationRepository.findAllByDateAndThemeId(date, themeId);

        // then
        assertThat(found).containsExactlyInAnyOrderElementsOf(reservations);
    }

    @Test
    @DisplayName("기간에 해당하는 인기 테마 목록을 조회한다.")
    void findPopularThemesByPeriod() {
        // given
        var endDate = JUNK_RESERVATION_1.date();
        var startDate = endDate.minusDays(1);
        var limit = 10;
        var popularThemes = List.of(
                JUNK_THEME_2, JUNK_THEME_1
        );

        // when
        final List<Theme> found = reservationRepository.findPopularThemesByPeriod(startDate, endDate, limit);

        // then
        assertThat(found).containsExactlyInAnyOrderElementsOf(popularThemes);
    }

    @Test
    @DisplayName("예약을 저장한다.")
    void save() {
        // given
        var reservation = JUNK_RESERVATION;

        // when
        final Long saved = reservationRepository.save(reservation);

        // then
        assertThat(saved).isEqualTo(reservation.id());
    }

    @Test
    @DisplayName("예약 ID에 해당하는 예약을 조회한다.")
    void findById() {
        // given
        var reservation = JUNK_RESERVATION_1;
        var reservationId = reservation.id();

        // when
        final Optional<Reservation> found = reservationRepository.findById(reservationId);

        // then
        assertThat(found).isPresent()
                .isEqualTo(Optional.of(reservation));
    }

    @Test
    @DisplayName("예약 ID에 해당하는 예약을 삭제한다.")
    void removeById() {
        // given
        var reservationId = JUNK_RESERVATION_1.id();

        // when
        final Boolean removed = reservationRepository.removeById(reservationId);

        // then
        assertThat(removed).isTrue();
    }
}
