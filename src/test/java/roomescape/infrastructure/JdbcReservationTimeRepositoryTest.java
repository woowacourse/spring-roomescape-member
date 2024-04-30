package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;

@JdbcTest
@Import(JdbcReservationTimeRepository.class)
class JdbcReservationTimeRepositoryTest {

    @Autowired
    private JdbcReservationTimeRepository jdbcReservationTimeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("id를 검색해서 존재하면 예약을 반환한다.")
    @Test
    void shouldReturnOptionalReservationTimeWhenFindById() {
        jdbcTemplate.update("insert into reservation_time (id, start_at) values (?, ?)",
                1L, Time.valueOf(LocalTime.of(10, 0)));

        Optional<ReservationTime> reservationTime = jdbcReservationTimeRepository.findById(1L);

        assertThat(reservationTime).isNotEmpty();
        assertThat(reservationTime.get()).isEqualTo(new ReservationTime(1L, LocalTime.of(10, 0)));
    }

    @DisplayName("id를 검색해서 존재하지 않으면 빈 옵셔널 객체를 반환한다.")
    @Test
    void shouldReturnOptionalEmptyWhenReservationTimeDoesNotExist() {
        Optional<ReservationTime> reservationTime = jdbcReservationTimeRepository.findById(1L);

        assertThat(reservationTime).isEmpty();
    }

    @DisplayName("id가 생성되지 않은 예약 시간을 추가하면, id가 주어진 예약 시간이 저장된다.")
    @Test
    void shouldReturnReservationTimeWithIdWhenCreateReservationTimeWithoutId() {
        ReservationTime reservationTimeWithoutId = new ReservationTime(LocalTime.of(10, 0));
        ReservationTime reservationTime = jdbcReservationTimeRepository.create(reservationTimeWithoutId);

        Integer count = jdbcTemplate.queryForObject("select count(*) from reservation_time", Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("특정 시간이 저장소에 존재하면 true를 반환한다.")
    @Test
    void shouldReturnTrueWhenReservationTimeAlreadyExist() {
        LocalTime startAt = LocalTime.of(10, 0);
        jdbcTemplate.update("insert into reservation_time (id, start_at) values (?, ?)",
                1L, startAt);

        assertThat(jdbcReservationTimeRepository.existsByStartAt(startAt)).isTrue();
    }

    @DisplayName("특정 시간이 저장소에 존재하지 않으면 false를 반환한다.")
    @Test
    void shouldReturnFalseWhenReservationTimeAlreadyExist() {
        LocalTime startAt = LocalTime.of(10, 0);

        assertThat(jdbcReservationTimeRepository.existsByStartAt(startAt)).isFalse();
    }
}
