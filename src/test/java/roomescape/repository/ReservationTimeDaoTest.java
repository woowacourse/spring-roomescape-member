package roomescape.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import roomescape.RepositoryTest;
import roomescape.domain.reservation.ReservationTime;
import roomescape.repository.rowmapper.ReservationTimeRowMapper;

import javax.sql.DataSource;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeDaoTest extends RepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private RowMapper<ReservationTime> rowMapper;
    private ReservationTimeDao timeDao;


    @BeforeEach
    void setUp() {
        rowMapper = new ReservationTimeRowMapper();
        timeDao = new ReservationTimeDao(jdbcTemplate, dataSource, rowMapper);
    }

    @AfterEach
    void clearTable() {
        jdbcTemplate.update("DELETE FROM reservation_time");
    }

    @Test
    @DisplayName("예약 시간을 저장할 수 있다")
    void should_SaveReservationTime() {
        //given
        int expectedSize = 3;
        ReservationTime time = new ReservationTime(null, LocalTime.from(LocalTime.MIDNIGHT));

        //when
        ReservationTime savedTime = timeDao.save(time);

        //then
        String sql = "SELECT count(*) FROM reservation_time";
        int reservationTimeSize = jdbcTemplate.queryForObject(sql, Integer.class);
        assertThat(reservationTimeSize).isEqualTo(expectedSize);
        assertThat(savedTime.getId()).isNotNull();
    }

    @Test
    @DisplayName("중복된 시간을 저장하려 할 경우 예외가 발생한다.")
    void should_ThrowIllegalStateException_When_SaveDuplicateTime() {
        //given
        ReservationTime duplicateTime = new ReservationTime(null, LocalTime.from(LocalTime.of(9, 0, 0)));

        //when-then
        assertThatThrownBy(() -> timeDao.save(duplicateTime))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("[ERROR] 키 값 에러 : 중복된 시간 키가 존재합니다");
    }

    @Test
    @DisplayName("예약 시간을 모두 조회할 수 있다")
    void should_getAllReservationTimes() {
        //given
        int expectedSize = 2;

        //when-then
        assertThat(timeDao.getAll()).hasSize(expectedSize);
    }

    @Test
    @DisplayName("예약 시간 id에 해당하는 시간조회가 가능하다")
    void should_FindTargetTime_WhenGiveId() {
        //given
        long targetId = 1;
        LocalTime expectedTime = LocalTime.of(9, 0, 0);

        //when
        Optional<ReservationTime> foundTime = timeDao.findById(targetId);

        //then
        assertThat(foundTime.isPresent()).isTrue();
        assertThat(foundTime.get().getStartAt()).isEqualTo(expectedTime);
    }

    @Test
    @DisplayName("예약 시간 id를 찾아 시간삭제가 가능하다")
    void should_DeleteTime_WhenGiveId() {
        //given
        long targetId = 1;

        //when
        timeDao.delete(targetId);

        //then
        String sql = "SELECT count(*) FROM reservation_time";
        int reservationSize = jdbcTemplate.queryForObject(sql, Integer.class);
        assertThat(reservationSize).isOne();
    }
}