package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.reservation.dto.request.CreateReservationRequest;
import roomescape.reservation.dto.response.CreateReservationResponse;
import roomescape.reservation.dto.response.FindAvailableTimesResponse;
import roomescape.reservation.dto.response.FindReservationResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.repository.ThemeRepository;
import roomescape.util.DummyDataFixture;

@SpringBootTest
class ReservationServiceTest extends DummyDataFixture {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("예약 생성 시 해당 데이터의 id값을 반환한다.")
    void createReservation() {
        // given
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                LocalDate.of(2024, 10, 10), "포비", 1L, 1L);

        // stub
        Mockito.when(reservationTimeRepository.findById(1L)).thenReturn(Optional.of(super.getReservationTimeById(1L)));
        Mockito.when(themeRepository.findById(1L)).thenReturn(Optional.of(super.getThemeById(1L)));
        Mockito.when(reservationRepository.save(any(Reservation.class))).thenReturn(super.getReservationById(10L));

        // when
        CreateReservationResponse createReservationResponse = reservationService.createReservation(
                createReservationRequest);

        // then
        assertThat(createReservationResponse.id()).isEqualTo(10L);
    }

    @Test
    @DisplayName("예약 생성 시 해당하는 예약 시간 id와 동일하게 저장된 예약 시간이 없는 경우 예외가 발생한다.")
    void createReservation_ifReservationTimeNotExist_throwException() {
        // given
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                LocalDate.of(10, 10, 10), "포비", 1L, 1L);

        // stub
        Mockito.when(reservationTimeRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(createReservationRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 예약 시간이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("예약 생성 시 해당하는 테마 id와 동일하게 저장된 테마가 없는 경우 예외가 발생한다.")
    void createReservation_ifThemeNotExist_throwException() {
        // given
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                LocalDate.of(10, 10, 10), "포비", 1L, 1L);

        // stub
        Mockito.when(reservationTimeRepository.findById(1L)).thenReturn(Optional.of(super.getReservationTimeById(1L)));
        Mockito.when(themeRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(createReservationRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 테마가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("예약 생성 시 해당하는 날짜와 시간이 동일하게 저장된 테마가 있는 경우 예외가 발생한다.")
    void createReservation_ifExistSameDateAndTime_throwException() {
        // given
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                LocalDate.of(2024, 10, 10), "포비", 1L, 1L);

        // stub
        Mockito.when(reservationTimeRepository.findById(1L)).thenReturn(Optional.of(super.getReservationTimeById(1L)));
        Mockito.when(themeRepository.findById(1L)).thenReturn(Optional.of(super.getThemeById(1L)));
        Mockito.when(reservationRepository.existsByDateAndTime(LocalDate.of(2024, 10, 10), 1L)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(createReservationRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("동일한 시간의 예약이 존재합니다.");
    }

    @Test
    @DisplayName("예약 생성 시 지나간 날짜와 시간에 대한 예약인 경우 예외가 발생한다.")
    void createReservation_validateReservationDateTime_throwException() {
        // given
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                LocalDate.of(1000, 10, 10), "포비", 1L, 1L);

        // stub
        Mockito.when(reservationTimeRepository.findById(1L)).thenReturn(Optional.of(super.getReservationTimeById(1L)));
        Mockito.when(themeRepository.findById(1L)).thenReturn(Optional.of(super.getThemeById(1L)));

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(createReservationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
    }

    @Test
    @DisplayName("예약 목록 조회 시 저장된 예약과 예약 시간에 대한 정보를 반환한다.")
    void getReservations() {
        // stub
        Mockito.when(reservationRepository.findAll())
                .thenReturn(List.of(super.getReservationById(1L), super.getReservationById(2L)));

        // when & then
        assertThat(reservationService.getReservations())
                .containsExactly(
                        FindReservationResponse.of(super.getReservationById(1L)),
                        FindReservationResponse.of(super.getReservationById(2L)));
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 대한 정보를 반환한다.")
    void getReservation() {
        // given
        long id = 1L;

        // stub
        Mockito.when(reservationRepository.findById(id))
                .thenReturn(Optional.ofNullable(super.getReservationById(id)));

        // when & then
        assertThat(reservationService.getReservation(id))
                .isEqualTo(FindReservationResponse.of(super.getReservationById(1L)));
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약이 없는 경우 예외가 발생한다.")
    void getReservation_ifNotExist_throwException() {
        // stub
        Mockito.when(reservationRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.getReservation(anyLong()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 예약이 존재하지 않습니다.");
    }


    @Test
    @DisplayName("해당하는 날짜와 테마를 통해 가능한 예약 시간 정보를 반환한다.")
    void getAvailableTimes() {
        LocalDate date = LocalDate.of(2024, 10, 23);

        // stub
        Mockito.when(reservationTimeRepository.findAll()).thenReturn(getPreparedReservationTimes());
        Mockito.when(reservationRepository.findAllByDateAndThemeId(date, 1L))
                .thenReturn(List.of(getReservationById(1L), getReservationById(2L)));

        // when & then
        assertThat(reservationService.getAvailableTimes(date, 1L))
                .isEqualTo(List.of(
                        FindAvailableTimesResponse.of(1L, getReservationTimeById(1L).getTime(), true),
                        FindAvailableTimesResponse.of(2L, getReservationTimeById(2L).getTime(), true),
                        FindAvailableTimesResponse.of(3L, getReservationTimeById(3L).getTime(), false),
                        FindAvailableTimesResponse.of(4L, getReservationTimeById(4L).getTime(), false),
                        FindAvailableTimesResponse.of(5L, getReservationTimeById(5L).getTime(), false),
                        FindAvailableTimesResponse.of(6L, getReservationTimeById(6L).getTime(), false)));
    }


    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약을 삭제한다.")
    void deleteReservation() {
        // given
        long reservationId = 1L;

        // stub
        Mockito.when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.of(super.getReservationById(reservationId)));

        // when
        reservationService.deleteReservation(reservationId);

        // then
        Mockito.verify(reservationRepository, Mockito.times(1)).deleteById(reservationId);
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약이 없는 경우 예외가 발생한다.")
    void deleteReservation_ifNotExist_throwException() {
        // given
        long timeId = 2L;

        // stub
        Mockito.when(reservationRepository.findById(timeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.deleteReservation(timeId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 예약이 존재하지 않습니다.");
    }
}
