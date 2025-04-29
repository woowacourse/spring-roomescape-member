package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.entity.ReservationTime;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationTimeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;


    @Test
    @DisplayName("전체 시간 목록을 가져온다.")
    void getAllReservationTimesTest() {
        //given
        // when
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(reservationTimes).hasSize(5);
            softAssertions.assertThat(reservationTimes.getFirst().getStartAt()).isEqualTo(LocalTime.of(10, 0));
        });
    }

    @Test
    @DisplayName("시간을 추가한다.")
    void addReservationTimeTest() {
        // given
        ReservationTime time = new ReservationTime(LocalTime.of(20, 0));
        // when
        ReservationTime reservationTime = reservationTimeRepository.save(time);
        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(reservationTime.getId()).isEqualTo(6);
            softAssertions.assertThat(reservationTime.getStartAt()).isEqualTo(LocalTime.of(20, 0));
        });
    }

    @Test
    @DisplayName("아이디를 통해 예약 시간을 삭제한다")
    void deleteReservationTimeById() {
        // given
        Long id = 1L;

        // when
        int row = reservationTimeRepository.deleteById(id);

        // then
        Assertions.assertThat(row).isEqualTo(1);
    }

}
