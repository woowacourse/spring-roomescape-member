package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.exceptions.EntityNotFoundException;
import roomescape.fake.ReservationFakeRepository;
import roomescape.fake.ReservationTimeFakeRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

public class ReservationServiceTest {

    private final ReservationRepository reservationRepository = new ReservationFakeRepository();
    private final ReservationTimeRepository reservationTimeRepository = new ReservationTimeFakeRepository();
    private final ReservationService reservationService = new ReservationService(reservationRepository,
            reservationTimeRepository);

    @Test
    @DisplayName("조회된 엔티티를 DTO로 매핑해 반환한다.")
    void test_readReservation() {
        //given & when
        List<ReservationResponse> actual = reservationService.readReservation();
        //then
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.getFirst().id()).isEqualTo(1);
    }

    @Test
    @DisplayName("저장한 엔티티를 DTO로 반환한다.")
    void test_postReservation() {
        //given
        List<ReservationResponse> given = reservationService.readReservation();
        assertThat(given.size()).isEqualTo(1);

        //when
        long timeId = 1L;
        ReservationRequest request = new ReservationRequest("브라운", LocalDate.MAX, timeId);
        ReservationResponse actual = reservationService.postReservation(request);
        //then
        assertThat(actual.id()).isEqualTo(2);
    }

    @Test
    @DisplayName("저장소에 없는 값을 삭제하려할 경우, 예외가 발생한다.")
    void test_deleteReservation() {
        assertThatThrownBy(() -> reservationService.deleteReservation(Long.MAX_VALUE))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
