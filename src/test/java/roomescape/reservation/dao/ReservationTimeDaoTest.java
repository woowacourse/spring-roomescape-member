package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dao.reservation.ReservationTimeDao;
import roomescape.domain.reservation.ReservationTime;

@JdbcTest
@Import(ReservationTimeDao.class)
@Sql(scripts = {"/test_schema.sql"})
class ReservationTimeDaoTest {

    @Autowired
    private ReservationTimeDao timeDao;

    @DisplayName("시간을 추가한다.")
    @Test
    void insert() {
        // given
        ReservationTime inserted = timeDao.insert(new ReservationTime(null, LocalTime.parse("10:00")));

        // when
        Long insertedTimeId = inserted.getId();

        // then
        assertThat(insertedTimeId).isEqualTo(1L);
    }

    @DisplayName("모든 시간을 조회한다.")
    @Test
    void findAll() {
        // given
        timeDao.insert(new ReservationTime(null, LocalTime.parse("10:00")));
        timeDao.insert(new ReservationTime(null, LocalTime.parse("11:00")));

        // when
        List<ReservationTime> times = timeDao.findAll();

        // then
        assertThat(times).extracting("startAt").containsExactly(LocalTime.parse("10:00"), LocalTime.parse("11:00"));
    }

    @DisplayName("ID를 이용하여 시간을 조회한다.")
    @Test
    void findById() {
        // given
        ReservationTime inserted = timeDao.insert(new ReservationTime(null, LocalTime.parse("10:00")));

        // when
        ReservationTime time = timeDao.findById(inserted.getId()).orElseThrow();

        // then
        assertThat(inserted.getStartAt()).isEqualTo(time.getStartAt());
    }

    @DisplayName("ID가 존재하지 않으면 빈 시간 객체를 반환한다.")
    @Test
    void findByNotExistId() {
        Optional<ReservationTime> time = timeDao.findById(2L);

        assertThat(time).isEmpty();
    }


    @DisplayName("ID를 이용하여 예약을 삭제한다.")
    @Test
    void deleteById() {
        // given
        ReservationTime inserted = timeDao.insert(new ReservationTime(null, LocalTime.parse("10:00")));

        // when
        timeDao.deleteById(inserted.getId());
        Optional<ReservationTime> time = timeDao.findById(inserted.getId());

        // then
        assertThat(time).isEmpty();
    }

    @DisplayName("동일한 시간이 존재하는지 확인한다.")
    @Test
    void hasSameTime() {
        // given
        timeDao.insert(new ReservationTime(null, LocalTime.parse("10:00")));

        // when
        Boolean hasSameTime = timeDao.hasSameTime(LocalTime.parse("10:00"));

        // then
        assertThat(hasSameTime).isTrue();
    }
}
