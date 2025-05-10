package roomescape.reservation.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.repository.JdbcReservationTimeRepository;
import roomescape.reservation.repository.ReservationTimeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeRepositoryImplTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void beforeEach() {
        reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
    }

//    @Test
//    @DisplayName("모든 시간을 조회한다.")
//    void findAll() {
//        // given
//        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
//
//        // when
//        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
//
//        // then
//        ReservationTime expected = new ReservationTime(1L, LocalTime.of(10, 0));
//        assertThat(reservationTimes.size()).isEqualTo(1);
//        assertThat(reservationTimes.getFirst()).isEqualTo(expected);
//    }
//
//    @Test
//    @DisplayName("id로 시간을 조회한다.")
//    void findById() {
//        // given
//        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
//
//        // when
//        Optional<ReservationTime> time = reservationTimeRepository.findById(1L);
//
//        // then
//        ReservationTime expected = new ReservationTime(1L, LocalTime.of(10, 0));
//        assertThat(time.get()).isEqualTo(expected);
//    }
//
//    @Test
//    @DisplayName("startAt이 이미 있는 시간이면 True를 반환한다.")
//    void findByStartAt_true() {
//        // given
//        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
//
//        // when
//        boolean exists = reservationTimeRepository.findByStartAt(LocalTime.of(10, 0));
//
//        // then
//        assertThat(exists).isTrue();
//    }
//
//    @Test
//    @DisplayName("startAt이 없는 시간이면 False를 반환한다.")
//    void findByStartAt_false() {
//        // given
//        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
//
//        // when
//        boolean exists = reservationTimeRepository.findByStartAt(LocalTime.of(10, 10));
//
//        // then
//        assertThat(exists).isFalse();
//    }
//
//    @Test
//    @DisplayName("시간을 저장한다.")
//    void save() {
//        // given
//        ReservationTime reservationTime = new ReservationTime(
//                null,
//                LocalTime.of(10, 0)
//        );
//
//        // when
//        ReservationTime saved = reservationTimeRepository.save(reservationTime);
//
//        // then
//        ReservationTime found = jdbcTemplate.queryForObject(
//                "select id, start_at from reservation_time where id = ?",
//                (resultSet, rowNum) ->
//                        new ReservationTime(
//                                resultSet.getLong("id"),
//                                LocalTime.parse(resultSet.getString("start_at"))
//                        ),
//                1L);
//        assertThat(found.getId()).isEqualTo(1L);
//        assertThat(found.getStartAt()).isEqualTo(reservationTime.getStartAt());
//        assertThat(found).isEqualTo(saved);
//    }
//
//    @Test
//    @DisplayName("id로 시간을 삭제한다.")
//    void deleteById() {
//        // given
//        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
//
//        // when
//        reservationTimeRepository.deleteById(1L);
//
//        // then
//        List<ReservationTime> reservationTimes = jdbcTemplate.query(
//                "select id, start_at from reservation_time",
//                (resultSet, rowNum) -> new ReservationTime(
//                        resultSet.getLong("id"),
//                        LocalTime.parse(resultSet.getString("start_at"))
//                )
//        );
//        assertThat(reservationTimes.size()).isEqualTo(0);
//    }
}
