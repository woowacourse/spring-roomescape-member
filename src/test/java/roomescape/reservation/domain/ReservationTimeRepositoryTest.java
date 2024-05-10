package roomescape.reservation.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.common.RepositoryTest;
import roomescape.reservation.persistence.ReservationTimeDao;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.TestFixture.MIA_RESERVATION_TIME;

class ReservationTimeRepositoryTest extends RepositoryTest {
    private ReservationTimeRepository reservationTimeRepository;
    private SimpleJdbcInsert jdbcInsert;

    @BeforeEach
    void setUp() {
        this.reservationTimeRepository = new ReservationTimeDao(jdbcTemplate, dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    @DisplayName("예약 시간을 저장한다.")
    void save() {
        // given
        ReservationTime reservationTime = new ReservationTime(MIA_RESERVATION_TIME);

        // when
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        // when
        assertThat(savedReservationTime.getId()).isNotNull();
    }

    @Test
    @DisplayName("예약 시간 목록을 조회한다.")
    void findAll() {
        // given
        SqlParameterSource params = new BeanPropertySqlParameterSource(new ReservationTime(MIA_RESERVATION_TIME));
        jdbcInsert.execute(params);

        // when
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        // then
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);
        assertThat(reservationTimes.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("Id로 예약 시간을 조회한다.")
    void findById() {
        // given
        SqlParameterSource params = new BeanPropertySqlParameterSource(new ReservationTime(MIA_RESERVATION_TIME));
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        // when
        Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(id);

        // then
        assertThat(reservationTime).isPresent();
    }

    @Test
    @DisplayName("Id에 해당하는 예약 시간이 없다면 빈 Optional을 반환한다.")
    void findByNotExistingId() {
        // given
        Long id = 1L;

        // when
        Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(id);

        // then
        assertThat(reservationTime).isEmpty();
    }

    @Test
    @DisplayName("Id로 예약 시간을 삭제한다.")
    void deleteById() {
        // given
        SqlParameterSource params = new BeanPropertySqlParameterSource(new ReservationTime(MIA_RESERVATION_TIME));
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        // when
        reservationTimeRepository.deleteById(id);

        // then
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time where id = ?", Integer.class, id);
        assertThat(count).isEqualTo(0);
    }
}
