package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static roomescape.test.fixture.DateFixture.NEXT_DAY;
import static roomescape.test.fixture.DateFixture.TODAY;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationCreationRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;
import roomescape.repository.theme.ThemeRepository;
import roomescape.test.fixture.ReservationFixture;

class ReservationServiceTest {

    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final ReservationTimeRepository timeRepository = mock(ReservationTimeRepository.class);
    private final ThemeRepository themeRepository = mock(ThemeRepository.class);
    private final ReservationService reservationService =
            new ReservationService(reservationRepository, timeRepository, themeRepository);

    @DisplayName("저장된 예약들을 조회할 수 있다")
    @Test
    void canGetReservations() {
        // given
        List<Reservation> reservations = List.of(ReservationFixture.create(1L, "예약1"),
                ReservationFixture.create(2L, "예약2"),
                ReservationFixture.create(3L, "예약3"));
        when(reservationRepository.findAll()).thenReturn(reservations);

        // when
        List<Reservation> allReservations = reservationService.getAllReservations();

        // then
        assertThat(allReservations).hasSize(3);
    }

    @DisplayName("ID로 예약을 조회할 수 있다")
    @Test
    void canGetReservationById() {
        // given
        Reservation reservation = ReservationFixture.create(1L, "예약1");
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // when
        Reservation actualReservation = reservationService.getReservationById(1L);

        // then
        assertThat(actualReservation).isEqualTo(reservation);
    }

    @DisplayName("ID로 예약을 조회할 때, 데이터가 없는 경우 예외를 발생시킨다")
    @Test
    void cannotGetReservationByIdBecauseOfEmptyData() {
        // given
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.getReservationById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 예약이 존재하지 않습니다.");
    }

    @DisplayName("예약을 추가할 수 있다")
    @Test
    void canSaveReservation() {
        // given
        ReservationCreationRequest request = new ReservationCreationRequest("예약", NEXT_DAY, 1L, 1L);
        Theme theme = new Theme(request.themeId(), "테마", "설명", "섬네일");
        ReservationTime time = new ReservationTime(request.timeId(), LocalTime.now());
        when(themeRepository.findById(request.themeId())).thenReturn(Optional.of(theme));
        when(timeRepository.findById(request.timeId())).thenReturn(Optional.of(time));
        when(reservationRepository.checkAlreadyReserved(request.date(), request.timeId(), request.themeId()))
                .thenReturn(false);
        when(reservationRepository.add(any())).thenReturn(1L);

        // when
        long savedId = reservationService.saveReservation(request);

        // then
        assertThat(savedId).isEqualTo(1L);
    }

    @DisplayName("예약을 추가할 때, 예약 테마가 유효하지 않은 경우 예외를 발생시킨다")
    @Test
    void cannotSaveReservationBecauseOfInvalidTheme() {
        // given
        ReservationCreationRequest request = new ReservationCreationRequest("예약", NEXT_DAY, 1L, 1L);
        ReservationTime time = new ReservationTime(request.timeId(), LocalTime.now());
        when(themeRepository.findById(request.themeId())).thenReturn(Optional.empty());
        when(timeRepository.findById(request.timeId())).thenReturn(Optional.of(time));
        when(reservationRepository.checkAlreadyReserved(request.date(), request.timeId(), request.themeId()))
                .thenReturn(false);
        when(reservationRepository.add(any())).thenReturn(1L);

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 테마가 존재하지 않습니다.");
    }

    @DisplayName("예약을 추가할 때, 예약 시간이 유효하지 않은 경우 예외를 발생시킨다")
    @Test
    void cannotSaveReservationBecauseOfInvalidTime() {
        // given
        ReservationCreationRequest request = new ReservationCreationRequest("예약", NEXT_DAY, 1L, 1L);
        Theme theme = new Theme(request.themeId(), "테마", "설명", "섬네일");
        when(themeRepository.findById(request.themeId())).thenReturn(Optional.of(theme));
        when(timeRepository.findById(request.timeId())).thenReturn(Optional.empty());
        when(reservationRepository.checkAlreadyReserved(request.date(), request.timeId(), request.themeId()))
                .thenReturn(false);
        when(reservationRepository.add(any())).thenReturn(1L);

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 예약시간이 존재하지 않습니다.");
    }

    @DisplayName("예약을 추가할 때, 과거 날짜와 시간으로는 예약을 추가할 수 없다")
    @Test
    void cannotCreateReservationWithPastDateTime() {
        // given
        ReservationCreationRequest request = new ReservationCreationRequest("예약", TODAY, 1L, 1L);
        Theme theme = new Theme(request.themeId(), "테마", "설명", "섬네일");
        ReservationTime time = new ReservationTime(request.timeId(), LocalTime.now().minusSeconds(1));
        when(themeRepository.findById(request.themeId())).thenReturn(Optional.of(theme));
        when(timeRepository.findById(request.timeId())).thenReturn(Optional.of(time));
        when(reservationRepository.checkAlreadyReserved(request.date(), request.timeId(), request.themeId()))
                .thenReturn(false);
        when(reservationRepository.add(any())).thenReturn(1L);

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 이미 과거의 날짜와 시간입니다.");
    }

    @DisplayName("예약을 추가할 때, 중복된 예약은 허용하지 않는다")
    @Test
    void cannotCreateReservationWithSameDateTime() {
        // given
        ReservationCreationRequest request = new ReservationCreationRequest("예약", NEXT_DAY, 1L, 1L);
        Theme theme = new Theme(request.themeId(), "테마", "설명", "섬네일");
        ReservationTime time = new ReservationTime(request.timeId(), LocalTime.now());
        when(themeRepository.findById(request.themeId())).thenReturn(Optional.of(theme));
        when(timeRepository.findById(request.timeId())).thenReturn(Optional.of(time));
        when(reservationRepository.checkAlreadyReserved(request.date(), request.timeId(), request.themeId()))
                .thenReturn(true);
        when(reservationRepository.add(any())).thenReturn(1L);

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 이미 존재하는 예약입니다.");
    }

    @DisplayName("ID를 통해 예약을 삭제할 수 있다.")
    @Test
    void canDeleteReservation() {
        // given
        Reservation reservation = ReservationFixture.create(1L, "예약1");
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        // when & then
        assertAll(
                () -> assertThatCode(() -> reservationService.deleteReservation(reservation.getId()))
                        .doesNotThrowAnyException(),
                () -> verify(reservationRepository, times(1)).deleteById(reservation.getId())
        );
    }

    @DisplayName("존재하지 않는 예약을 삭제하려고 할 경우 예외를 발생시킨다")
    @Test
    void cannotDeleteNoneExistentReservation() {
        // given
        long noneExistentId = 1L;
        when(reservationRepository.findById(noneExistentId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.deleteReservation(noneExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 예약이 존재하지 않습니다.");
    }
}
