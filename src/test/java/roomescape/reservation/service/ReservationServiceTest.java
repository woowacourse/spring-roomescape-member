package roomescape.reservation.service;

import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.repository.ReservationRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    ReservationService reservationService;

    @Test
    void 삭제_요청_시_이미_삭제된_예약인_경우_예외가_발생한다() {
        // given
        Long id = 1L;
        when(reservationRepository.deleteById(id)).thenReturn(0);

        // when & then
        Assertions.assertThatThrownBy(() -> reservationService.cancelReservation(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 식별자에_해당하는_예약을_삭제한다() {
        // given
        Long id = 1L;
        when(reservationRepository.deleteById(id)).thenReturn(1);

        // when & then
        Assertions.assertThatCode(() -> reservationService.cancelReservation(id))
                .doesNotThrowAnyException();
    }
}
