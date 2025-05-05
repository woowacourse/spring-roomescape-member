package roomescape.domain.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.exception.ReservationException.ReservationThemeInUseException;
import roomescape.domain.repository.ReservationRepository;

@ExtendWith(MockitoExtension.class)
class ReservationThemeValidationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationThemeValidationService reservationThemeValidationService;

    @DisplayName("해당 테마를 사용중인 예약이 있다면 예외를 발생시킨다")
    @Test
    void validateNotInUse() {
        // given
        Long themeId = 1L;
        given(reservationRepository.existsByThemeId(themeId)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationThemeValidationService.validateNotInUse(themeId))
                .isInstanceOf(ReservationThemeInUseException.class);
    }
}
