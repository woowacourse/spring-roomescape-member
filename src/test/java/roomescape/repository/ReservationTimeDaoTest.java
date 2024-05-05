package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.model.ReservationTime;
import roomescape.repository.dao.ReservationTimeDao;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    private SimpleJdbcInsert insertActor;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time RESTART IDENTITY");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");

        insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
        insertToReservationTime("10:00");
        insertToReservationTime("11:00");
    }

    private void insertToReservationTime(String startAt) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("start_at", startAt);
        insertActor.execute(parameters);
    }

    @DisplayName("모든 예약 시간을 조회한다")
    @Test
    void should_get_reservation_times() {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        assertThat(reservationTimes).hasSize(2);
    }

    @DisplayName("예약 시간을 추가한다")
    @Test
    void should_add_reservation_time() {
        reservationTimeDao.save(new ReservationTime(LocalTime.of(12, 0)));
        Integer count = jdbcTemplate.queryForObject("select count(1) from reservation_time", Integer.class);
        assertThat(count).isEqualTo(3);
    }

    @DisplayName("예약 시간을 삭제한다")
    @Test
    void should_delete_reservation_time() {
        reservationTimeDao.deleteById(1);
        Integer count = jdbcTemplate.queryForObject("select count(1) from reservation_time", Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("아이디에 해당하는 예약 시간을 조회한다.")
    @Test
    void should_get_reservation_time() {
        ReservationTime reservationTime = reservationTimeDao.findById(1).orElse(null);
        assertThat(reservationTime.getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @DisplayName("특정 id를 갖는 데이터가 존재하는 경우 참을 반환한다.")
    @Test
    void should_return_true_when_exist() {
        Boolean isExist = reservationTimeDao.isExistById(1);
        assertThat(isExist).isTrue();
    }

    @DisplayName("특정 id를 갖는 데이터가 존재하지 않는 경우 거짓을 반환한다.")
    @Test
    void should_return_false_when_not_exist() {
        Boolean isExist = reservationTimeDao.isExistById(100000000);
        assertThat(isExist).isFalse();
    }
}
