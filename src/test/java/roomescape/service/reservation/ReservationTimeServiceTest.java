package roomescape.service.reservation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.reservation.ReservationTimeRequest;
import roomescape.dto.reservation.ReservationTimeResponse;
import roomescape.exceptions.EntityNotFoundException;
import roomescape.exceptions.reservation.ReservationTimeDuplicateException;

@SpringBootTest
@Transactional
@Sql({"/fixtures/schema-test.sql", "/fixtures/reservationTime.sql"})
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationService;

    @Test
    @DisplayName("조회된 엔티티를 DTO로 매핑해 반환한다.")
    void getReservationTimes() {
        //given & when
        List<ReservationTimeResponse> actual = reservationService.getReservationTimes();
        //then
        assertThat(actual.size()).isEqualTo(15);
        assertThat(actual.getFirst().startAt()).isEqualTo(LocalTime.of(9, 0));
    }

    @Test
    @DisplayName("엔티티를 저장한 후, DTO로 반환한다.")
    void addReservationTime() {
        //given
        LocalTime time = LocalTime.MIN;
        ReservationTimeRequest request = new ReservationTimeRequest(time);
        //when
        ReservationTimeResponse actual = reservationService.addReservationTime(request);
        //then
        assertThat(actual.startAt()).isEqualTo(time);
    }

    @Test
    @DisplayName("예약 시간을 성공적으로 삭제한다.")
    void successfulDeleteReservationTime() {
        // given
        Long existingId = 15L;

        // when
        reservationService.deleteReservationTime(existingId);

        // then
        assertThatThrownBy(() -> reservationService.deleteReservationTime(existingId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("저장되지 않은 값을 삭제하려할 경우, 예외가 발생한다.")
    void deleteReservationTime() {
        assertThatThrownBy(() -> reservationService.deleteReservationTime(999L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("예약 시간 생성 시, 중복된 시간일 경우 예외가 발생한다.")
    void addReservationTimeIfDuplicationDatetime() {
        //given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(14, 0));
        //when&then
        assertThatThrownBy(() -> reservationService.addReservationTime(request))
                .isInstanceOf(ReservationTimeDuplicateException.class)
                .hasMessageContaining("중복된 예약 시간이 존재합니다.");
    }
}
