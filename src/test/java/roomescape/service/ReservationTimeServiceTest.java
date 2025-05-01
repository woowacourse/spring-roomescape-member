package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.exceptions.EntityNotFoundException;
import roomescape.fake.ReservationTimeFakeRepository;
import roomescape.repository.ReservationTimeRepository;

public class ReservationTimeServiceTest {

    private final ReservationTimeRepository reservationTimeRepository = new ReservationTimeFakeRepository();
    private final ReservationTimeService reservationService = new ReservationTimeService(reservationTimeRepository);

    @Test
    @DisplayName("조회된 엔티티를 DTO로 매핑해 반환한다.")
    void test_readAllReservationTime() {
        //given & when
        List<ReservationTimeResponse> actual = reservationService.readAllReservationTime();
        //then
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.getFirst().startAt()).isEqualTo(LocalTime.MAX);
    }

    @Test
    @DisplayName("엔티티를 저장한 후, DTO로 반환한다.")
    void test_postReservationTime() {
        //given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.MAX);
        //when
        ReservationTimeResponse actual = reservationService.postReservationTime(request);
        //then
        assertThat(actual.startAt()).isEqualTo(LocalTime.MAX);
    }

    @Test
    @DisplayName("저장소에 없는 값을 삭제하려할 경우, 예외가 발생한다.")
    void test_deleteReservationTime() {
        assertThatThrownBy(() -> reservationService.deleteReservationTime(999L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
