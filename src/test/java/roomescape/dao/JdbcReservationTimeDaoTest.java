package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcReservationTimeDaoTest {

    @Autowired
    ReservationTimeDao reservationTimeDAO;

    @DisplayName("데이터 베이스에 예약 시간을 추가하고 id 값을 반환한다")
    @Test
    void insertTest() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(16, 0));

        // when
        long result = reservationTimeDAO.insert(reservationTime);

        // then
        assertThat(result).isEqualTo(3L);
    }

    @DisplayName("같은 예약 시간이 존재하면 true를 반환한다")
    @Test
    void existsByStartAtTest() {
        // when
        boolean result = reservationTimeDAO.existsByStartAt(LocalTime.of(10, 0));

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("존재하는 모든 시간을 찾아 반환한다")
    @Test
    void findAllTest() {
        // when
        List<ReservationTime> result = reservationTimeDAO.findAll();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getStartAt()).isEqualTo(LocalTime.of(10, 0));
        assertThat(result.getLast().getStartAt()).isEqualTo(LocalTime.of(12, 0));
    }

    @DisplayName("id에 해당하는 예약 시간을 반환한다")
    @Test
    void findByIdTest() {
        // given
        long id = 1L;

        // when
        Optional<ReservationTime> resultOptional = reservationTimeDAO.findById(id);

        // then
        assertThat(resultOptional).isPresent();
        assertThat(resultOptional.get().getId()).isEqualTo(id);
        assertThat(resultOptional.get().getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @DisplayName("예약에 사용중인 예약 시간을 찾아 반환한다")
    @Test
    void findAllBookedTimeTest() {
        // when
        List<ReservationTime> bookedTimes = reservationTimeDAO.findAllBookedTime(
                LocalDate.of(2025, 4, 28), 1L
        );

        // then
        assertThat(bookedTimes).hasSize(1);
        assertThat(bookedTimes.getFirst().getId()).isEqualTo(1L);
        assertThat(bookedTimes.getFirst().getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @DisplayName("주어진 id에 해당하는 예약 시간을 삭제한다")
    @Test
    void deleteByIdTest() {
        // given
        long id = 2L;

        // when
        boolean result = reservationTimeDAO.deleteById(id);

        // then
        assertThat(result).isTrue();
    }
}
