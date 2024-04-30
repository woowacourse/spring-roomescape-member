package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@JdbcTest
@Import(JdbcReservationRepository.class)
class JdbcReservationRepositoryTest {

    @Autowired
    private JdbcReservationRepository jdbcReservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("id로 예약을 조회하면 해당 id를 가진 예약을 반환한다.")
    @Test
    void shouldReturnReservationWhenReservationIdExist() {
        jdbcTemplate.update("insert into reservation_time (id, start_at) values (?, ?)", 1L, LocalTime.now());
        jdbcTemplate.update("insert into reservation (id, name, date, time_id) values (?, ?, ?, ?)", 1L, "test",
                LocalDate.now(), 1L);

        Optional<Reservation> findReservation = jdbcReservationRepository.findById(1L);

        assertThat(findReservation.isPresent()).isTrue();
        assertThat(findReservation.get().getId()).isEqualTo(1L);
    }

    @DisplayName("id로 예약을 조회시 존재하지 않으면 빈 객체를 반환한다.")
    @Test
    void shouldEmptyReservationWhenReservationIdNotExist() {
        Optional<Reservation> reservation = jdbcReservationRepository.findById(1L);

        assertThat(reservation.isEmpty()).isTrue();
    }

    @DisplayName("존재하는 모든 예약을 반환한다.")
    @Test
    void shouldReturnAllReservationsWhenFindAll() {
        jdbcTemplate.update("insert into reservation_time (id, start_at) values (?, ?)", 1L, LocalTime.now());
        jdbcTemplate.update("insert into reservation (id, name, date, time_id) values (?, ?, ?, ?)", 1L, "test",
                LocalDate.now(), 1L);

        List<Reservation> reservations = jdbcReservationRepository.findAll();

        assertThat(reservations.size()).isEqualTo(1);
    }

    @DisplayName("존재하는 예약이 없는 경우 빈 리스트를 반환한다.")
    @Test
    void shouldReturnEmptyListWhenReservationsNotExist() {
        List<Reservation> reservations = jdbcReservationRepository.findAll();

        assertThat(reservations).isEmpty();
    }

    @DisplayName("예약을 저장하면 id를 가진 예약을 저장 후 반환한다.")
    @Test
    void shouldReturnReservationWithIdWhenReservationSave() {
        jdbcTemplate.update("insert into reservation_time (id, start_at) values (?, ?)", 1L, LocalTime.now());
        Reservation reservationWithoutId = new Reservation(
                new Name("test"),
                LocalDate.now(),
                new ReservationTime(1L, LocalTime.now())
        );

        Reservation reservationWithId = jdbcReservationRepository.save(reservationWithoutId);

        assertThat(reservationWithId.getId()).isNotNull();
    }

    @DisplayName("id로 예약을 삭제한다.")
    @Test
    void shouldDeleteReservationWhenReservationIdExist() {
        jdbcTemplate.update("insert into reservation_time (id, start_at) values (?, ?)", 1L, LocalTime.now());
        jdbcTemplate.update("insert into reservation (id, name, date, time_id) values (?, ?, ?, ?)", 1L, "test",
                LocalDate.now(), 1L);

        jdbcReservationRepository.deleteById(1L);

        Integer count = jdbcTemplate.queryForObject("select count(*) from reservation", Integer.class);

        assertThat(count).isZero();
    }

    @DisplayName("예약 시간 id를 가진 예약의 개수를 조회한다.")
    @Test
    void shouldReturnCountOfReservationWhenReservationTimeUsed() {
        jdbcTemplate.update("insert into reservation_time (id, start_at) values (?, ?)", 1L, LocalTime.now());
        jdbcTemplate.update("insert into reservation (id, name, date, time_id) values (?, ?, ?, ?)", 1L, "test",
                LocalDate.now(), 1L);

        long count = jdbcReservationRepository.findReservationCountByTimeId(1L);

        assertThat(count).isOne();
    }

    @DisplayName("동일한 예약의 이름, 날짜, 시간으로 저장된 예약이 있는지 반환한다.")
    @Test
    void shouldReturnIsExistReservationWhenReservationsNameAndDateAndTimeIsSame() {
        String name = "test";
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        jdbcTemplate.update("insert into reservation_time (id, start_at) values (?, ?)", 1L, time);
        jdbcTemplate.update("insert into reservation (id, name, date, time_id) values (?, ?, ?, ?)", 1L, "test",
                date, 1L);

        boolean isExist = jdbcReservationRepository.existByDateAndTimeId(date, 1L);

        assertThat(isExist).isTrue();
    }
}
