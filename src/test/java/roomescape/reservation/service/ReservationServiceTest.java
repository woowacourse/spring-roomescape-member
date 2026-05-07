package roomescape.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.ReservationTimeService;

import java.time.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private static final LocalDate FUTURE_DATE = LocalDate.of(2026, 12, 31);
    private static final LocalTime START_AT = LocalTime.of(10, 0);
    private static final Long TIME_ID = 1L;
    private static final Long THEME_ID = 1L;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeService reservationTimeService;

    @Mock
    private ThemeService themeService;

    private ReservationService reservationService;

    private ReservationTime reservationTime;
    private Theme theme;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(
                LocalDateTime.of(2026, 5, 6, 9, 0).atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
        reservationService = new ReservationService(
                reservationRepository,
                reservationTimeService,
                themeService,
                fixedClock
        );

        reservationTime = new ReservationTime(TIME_ID, START_AT);
        theme = new Theme(THEME_ID, "우테코", "우테코 전용 테마", "https://example.com");
    }

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("정상 요청이면 예약을 저장하고 저장된 예약을 반환한다.")
        void saveSuccess() {
            // given
            ReservationRequest request = new ReservationRequest("브라운", FUTURE_DATE, TIME_ID, THEME_ID);

            given(reservationTimeService.getById(TIME_ID)).willReturn(reservationTime);
            given(themeService.getById(THEME_ID)).willReturn(theme);
            given(reservationRepository.existsByDateAndTimeIdAndThemeId(FUTURE_DATE, TIME_ID, THEME_ID))
                    .willReturn(false);

            Reservation saved = new Reservation(99L, "브라운", FUTURE_DATE, reservationTime, theme);
            given(reservationRepository.save(any(Reservation.class))).willReturn(saved);

            // when
            Reservation result = reservationService.save(request);

            // then
            assertThat(result.getId()).isEqualTo(99L);
            assertThat(result.getName()).isEqualTo("브라운");
            verify(reservationRepository, times(1)).save(any(Reservation.class));
        }

        @Test
        @DisplayName("예약 일시가 현재보다 과거이면 예외가 발생하고 저장되지 않는다.")
        void saveFailWhenPast() {
            // given
            LocalDate pastDate = LocalDate.of(2026, 5, 5);
            ReservationRequest request = new ReservationRequest("브라운", pastDate, TIME_ID, THEME_ID);

            given(reservationTimeService.getById(TIME_ID)).willReturn(reservationTime);
            given(themeService.getById(THEME_ID)).willReturn(theme);

            // when & then
            assertThatThrownBy(() -> reservationService.save(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("과거 시각");

            verify(reservationRepository, never()).save(any(Reservation.class));
        }

        @Test
        @DisplayName("동일한 날짜/시간/테마 조합의 예약이 이미 존재하면 예외가 발생한다.")
        void saveFailWhenDuplicate() {
            // given
            ReservationRequest request = new ReservationRequest("브라운", FUTURE_DATE, TIME_ID, THEME_ID);

            given(reservationTimeService.getById(TIME_ID)).willReturn(reservationTime);
            given(themeService.getById(THEME_ID)).willReturn(theme);
            given(reservationRepository.existsByDateAndTimeIdAndThemeId(FUTURE_DATE, TIME_ID, THEME_ID))
                    .willReturn(true);

            // when & then
            assertThatThrownBy(() -> reservationService.save(request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("이미 해당 날짜와 시간에 예약이 존재합니다.");

            verify(reservationRepository, never()).save(any(Reservation.class));
        }

        @Test
        @DisplayName("오늘 날짜라도 예약 시간이 현재 시각보다 이전이면 예외가 발생한다.")
        void saveFailWhenTodayButPastTime() {
            // given
            LocalDate today = LocalDate.of(2026, 5, 6);
            LocalTime pastTimeAt = LocalTime.of(8, 0);
            ReservationTime pastReservationTime = new ReservationTime(TIME_ID, pastTimeAt);

            ReservationRequest request = new ReservationRequest("브라운", today, TIME_ID, THEME_ID);

            given(reservationTimeService.getById(TIME_ID)).willReturn(pastReservationTime);
            given(themeService.getById(THEME_ID)).willReturn(theme);

            // when & then
            assertThatThrownBy(() -> reservationService.save(request))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(reservationRepository, never()).save(any(Reservation.class));
        }

        @Test
        @DisplayName("정상 요청 시 Repository에 저장되는 객체의 필드 값을 검증한다.")
        void saveSuccessDetail() {
            // given
            ReservationRequest request = new ReservationRequest("브라운", FUTURE_DATE, TIME_ID, THEME_ID);
            given(reservationTimeService.getById(TIME_ID)).willReturn(reservationTime);
            given(themeService.getById(THEME_ID)).willReturn(theme);
            given(reservationRepository.existsByDateAndTimeIdAndThemeId(any(), any(), any())).willReturn(false);

            given(reservationRepository.save(any(Reservation.class)))
                    .willAnswer(invocation -> invocation.getArgument(0));

            // when
            Reservation result = reservationService.save(request);

            // then
            assertThat(result.getName()).isEqualTo("브라운");
            assertThat(result.getDate()).isEqualTo(FUTURE_DATE);
            assertThat(result.getTime()).isEqualTo(reservationTime);
            assertThat(result.getTheme()).isEqualTo(theme);
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById {

        @Test
        @DisplayName("ID에 해당하는 예약 삭제를 Repository에 위임한다.")
        void deleteByIdDelegatesToRepository() {
            // when
            reservationService.deleteById(1L);

            // then
            verify(reservationRepository, times(1)).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("deleteAll 메서드는")
    class DeleteAll {

        @Test
        @DisplayName("모든 예약 삭제를 Repository에 위임한다.")
        void deleteAllDelegatesToRepository() {
            // when
            reservationService.deleteAll();

            // then
            verify(reservationRepository, times(1)).deleteAll();
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @Test
        @DisplayName("Repository가 반환한 예약 목록을 그대로 돌려준다.")
        void findAllReturnsRepositoryResult() {
            // given
            Reservation reservation = new Reservation(1L, "브라운", FUTURE_DATE, reservationTime, theme);
            given(reservationRepository.findAll()).willReturn(List.of(reservation));

            // when
            List<Reservation> result = reservationService.findAll();

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("브라운");
        }

        @Test
        @DisplayName("예약이 없으면 빈 목록을 반환한다.")
        void findAllReturnsEmpty() {
            // given
            given(reservationRepository.findAll()).willReturn(List.of());

            // when
            List<Reservation> result = reservationService.findAll();

            // then
            assertThat(result).isEmpty();
        }
    }
}
