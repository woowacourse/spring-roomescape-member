package roomescape.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.ReservationCreateCommand;
import roomescape.service.exception.PastReservationException;
import roomescape.service.exception.ReservationTimeNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserReservationServiceTest {

    private static final ReservationTime VALID_TIME = new ReservationTime(1L, LocalTime.of(10, 0));
    private static final LocalDate FUTURE_DATE = LocalDate.of(2099, 12, 31);
    private static final LocalDate PAST_DATE = LocalDate.of(2020, 1, 1);

    @Mock
    private AdminReservationService reservationService;
    @Mock
    private ReservationTimeRepository reservationTimeRepository;
    @InjectMocks
    private UserReservationService userReservationService;

    @Test
    @DisplayName("미래 시점에 예약하면 정상적으로 생성된다")
    void 미래_시점_예약은_정상_생성된다() {
        ReservationCreateCommand command = new ReservationCreateCommand("브라운", FUTURE_DATE, 1L, 1L);
        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(VALID_TIME));

        assertDoesNotThrow(() -> userReservationService.create(command));
    }

    @Test
    @DisplayName("과거 날짜로 예약하면 PastReservationException이 발생한다")
    void 과거_날짜_예약시_예외가_발생한다() {
        ReservationCreateCommand command = new ReservationCreateCommand("브라운", PAST_DATE, 1L, 1L);
        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(VALID_TIME));

        assertThrows(
                PastReservationException.class,
                () -> userReservationService.create(command)
        );
    }

    @Test
    @DisplayName("존재하지 않는 timeId로 예약하면 ReservationTimeNotFoundException이 발생한다")
    void 존재하지_않는_timeId로_예약시_예외가_발생한다() {
        ReservationCreateCommand command = new ReservationCreateCommand("브라운", FUTURE_DATE, 1L, 1L);
        given(reservationTimeRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(
                ReservationTimeNotFoundException.class,
                () -> userReservationService.create(command)
        );
    }
}
