package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.repository.rowmapper.ReservationTimeRowMapper;

@TestExecutionListeners(value = {
        DatabaseCleanupListener.class,
        DependencyInjectionTestExecutionListener.class
})
@JdbcTest
class JdbcReservationTimeRepositoryTest {

    @Autowired
    private DataSource dataSource;
    private ReservationTimeRepository reservationTimeRepository;

    private final ReservationTime time1 = new ReservationTime(null, "20:20");
    private final ReservationTime time2 = new ReservationTime(null, "21:21");

    @BeforeEach
    void setUp() {
        RowMapper<ReservationTime> rowMapper = new ReservationTimeRowMapper();
        reservationTimeRepository = new JdbcReservationTimeRepository(dataSource, rowMapper);
    }

    @DisplayName("저장된 모든 예약 시간 정보를 가져온다.")
    @Test
    void find_all_reservation_times() {
        reservationTimeRepository.insertReservationTime(time1);
        reservationTimeRepository.insertReservationTime(time2);

        List<ReservationTime> allReservationTimes = reservationTimeRepository.findAllReservationTimes();

        assertThat(allReservationTimes.size()).isEqualTo(2);
    }

    @DisplayName("예약 시간을 저장한다.")
    @Test
    void save_reservation_time() {
        ReservationTime time = reservationTimeRepository.insertReservationTime(time1);

        assertAll(
                () -> assertThat(time.getId()).isEqualTo(1),
                () -> assertThat(time.getStartAt()).isEqualTo("20:20")
        );
    }

    @Test
    @DisplayName("예약 시간을 id로 삭제한다.")
    void delete_reservation_time_by_id() {
        reservationTimeRepository.insertReservationTime(time1);
        int beforeSize = reservationTimeRepository.findAllReservationTimes().size();

        reservationTimeRepository.deleteReservationTimeById(1L);
        int afterSize = reservationTimeRepository.findAllReservationTimes().size();

        assertAll(
                () -> assertThat(beforeSize).isEqualTo(1),
                () -> assertThat(afterSize).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("테이블에 시간 존재 여부를 판단한다.")
    void is_Exist_reservation_time() {
        reservationTimeRepository.insertReservationTime(time1);

        boolean existOfTime = reservationTimeRepository.isExistTimeOf(time1.getStartAt().toString());
        boolean existOfId = reservationTimeRepository.isExistTimeOf(1L);
        boolean notExistOfTime = reservationTimeRepository.isExistTimeOf(time2.getStartAt().toString());
        boolean notExistOfId = reservationTimeRepository.isExistTimeOf(2L);

        assertAll(
                () -> assertThat(existOfTime).isTrue(),
                () -> assertThat(existOfId).isTrue(),
                () -> assertThat(notExistOfTime).isFalse(),
                () -> assertThat(notExistOfId).isFalse()
        );
    }
}
