package roomescape.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationQueryRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
class JdbcReservationQueryRepositoryTest extends JdbcReservationTest {
    private final ReservationQueryRepository reservationQueryRepository;

    @Autowired
    public JdbcReservationQueryRepositoryTest(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.reservationQueryRepository = new JdbcReservationQueryRepository(jdbcTemplate);
    }

    @DisplayName("id로 예약을 조회한다.")
    @Test
    void shouldReturnReservationWhenReservationIdExist() {
        long id = createReservation().getId();
        Optional<Reservation> foundReservation = reservationQueryRepository.findById(id);
        assertThat(foundReservation).isPresent();
    }

    @DisplayName("id로 예약을 조회시 존재하지 않으면 빈 객체를 반환한다.")
    @Test
    void shouldEmptyReservationWhenReservationIdNotExist() {
        Optional<Reservation> reservation = reservationQueryRepository.findById(99L);
        assertThat(reservation).isEmpty();
    }

    @DisplayName("존재하는 모든 예약을 반환한다.")
    @Test
    void shouldReturnAllReservationsWhenFindAll() {
        createReservation();
        List<Reservation> reservations = reservationQueryRepository.findAll();
        assertThat(reservations).hasSize(1);
    }

//    @DisplayName("날짜와 테마 id가 주어지면, 예약 가능한 시간을 반환한다.")
//    @Test
//    @Sql("/insert-reservations.sql")
//    void shouldReturnAvailableTimes() {
//        LocalDate date = LocalDate.of(2024, 12, 25);
//        List<AvailableTimeDto> times = reservationQueryRepository.findAvailableReservationTimes(date, 1L)
//                .stream()
//                .filter(time -> !time.isBooked())
//                .toList();
//        assertThat(times).hasSize(4);
//    }
//
//    @DisplayName("주어진 날짜 사이에 예약된 갯수를 기준으로 테마를 반환한다.")
//    @Test
//    @Sql("/insert-reservations.sql")
//    void shouldReturnPopularThemes() {
//        LocalDate from = LocalDate.of(2024, 12, 24);
//        LocalDate to = LocalDate.of(2024, 12, 28);
//        int limit = 3;
//
//        List<Long> themeIds = reservationQueryRepository.findPopularThemesDateBetween(from, to, limit)
//                .stream()
//                .map(Theme::getId)
//                .toList();
//        assertThat(themeIds).containsExactly(4L, 3L, 2L);
//    }
//
//    @DisplayName("예약 시간 id를 가진 예약의 개수를 조회한다.")
//    @Test
//    void shouldReturnCountOfReservationWhenReservationTimeUsed() {
//        long id = createReservation().getId();
//        long count = reservationQueryRepository.findReservationCountByTimeId(id);
//        assertThat(count).isOne();
//    }

    @DisplayName("날짜, 시간으로 저장된 예약이 있는지 확인한다.")
    @Test
    void shouldReturnIsExistReservationWhenReservationsNameAndDateAndTimeIsSame() {
        Reservation reservation = createReservation();
        ReservationTime time = reservation.getTime();
        Theme theme = reservation.getTheme();

        boolean isExist = reservationQueryRepository.existBy(reservation.getDate(), time.getId(), theme.getId());
        assertThat(isExist).isTrue();
    }
}
