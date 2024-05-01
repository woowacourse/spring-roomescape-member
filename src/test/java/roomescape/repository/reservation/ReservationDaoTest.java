package roomescape.repository.reservation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.Fixtures.themeFixture;

@JdbcTest
@Import(ReservationDao.class)
@DisplayName("예약 DAO")
@Sql(value = {"/recreate_table.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationDaoTest {

    private final ReservationRepository reservationRepository;
    private final SimpleJdbcInsert simpleJdbcInsertWithReservation;
    private final SimpleJdbcInsert simpleJdbcInsertWithReservationTime;

    @Autowired
    public ReservationDaoTest(ReservationRepository reservationRepository, DataSource dataSource) {
        this.reservationRepository = reservationRepository;
        this.simpleJdbcInsertWithReservation = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        this.simpleJdbcInsertWithReservationTime = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @DisplayName("예약 DAO는 생성 요청이 들어오면 DB에 값을 저장한다.")
    @Test
    void save() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 10));
        Long reservationTimeId = simpleJdbcInsertWithReservationTime.executeAndReturnKey(new BeanPropertySqlParameterSource(reservationTime))
                .longValue();
        ReservationTime newReservationTime = new ReservationTime(reservationTimeId, reservationTime.getStartAt());
        Reservation reservation = new Reservation(
                "브라운",
                LocalDate.of(2024, 11, 16),
                newReservationTime,
                new Theme(1L, themeFixture)
        );

        // when
        Reservation newReservation = reservationRepository.save(reservation);
        Optional<Reservation> actual = reservationRepository.findById(newReservation.getId());

        // then
        assertThat(actual.isPresent()).isTrue();
    }

    @DisplayName("예약 DAO는 조회 요청이 들어오면 id에 맞는 값을 반환한다.")
    @Test
    void findById() {
        // when
        Optional<Reservation> actual = reservationRepository.findById(1L);

        // then
        assertThat(actual.isPresent()).isTrue();
    }

    @DisplayName("예약 DAO는 조회 요청이 들어오면 저장한 모든 값을 반환한다.")
    @Test
    void findAll() {
        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations.size()).isEqualTo(2);
    }

    @DisplayName("예약 DAO는 삭제 요청이 들어오면 id에 맞는 값을 삭제한다.")
    @Test
    void deleteById() {
        // given
        Long id = saveInitReservation();

        // when
        reservationRepository.deleteById(id);
        Optional<Reservation> actual = reservationRepository.findById(id);

        // then
        assertThat(actual.isPresent()).isFalse();
    }

    private Long saveInitReservation() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 10));
        Long reservationTimeId = simpleJdbcInsertWithReservationTime.executeAndReturnKey(new BeanPropertySqlParameterSource(reservationTime))
                .longValue();
        ReservationTime newReservationTime = new ReservationTime(reservationTimeId, reservationTime.getStartAt());

        Reservation reservation = new Reservation(
                "브라운",
                LocalDate.of(2024, 11, 16),
                newReservationTime,
                themeFixture
        );
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId());

        return simpleJdbcInsertWithReservation.executeAndReturnKey(sqlParameterSource).longValue();
    }
}
