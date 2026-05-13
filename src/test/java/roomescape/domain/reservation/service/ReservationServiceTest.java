package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.common.exception.BusinessException;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.exception.ReservationErrorCode;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.request.ReservationCreateRequest;
import roomescape.domain.reservation.request.ReservationUpdateRequest;
import roomescape.domain.reservation.response.ReservationResponse;
import roomescape.domain.reservation.response.ReservationsResponse;
import roomescape.domain.reservationtime.entity.ReservationTime;
import roomescape.domain.reservationtime.exception.TimeErrorCode;
import roomescape.domain.reservationtime.repository.ReservationTimeRepository;
import roomescape.domain.reservationtime.response.ReservationTimeResponse;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.theme.response.ThemeResponse;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private static final ZoneId ZONE_ID = ZoneId.systemDefault();
    private static final LocalDate FIXED_DATE = LocalDate.of(2026, 5, 6);
    private static final LocalDate PAST_DATE = FIXED_DATE.minusDays(1);
    private static final LocalDate FUTURE_DATE = FIXED_DATE.plusDays(1);

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ReservationTimeRepository reservationTimeRepository;

    @Mock
    ThemeRepository themeRepository;

    ReservationService reservationService;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(
                FIXED_DATE.atTime(14, 0).atZone(ZONE_ID).toInstant(),
                ZONE_ID
        );

        reservationService = new ReservationService(
                reservationRepository,
                reservationTimeRepository,
                themeRepository,
                fixedClock
        );
    }

    @Test
    @DisplayName("모든 예약을 조회한다.")
    void findAllReservations() {
        // given
        List<Reservation> reservations = new ArrayList<>();
        Theme theme = Theme.of(1L, "theme1", "description1", "thumbnail url 1");
        ReservationTime time1 = ReservationTime.of(1L, LocalTime.of(10, 0));
        ReservationTime time2 = ReservationTime.of(2L, LocalTime.of(11, 0));

        reservations.add(Reservation.of(1L, "브라운", theme, LocalDate.of(2026, 4, 30), time1));
        reservations.add(Reservation.of(2L, "크루", theme, LocalDate.of(2026, 4, 30), time2));
        when(reservationRepository.findAll()).thenReturn(reservations);

        // when
        ReservationsResponse foundReservations = reservationService.findAllReservations();

        // then
        assertThat(foundReservations.reservations()).hasSize(2)
                .extracting("username", "theme", "date", "time")
                .containsExactly(
                        tuple(
                                "브라운",
                                ThemeResponse.from(theme),
                                LocalDate.of(2026, 4, 30),
                                ReservationTimeResponse.from(time1)
                        ),
                        tuple(
                                "크루",
                                ThemeResponse.from(theme),
                                LocalDate.of(2026, 4, 30),
                                ReservationTimeResponse.from(time2)
                        )
                );

        verify(reservationRepository).findAll();
    }

    @Test
    @DisplayName("사용자 이름으로 예약을 조회한다.")
    void findAllByUsernameTest() {
        // given
        String username = "브라운";
        Theme theme = Theme.of(1L, "theme1", "description1", "thumbnail url 1");
        ReservationTime time1 = ReservationTime.of(1L, LocalTime.of(10, 0));
        ReservationTime time2 = ReservationTime.of(2L, LocalTime.of(11, 0));
        Reservation reservation1 = Reservation.of(1L, username, theme, LocalDate.of(2026, 4, 30), time1);
        Reservation reservation2 = Reservation.of(2L, username, theme, LocalDate.of(2026, 4, 30), time2);
        List<Reservation> reservations = List.of(reservation1, reservation2);

        when(reservationRepository.findAllByUsername(username)).thenReturn(reservations);

        // when
        ReservationsResponse foundReservations = reservationService.findMyReservations(username);

        // then
        assertThat(foundReservations.reservations()).hasSize(2)
                .extracting("username", "theme", "date", "time")
                .containsExactly(
                        tuple(
                                username,
                                ThemeResponse.from(theme),
                                LocalDate.of(2026, 4, 30),
                                ReservationTimeResponse.from(time1)
                        ),
                        tuple(
                                username,
                                ThemeResponse.from(theme),
                                LocalDate.of(2026, 4, 30),
                                ReservationTimeResponse.from(time2)
                        )
                );

        verify(reservationRepository).findAllByUsername(username);
    }

    @Test
    @DisplayName("사용자가 예약을 성공적으로 생성한다.")
    void saveReservationByUser() {
        // given
        ReservationTime reservationTime = ReservationTime.of(1L, LocalTime.of(10, 0));
        when(reservationTimeRepository.findById(eq(1L)))
                .thenReturn(Optional.of(reservationTime));

        Theme theme = Theme.create("theme1", "description1", "thumbnail url 1");
        when(themeRepository.findById(eq(1L)))
                .thenReturn(Optional.of(theme));

        Reservation reservation = Reservation.of(
                1L,
                "브라운",
                theme,
                LocalDate.of(2026, 5, 8),
                reservationTime
        );
        when(reservationRepository.save(any(Reservation.class)))
                .thenReturn(reservation);

        ReservationCreateRequest request = new ReservationCreateRequest(
                "브라운",
                1L,
                LocalDate.of(2026, 5, 8),
                1L
        );

        // when
        ReservationResponse response = reservationService.saveReservationByUser(request);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.username()).isEqualTo("브라운");
        assertThat(response.theme().name()).isEqualTo("theme1");
        assertThat(response.date()).isEqualTo(LocalDate.of(2026, 5, 8));
        assertThat(response.time().id()).isEqualTo(1L);

        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    @DisplayName("존재하지 않는 테마로 예약 시 예외가 발생한다.")
    void saveReservation_ByUser_throwsException_whenThemeNotFound() {
        // given
        Long invalidThemeId = 999L;
        ReservationCreateRequest request = new ReservationCreateRequest("브라운", invalidThemeId, FIXED_DATE.plusDays(1),
                1L);

        when(reservationTimeRepository.findById(anyLong())).thenReturn(
                Optional.of(ReservationTime.of(1L, LocalTime.of(10, 0))));
        when(themeRepository.findById(invalidThemeId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservationByUser(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(roomescape.domain.theme.exception.ThemeErrorCode.THEME_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 시간으로 예약 시 예외가 발생한다.")
    void saveReservation_ByUser_throwsException_whenTimeNotFound() {
        // given
        Long invalidTimeId = 999L;

        ReservationCreateRequest request = new ReservationCreateRequest(
                "브라운",
                1L,
                LocalDate.of(2026, 5, 8),
                invalidTimeId
        );

        when(reservationTimeRepository.findById(invalidTimeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservationByUser(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(TimeErrorCode.RESERVATION_TIME_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("지나간 날짜에 대한 예약 생성은 불가능하다.")
    void saveReservationByUserWithPastDateThrowException() {
        // given
        Long timeId = 1L;
        Long themeId = 1L;
        LocalDate pastDate = FIXED_DATE.minusDays(1);
        ReservationCreateRequest request = new ReservationCreateRequest(
                "브라운",
                themeId,
                pastDate,
                timeId
        );

        when(reservationTimeRepository.findById(timeId))
                .thenReturn(Optional.of(ReservationTime.of(timeId, LocalTime.of(10, 0))));

        when(themeRepository.findById(themeId))
                .thenReturn(Optional.of(Theme.of(themeId, "theme", "desc", "url")));

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservationByUser(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ReservationErrorCode.PAST_RESERVATION.getMessage());
    }

    @Test
    @DisplayName("당일 지나간 시간에 대한 예약 생성은 불가능하다.")
    void saveReservationByUserWithPastTimeOnSameDateThrowException() {
        // given
        Long timeId = 1L;
        Long themeId = 1L;
        LocalTime pastTime = LocalTime.of(13, 0);
        ReservationCreateRequest request = new ReservationCreateRequest(
                "브라운",
                themeId,
                FIXED_DATE,
                timeId
        );

        when(reservationTimeRepository.findById(timeId))
                .thenReturn(Optional.of(ReservationTime.of(timeId, pastTime)));

        when(themeRepository.findById(themeId))
                .thenReturn(Optional.of(Theme.of(themeId, "theme", "desc", "url")));

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservationByUser(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ReservationErrorCode.PAST_RESERVATION.getMessage());
    }

    @Test
    @DisplayName("이미 동일한 테마, 날짜, 시간에 예약이 존재하면 예약이 불가능하다.")
    void saveReservationByUserWithDuplicateThrowException() {
        // given
        Long timeId = 1L;
        Long themeId = 1L;
        LocalDate date = FIXED_DATE.plusDays(1);
        ReservationCreateRequest request = new ReservationCreateRequest(
                "브라운",
                themeId,
                date,
                timeId
        );

        when(reservationTimeRepository.findById(timeId))
                .thenReturn(Optional.of(ReservationTime.of(timeId, LocalTime.of(10, 0))));

        when(themeRepository.findById(themeId))
                .thenReturn(Optional.of(Theme.of(themeId, "theme", "desc", "url")));

        when(reservationRepository.existsByThemeIdAndDateAndTimeId(themeId, date, timeId))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservationByUser(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ReservationErrorCode.DUPLICATE_RESERVATION.getMessage());
    }

    @Test
    @DisplayName("예약을 성공적으로 수정한다.")
    void updateReservation() {
        // given
        Long reservationId = 1L;
        Theme theme = Theme.of(1L, "theme1", "desc1", "url1");
        LocalDate newDate = FIXED_DATE.plusDays(1);
        ReservationTime newTime = ReservationTime.of(2L, LocalTime.of(11, 0));

        Reservation existingReservation = Reservation.of(reservationId, "브라운", theme, FIXED_DATE,
                ReservationTime.of(1L, LocalTime.of(10, 0)));
        ReservationUpdateRequest request = new ReservationUpdateRequest(theme.getId(), newDate, newTime.getId());

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(existingReservation));
        when(themeRepository.findById(theme.getId())).thenReturn(Optional.of(theme));
        when(reservationTimeRepository.findById(newTime.getId())).thenReturn(Optional.of(newTime));
        when(reservationRepository.existsByThemeIdAndDateAndTimeId(theme.getId(), newDate, newTime.getId()))
                .thenReturn(false);

        // when
        ReservationResponse response = reservationService.updateReservation(reservationId, request);

        // then
        assertThat(response.id()).isEqualTo(reservationId);
        assertThat(response.date()).isEqualTo(newDate);
        assertThat(response.time().id()).isEqualTo(newTime.getId());
        assertThat(response.username()).isEqualTo("브라운");

        verify(reservationRepository).update(anyLong(), any(Reservation.class));
    }

    @Test
    @DisplayName("과거 날짜로 예약을 수정하면 예외가 발생한다.")
    void updateReservationWithPastDateThrowException() {
        // given
        Long reservationId = 1L;
        Theme theme = Theme.of(1L, "theme1", "desc1", "url1");
        LocalDate newDate = FIXED_DATE.minusDays(1);
        ReservationTime newTime = ReservationTime.of(2L, LocalTime.of(11, 0));

        Reservation existingReservation = Reservation.of(reservationId, "브라운", theme, FIXED_DATE,
                ReservationTime.of(1L, LocalTime.of(10, 0)));
        ReservationUpdateRequest request = new ReservationUpdateRequest(theme.getId(), newDate, newTime.getId());

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(existingReservation));
        when(themeRepository.findById(theme.getId())).thenReturn(Optional.of(theme));
        when(reservationTimeRepository.findById(newTime.getId())).thenReturn(Optional.of(newTime));

        // when & then
        assertThatThrownBy(() -> reservationService.updateReservation(reservationId, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ReservationErrorCode.PAST_RESERVATION.getMessage());
    }

    @Test
    @DisplayName("오늘 날짜 + 과거 시간으로 예약 수정하면 예외 발생한다.")
    void updateReservationWithPastTimeOnSameDateThrowException() {
        // given
        Long reservationId = 1L;
        Theme theme = Theme.of(1L, "theme1", "desc1", "url1");
        LocalTime pastTime = LocalTime.of(13, 0);
        ReservationTime newTime = ReservationTime.of(2L, pastTime);

        Reservation existingReservation = Reservation.of(reservationId, "브라운", theme, FIXED_DATE,
                ReservationTime.of(1L, LocalTime.of(10, 0)));
        ReservationUpdateRequest request = new ReservationUpdateRequest(theme.getId(), FIXED_DATE, newTime.getId());

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(existingReservation));
        when(themeRepository.findById(theme.getId())).thenReturn(Optional.of(theme));
        when(reservationTimeRepository.findById(newTime.getId())).thenReturn(Optional.of(newTime));

        // when & then
        assertThatThrownBy(() -> reservationService.updateReservation(reservationId, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ReservationErrorCode.PAST_RESERVATION.getMessage());
    }

    @Test
    @DisplayName("중복된 시간으로 예약을 수정하면 예외가 발생한다.")
    void updateReservationWithDuplicateThrowException() {
        // given
        Long reservationId = 1L;
        Theme theme = Theme.of(1L, "theme1", "desc1", "url1");
        LocalDate newDate = FIXED_DATE.plusDays(1);
        ReservationTime newTime = ReservationTime.of(2L, LocalTime.of(11, 0));

        Reservation existingReservation = Reservation.of(reservationId, "브라운", theme, FIXED_DATE,
                ReservationTime.of(1L, LocalTime.of(10, 0)));
        ReservationUpdateRequest request = new ReservationUpdateRequest(theme.getId(), newDate, newTime.getId());

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(existingReservation));
        when(themeRepository.findById(theme.getId())).thenReturn(Optional.of(theme));
        when(reservationTimeRepository.findById(newTime.getId())).thenReturn(Optional.of(newTime));
        when(reservationRepository.existsByThemeIdAndDateAndTimeId(theme.getId(), newDate, newTime.getId()))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.updateReservation(reservationId, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ReservationErrorCode.DUPLICATE_RESERVATION.getMessage());
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteReservationBy() {
        // given
        Long reservationId = 1L;

        // when
        reservationService.deleteReservationBy(reservationId);

        // then
        verify(reservationRepository).deleteById(reservationId);
    }

    @Test
    @DisplayName("존재하지 않는 예약 삭제 불가")
    void deleteReservation_throwsException_whenReservationNotFound() {
        // given
        Long invalidReservationId = 999L;
        when(reservationRepository.findById(invalidReservationId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.deleteReservationBy(invalidReservationId))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ReservationErrorCode.RESERVATION_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("사용자는 과거 날짜 예약 삭제 불가")
    void deleteReservation_throwsException_whenPastDate() {
        // given
        Long reservationId = 1L;
        Reservation reservation = Reservation.of(reservationId, "브라운", Theme.of(1L, "t", "d", "u"),
                FIXED_DATE.minusDays(1), ReservationTime.of(1L, LocalTime.of(10, 0)));
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> reservationService.deleteReservationBy(reservationId))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ReservationErrorCode.PAST_RESERVATION.getMessage());
    }

    @Test
    @DisplayName("사용자는 오늘 + 과거 시간 예약 삭제 불가")
    void deleteReservation_throwsException_whenPastTimeOnSameDate() {
        // given
        Long reservationId = 1L;
        ReservationTime pastTime = ReservationTime.of(1L, LocalTime.of(13, 0));
        Reservation reservation = Reservation.of(reservationId, "브라운", Theme.of(1L, "t", "d", "u"), FIXED_DATE,
                pastTime);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> reservationService.deleteReservationBy(reservationId))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ReservationErrorCode.PAST_RESERVATION.getMessage());
    }
}
