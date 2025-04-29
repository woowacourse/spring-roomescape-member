package roomescape.reservation.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.config.TestConfig;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.repository.EntityRepository;
import roomescape.domain.reservation.repository.impl.ReservationDAO;
import roomescape.utils.JdbcTemplateUtils;

class ReservationDAOTest {

    private static final Long RESERVATION_TIME_ID = 1L;
    private static final LocalTime RESERVATION_TIME_START_TIME = LocalTime.of(8, 0);

    private JdbcTemplate jdbcTemplate;
    private EntityRepository<Reservation> reservationRepository;

    @BeforeEach
    void init() {
        jdbcTemplate = TestConfig.getJdbcTemplate();
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = TestConfig.getNamedParameterJdbcTemplate();
        reservationRepository = new ReservationDAO(namedParameterJdbcTemplate, TestConfig.getDataSource());
    }

    @DisplayName("예약 정보를 저장한다.")
    @Test
    void test1() {
        // given
        ReservationTime reservationTime = saveReservationTime(RESERVATION_TIME_ID, RESERVATION_TIME_START_TIME);

        LocalDateTime now = LocalDateTime.now();
        String name = "꾹";
        Reservation reservation = Reservation.withoutId(name, now.toLocalDate(), reservationTime);

        // when
        Reservation result = reservationRepository.save(reservation);

        // then
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getReservationDate()).isEqualTo(now.toLocalDate());
        assertThat(result.getReservationTime().getId()).isEqualTo(RESERVATION_TIME_ID);
    }

    @DisplayName("id가 같다면 해당 예약 정보로 변경한다.")
    @Test
    void test4() {
        // given
        long reservationId = 1;
        LocalDateTime now = LocalDateTime.now();

        ReservationTime reservationTime = saveReservationTime(RESERVATION_TIME_ID, now.toLocalTime());

        String originalName = "꾹";
        saveReservation(reservationId, originalName, now.toLocalDate(), RESERVATION_TIME_ID);

        String changedName = "드라고";
        Reservation updateReservation = new Reservation(reservationId, changedName, now.toLocalDate(), reservationTime);

        // when
        Reservation result = reservationRepository.save(updateReservation);

        // then
        assertThat(result).isEqualTo(updateReservation);
    }

    @DisplayName("존재하지 않는 id를 save한다면 예외를 반환한다.")
    @Test
    void test8() {
        // given
        ReservationTime reservationTime = saveReservationTime(RESERVATION_TIME_ID, RESERVATION_TIME_START_TIME);
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation(1L, "꾹", now.toLocalDate(), reservationTime);

        // when & then
        assertThatThrownBy(() -> reservationRepository.save(reservation))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("id로 예약 정보를 가져온다")
    @Test
    void test5() {
        // given

        Long timeId = 1L;
        LocalDateTime now = LocalDateTime.now();

        saveReservationTime(timeId, now.toLocalTime());

        long id = 1;
        String name = "꾹";
        saveReservation(id, name, now.toLocalDate(), timeId);

        // when
        Reservation result = reservationRepository.findById(id).get();

        // then
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getReservationDate()).isEqualTo(now.toLocalDate());
        assertThat(result.getReservationTime().getId()).isEqualTo(timeId);
        assertThat(result.getReservationTime().getStartAt()).isEqualTo(now.toLocalTime());
    }

    @DisplayName("모든 예약 정보를 가져온다.")
    @Test
    void test6() {
        // given
        LocalDate date = LocalDate.now();

        saveReservationTime(RESERVATION_TIME_ID, RESERVATION_TIME_START_TIME);

        List<String> names = List.of("꾹", "헤일러", "라젤");

        String sql = "insert into reservation (name, date, time_id) values (?, ?, ?)";

        for (String name : names) {
            jdbcTemplate.update(sql, name, date, RESERVATION_TIME_ID);
        }

        // when
        List<Reservation> result = reservationRepository.findAll();

        // then

        List<String> resultNames = result.stream().map(Reservation::getName).toList();
        List<LocalDate> resultDates = result.stream().map(Reservation::getReservationDate).toList();
        List<LocalTime> resultTimes = result.stream().map(Reservation::getReservationTime)
                .map(ReservationTime::getStartAt)
                .toList();

        assertThat(resultNames).containsAll(names);

        for (LocalDate resultDate : resultDates) {
            assertThat(resultDate).isEqualTo(date);
        }

        for (LocalTime resultTime : resultTimes) {
            assertThat(resultTime).isEqualTo(RESERVATION_TIME_START_TIME);
        }
    }

    @DisplayName("예약 정보를 삭제한다.")
    @Test
    void test7() {
        // given
        Long reservationId = 2L;
        String name = "꾹";
        LocalDateTime now = LocalDateTime.now();

        saveReservationTime(RESERVATION_TIME_ID, now.toLocalTime());
        saveReservation(reservationId, name, now.toLocalDate(), RESERVATION_TIME_ID);

        // when
        reservationRepository.deleteById(reservationId);

        // then
        String sql = "select count(*) from reservation where id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, reservationId);
        assertThat(count).isZero();
    }

    private ReservationTime saveReservationTime(Long id, LocalTime time) {
        String insertTimeSql = "insert into reservation_time (id, start_at) values (?, ?)";
        jdbcTemplate.update(insertTimeSql, id, time);

        return new ReservationTime(id, time);
    }

    private void saveReservation(Long id, String name, LocalDate date, Long timeId) {
        String sql = "insert into reservation (id, name, date, time_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, id, name, date, timeId);
    }

    @AfterEach
    void cleanUp() {
        JdbcTemplateUtils.deleteAllTables(jdbcTemplate);
    }
}
