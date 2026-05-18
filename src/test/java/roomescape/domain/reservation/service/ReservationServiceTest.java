package roomescape.domain.reservation.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.exception.DuplicateReservationException;
import roomescape.domain.reservation.exception.PastReservationDateException;
import roomescape.domain.reservation.exception.ReservationOwnerMismatchException;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.request.ReservationCreateRequest;
import roomescape.domain.reservation.request.ReservationUpdateRequest;
import roomescape.domain.reservation.response.ReservationResponse;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.theme.response.ThemeResponse;
import roomescape.domain.time.entity.ReservationTime;
import roomescape.domain.time.exception.ReservationTimeNotFoundException;
import roomescape.domain.time.repository.ReservationTimeRepository;
import roomescape.domain.time.response.ReservationTimeResponse;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ReservationTimeRepository reservationTimeRepository;

    @Mock
    ThemeRepository themeRepository;

    private final Clock fixedClock = Clock.fixed(
            LocalDate.of(2026, 5, 6)
                    .atTime(23, 0)
                    .atZone(ZoneId.systemDefault())
                    .toInstant(),
            ZoneId.systemDefault()
    );

    ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(
                reservationRepository,
                reservationTimeRepository,
                themeRepository,
                fixedClock
        );
    }

    @Test
    @DisplayName("예약을 성공적으로 생성한다.")
    void saveReservation() {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        when(reservationTimeRepository.findById(eq(1L)))
                .thenReturn(Optional.of(reservationTime));

        Theme theme = new Theme("theme1", "description1", "thumbnail url 1");
        when(themeRepository.findById(eq(1L)))
                .thenReturn(Optional.of(theme));

        Reservation reservation = new Reservation(
                1L,
                "브라운",
                theme,
                LocalDate.of(9999, 4, 30),
                reservationTime
        );
        when(reservationRepository.save(any(Reservation.class)))
                .thenReturn(reservation);

        ReservationCreateRequest request = new ReservationCreateRequest(
                "브라운",
                1L,
                LocalDate.of(9999, 4, 30),
                1L
        );

        // when
        ReservationResponse response = reservationService.saveReservation(request);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.username()).isEqualTo("브라운");
        assertThat(response.theme().name()).isEqualTo("theme1");
        assertThat(response.date()).isEqualTo(LocalDate.of(9999, 4, 30));
        assertThat(response.time().id()).isEqualTo(1L);

        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    @DisplayName("존재하지 않는 시간으로 예약 시 예외가 발생한다.")
    void saveReservation_throwsException_whenTimeNotFound() {
        // given
        Long invalidTimeId = 999L;

        ReservationCreateRequest request = new ReservationCreateRequest(
                "브라운",
                1L,
                LocalDate.of(2026, 4, 30),
                invalidTimeId
        );

        when(reservationTimeRepository.findById(invalidTimeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(ReservationTimeNotFoundException.class)
                .hasMessageContaining("존재하지 않는 예약 시간입니다.");
    }

    @Test
    @DisplayName("지난 날짜로 예약 시 예외가 발생한다.")
    void saveReservation_throwsException_whenBeforeDate() {
        // given
        Long timeId = 999L;
        ReservationCreateRequest request = new ReservationCreateRequest(
                "브라운",
                1L,
                LocalDate.of(1000, 4, 30),
                timeId
        );

        when(reservationTimeRepository.findById(timeId))
                .thenReturn(Optional.of(new ReservationTime(timeId, LocalTime.of(10, 0))));

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(PastReservationDateException.class)
                .hasMessageContaining("과거 날짜로 예약할 수 없습니다.");
    }

    @Test
    @DisplayName("당일 지난 시간으로 예약 시 예외가 발생한다.")
    void saveReservation_throwsException_whenBeforeTime() {
        // given
        Long timeId = 999L;
        ReservationCreateRequest request = new ReservationCreateRequest(
                "브라운",
                1L,
                LocalDate.now(fixedClock),
                timeId
        );

        when(reservationTimeRepository.findById(timeId))
                .thenReturn(Optional.of(new ReservationTime(timeId, LocalTime.of(1, 0))));

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(PastReservationDateException.class)
                .hasMessageContaining("과거 날짜로 예약할 수 없습니다.");
    }

    @Test
    @DisplayName("같은 날짜+시간+테마에 이미 예약이 있으면 중복 예약을 거부한다.")
    void saveReservation_throwsException_whenDuplicateReservation() {
        // given
        Long themeId = 1L;
        Long timeId = 1L;
        LocalDate date = LocalDate.of(9999, 5, 10);

        ReservationTime reservationTime = new ReservationTime(timeId, LocalTime.of(10, 0));
        Theme theme = new Theme(themeId, "theme1", "description1", "thumbnail url 1");

        ReservationCreateRequest request = new ReservationCreateRequest(
                "브라운",
                themeId,
                date,
                timeId
        );

        when(reservationTimeRepository.findById(timeId))
                .thenReturn(Optional.of(reservationTime));
        when(themeRepository.findById(themeId))
                .thenReturn(Optional.of(theme));
        when(reservationRepository.exists(any(Reservation.class)))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(DuplicateReservationException.class)
                .hasMessageContaining("이미 예약된 시간입니다.");

        verify(reservationRepository).exists(any(Reservation.class));
    }


    @Test
    @DisplayName("모든 예약을 조회한다.")
    void findAllReservations() {
        // given
        List<Reservation> reservations = new ArrayList<>();
        Theme theme = new Theme(1L, "theme1", "description1", "thumbnail url 1");
        ReservationTime time1 = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime time2 = new ReservationTime(2L, LocalTime.of(11, 0));

        reservations.add(new Reservation(1L, "브라운", theme, LocalDate.of(2026, 4, 30), time1));
        reservations.add(new Reservation(2L, "크루", theme, LocalDate.of(2026, 4, 30), time2));
        when(reservationRepository.findAll()).thenReturn(reservations);

        // when
        List<ReservationResponse> responses = reservationService.findAllReservations();

        // then
        assertThat(responses).hasSize(2)
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
    @DisplayName("사용자 이름으로 예약 목록을 조회한다.")
    void findReservationsByUsername() {
        // given
        String username = "브라운";
        Theme theme = new Theme(1L, "theme1", "description1", "thumbnail url 1");
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        List<Reservation> reservations = List.of(
                new Reservation(1L, username, theme, LocalDate.of(2026, 4, 30), time)
        );

        when(reservationRepository.findByUsername(username)).thenReturn(reservations);

        // when
        List<ReservationResponse> responses = reservationService.findReservationsByUsername(username);

        // then
        assertThat(responses).hasSize(1)
                .extracting("username", "theme", "date", "time")
                .containsExactly(
                        tuple(
                                username,
                                ThemeResponse.from(theme),
                                LocalDate.of(2026, 4, 30),
                                ReservationTimeResponse.from(time)
                        )
                );

        verify(reservationRepository).findByUsername(username);
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
    @DisplayName("사용자는 본인의 예약을 취소한다.")
    void cancelReservationBy() {
        // given
        Long reservationId = 1L;
        String username = "브라운";
        Theme theme = new Theme(1L, "theme1", "description1", "thumbnail url 1");
        ReservationTime time = new ReservationTime(1L, LocalTime.of(13, 0));
        Reservation reservation = new Reservation(1L, username, theme, LocalDate.of(9999, 1, 1), time);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // when
        reservationService.cancelReservationBy(reservationId, username);

        // then
        verify(reservationRepository).findById(reservationId);
        verify(reservationRepository).deleteById(reservationId);
    }

    @Test
    @DisplayName("사용자는 다른 사람의 예약을 취소할 수 없다.")
    void cancelReservationBy_throwsException_whenUsernameDoesNotMatch() {
        // given
        Long reservationId = 1L;
        Theme theme = new Theme(1L, "theme1", "description1", "thumbnail url 1");
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Reservation reservation = new Reservation(1L, "브라운", theme, LocalDate.of(9999, 4, 30), time);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> reservationService.cancelReservationBy(reservationId, "다른사용자"))
                .isInstanceOf(ReservationOwnerMismatchException.class)
                .hasMessageContaining("본인의 예약만 취소할 수 있습니다.");

        verify(reservationRepository).findById(reservationId);
    }

    @Test
    @DisplayName("사용자는 본인 예약의 날짜와 시간을 변경한다.")
    void updateReservationSchedule() {
        // given
        Long reservationId = 1L;
        String username = "브라운";
        Theme theme = new Theme(1L, "theme1", "description1", "thumbnail url 1");
        ReservationTime originalTime = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime newTime = new ReservationTime(2L, LocalTime.of(13, 0));
        LocalDate newDate = LocalDate.of(9999, 5, 10);
        Reservation reservation = new Reservation(
                reservationId,
                username,
                theme,
                LocalDate.of(9999, 5, 9),
                originalTime
        );
        Reservation updatedReservation = new Reservation(reservationId, username, theme, newDate, newTime);
        ReservationUpdateRequest request = new ReservationUpdateRequest(newDate, newTime.getId());

        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.of(reservation));
        when(reservationTimeRepository.findById(newTime.getId()))
                .thenReturn(Optional.of(newTime));
        when(reservationRepository.update(any(Reservation.class)))
                .thenReturn(updatedReservation);

        // when
        ReservationResponse response = reservationService.updateReservationSchedule(reservationId, username, request);

        // then
        assertThat(response.id()).isEqualTo(reservationId);
        assertThat(response.username()).isEqualTo(username);
        assertThat(response.theme()).isEqualTo(ThemeResponse.from(theme));
        assertThat(response.date()).isEqualTo(newDate);
        assertThat(response.time()).isEqualTo(ReservationTimeResponse.from(newTime));

        verify(reservationRepository).findById(reservationId);
        verify(reservationTimeRepository).findById(newTime.getId());
        verify(reservationRepository).exists(any(Reservation.class));
        verify(reservationRepository).update(any(Reservation.class));
    }

    @Test
    @DisplayName("사용자는 다른 사람의 예약을 변경할 수 없다.")
    void updateReservationSchedule_throwsException_whenUsernameDoesNotMatch() {
        // given
        Long reservationId = 1L;
        Theme theme = new Theme(1L, "theme1", "description1", "thumbnail url 1");
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Reservation reservation = new Reservation(1L, "브라운", theme, LocalDate.of(9999, 4, 30), time);
        ReservationUpdateRequest request = new ReservationUpdateRequest(LocalDate.of(9999, 5, 10), 2L);

        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> reservationService.updateReservationSchedule(reservationId, "다른사용자", request))
                .isInstanceOf(ReservationOwnerMismatchException.class)
                .hasMessageContaining("본인의 예약만 취소할 수 있습니다.");

        verify(reservationRepository).findById(reservationId);
    }

    @Test
    @DisplayName("존재하지 않는 시간으로 예약 변경 시 예외가 발생한다.")
    void updateReservationSchedule_throwsException_whenTimeNotFound() {
        // given
        Long reservationId = 1L;
        Long invalidTimeId = 999L;
        String username = "브라운";
        Theme theme = new Theme(1L, "theme1", "description1", "thumbnail url 1");
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Reservation reservation = new Reservation(reservationId, username, theme, LocalDate.of(9999, 4, 30), time);
        ReservationUpdateRequest request = new ReservationUpdateRequest(LocalDate.of(9999, 5, 10), invalidTimeId);

        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.of(reservation));
        when(reservationTimeRepository.findById(invalidTimeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.updateReservationSchedule(reservationId, username, request))
                .isInstanceOf(ReservationTimeNotFoundException.class)
                .hasMessageContaining("존재하지 않는 예약 시간입니다.");

        verify(reservationRepository).findById(reservationId);
        verify(reservationTimeRepository).findById(invalidTimeId);
    }

    @Test
    @DisplayName("지난 날짜로 예약 변경 시 예외가 발생한다.")
    void updateReservationSchedule_throwsException_whenBeforeDate() {
        // given
        Long reservationId = 1L;
        String username = "브라운";
        Theme theme = new Theme(1L, "theme1", "description1", "thumbnail url 1");
        ReservationTime originalTime = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime newTime = new ReservationTime(2L, LocalTime.of(13, 0));
        Reservation reservation = new Reservation(reservationId, username, theme, LocalDate.of(9999, 4, 30), originalTime);
        ReservationUpdateRequest request = new ReservationUpdateRequest(LocalDate.of(1000, 5, 10), newTime.getId());

        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.of(reservation));
        when(reservationTimeRepository.findById(newTime.getId()))
                .thenReturn(Optional.of(newTime));

        // when & then
        assertThatThrownBy(() -> reservationService.updateReservationSchedule(reservationId, username, request))
                .isInstanceOf(PastReservationDateException.class)
                .hasMessageContaining("과거 날짜로 예약할 수 없습니다.");
    }

    @Test
    @DisplayName("변경하려는 날짜+시간+테마에 이미 예약이 있으면 중복 예약을 거부한다.")
    void updateReservationSchedule_throwsException_whenDuplicateReservation() {
        // given
        Long reservationId = 1L;
        String username = "브라운";
        Theme theme = new Theme(1L, "theme1", "description1", "thumbnail url 1");
        ReservationTime originalTime = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime newTime = new ReservationTime(2L, LocalTime.of(13, 0));
        Reservation reservation = new Reservation(reservationId, username, theme, LocalDate.of(9999, 4, 30), originalTime);
        ReservationUpdateRequest request = new ReservationUpdateRequest(LocalDate.of(9999, 5, 10), newTime.getId());

        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.of(reservation));
        when(reservationTimeRepository.findById(newTime.getId()))
                .thenReturn(Optional.of(newTime));
        when(reservationRepository.exists(any(Reservation.class)))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.updateReservationSchedule(reservationId, username, request))
                .isInstanceOf(DuplicateReservationException.class)
                .hasMessageContaining("이미 예약된 시간입니다.");

        verify(reservationRepository).exists(any(Reservation.class));
    }
}
