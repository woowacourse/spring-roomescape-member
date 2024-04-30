package roomescape.repository.reservationtime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.domain.ReservationTime;

import javax.sql.DataSource;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@DisplayName("예약 시간 DAO")
class ReservationTimeDaoTest {

    private final ReservationTimeRepository reservationTimeRepository;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public ReservationTimeDaoTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.reservationTimeRepository = new ReservationTimeDao(jdbcTemplate, dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @DisplayName("예약 시간 DAO느 생성 요청이 들어오면 DB에 값을 저장한다.")
    @Test
    void save() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 10));

        // when
        ReservationTime newReservationTime = reservationTimeRepository.save(reservationTime);
        Optional<ReservationTime> actual = reservationTimeRepository.findById(newReservationTime.getId());

        // then
        assertThat(actual.isPresent()).isTrue();
    }

    @DisplayName("예약 시간 DAO는 조회 요청이 들어오면 id에 맞는 값을 반환한다.")
    @Test
    void findById() {
        // given
        Long id = saveInitReservationTime();

        // when
        Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(id);

        // then
        assertThat(reservationTime.isPresent()).isTrue();
    }

    @DisplayName("예약 시간 DAO는 조회 요청이 들어오면 저장된 모든 값을 반환한다.")
    @Test
    void findAll() {
        // given
        int count = 5;
        for (int i = 0; i < count; i++) {
            saveInitReservationTime();
        }

        // when
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        // then
        assertThat(reservationTimes.size()).isEqualTo(count);
    }

    @DisplayName("예약 시간 DAO는 삭제 요청이 들어오면 id에 맞는 값을 삭제한다.")
    @Test
    void deleteById() {
        // given
        Long id = saveInitReservationTime();

        // when
        reservationTimeRepository.deleteById(id);
        Optional<ReservationTime> actual = reservationTimeRepository.findById(id);

        // then
        assertThat(actual.isPresent()).isFalse();
    }

    private Long saveInitReservationTime() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 10));
        return simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(reservationTime))
                .longValue();
    }
}
