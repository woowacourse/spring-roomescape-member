package roomescape.cycle2;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequestDto;
import roomescape.exception.CustomException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.ReservationService;

@ExtendWith(MockitoExtension.class)
class ReservationServiceErrorTest {

    private static final Long TIME_ID = 1L;
    private static final Long THEME_ID = 1L;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ReservationService reservationService;

    private final Theme theme = new Theme(THEME_ID, "테마", "테마 설명", "thumbnail.png");

    @Nested
    class 과거_예약_생성_거부 {

        @Test
        void 지나간_날짜로_예약_시_예외() {
            ReservationTime reservationTime = new ReservationTime(TIME_ID, LocalTime.of(10, 0));
            when(reservationTimeRepository.findById(TIME_ID)).thenReturn(Optional.of(reservationTime));

            LocalDate pastDate = LocalDate.now().minusDays(1);
            ReservationRequestDto request = new ReservationRequestDto("예약자", pastDate, TIME_ID, THEME_ID);

            assertThatThrownBy(() -> reservationService.create(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("오늘보다 이전 날짜로 예약할 수 없습니다.");
        }

        @Test
        void 오늘_지나간_시간으로_예약_시_예외() {
            // LocalTime.MIN(00:00:00) 은 자정 직후를 제외한 모든 시각보다 이전이므로 wrap-around 가 없다.
            ReservationTime reservationTime = new ReservationTime(TIME_ID, LocalTime.MIN);
            when(reservationTimeRepository.findById(TIME_ID)).thenReturn(Optional.of(reservationTime));

            ReservationRequestDto request = new ReservationRequestDto(
                    "예약자", LocalDate.now(), TIME_ID, THEME_ID);

            assertThatThrownBy(() -> reservationService.create(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("현재 시각보다 이전 시간으로 예약할 수 없습니다.");
        }
    }

    @Nested
    class 중복_예약_거부 {

        @Test
        void 중복_예약_시_예외() {
            LocalDate futureDate = LocalDate.now().plusDays(1);
            ReservationTime reservationTime = new ReservationTime(TIME_ID, LocalTime.of(10, 0));

            when(reservationTimeRepository.findById(TIME_ID)).thenReturn(Optional.of(reservationTime));
            when(themeRepository.findById(THEME_ID)).thenReturn(Optional.of(theme));
            when(reservationRepository.existsByDateAndTimeIdAndThemeId(futureDate, TIME_ID, THEME_ID)).thenReturn(true);

            ReservationRequestDto request = new ReservationRequestDto(
                    "예약자", futureDate, TIME_ID, THEME_ID);

            assertThatThrownBy(() -> reservationService.create(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("동일한 예약이 이미 존재합니다.");
        }
    }

    @Nested
    class 유효하지_않은_입력값_거부 {

        @Test
        void 빈_이름으로_예약_시_예외() {
            LocalDate futureDate = LocalDate.now().plusDays(1);
            ReservationTime reservationTime = new ReservationTime(TIME_ID, LocalTime.of(10, 0));

            when(reservationTimeRepository.findById(TIME_ID)).thenReturn(Optional.of(reservationTime));
            when(themeRepository.findById(THEME_ID)).thenReturn(Optional.of(theme));
            when(reservationRepository.existsByDateAndTimeIdAndThemeId(futureDate, TIME_ID, THEME_ID)).thenReturn(
                    false);

            ReservationRequestDto request = new ReservationRequestDto(
                    "", futureDate, TIME_ID, THEME_ID);

            assertThatThrownBy(() -> reservationService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 이름은 비어 있을 수 없습니다.");
        }
    }
}
