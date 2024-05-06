package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.dto.request.CreateReservationRequest;
import roomescape.reservation.dto.response.FindAvailableTimesResponse;
import roomescape.reservation.dto.response.FindReservationResponse;
import roomescape.reservation.fake.FakeReservationRepository;
import roomescape.reservationtime.fake.FakeReservationTimeRepository;
import roomescape.theme.fake.FakeThemeRepository;
import roomescape.fixture.Fixture;

class ReservationServiceTest {

    private static FakeThemeRepository themeRepository;
    private static FakeReservationTimeRepository reservationTimeRepository;
    private static FakeReservationRepository reservationRepository;
    private static ReservationService reservationService;

    @BeforeAll
    static void beforeAll() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        themeRepository = new FakeThemeRepository(reservationRepository);
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository,
                themeRepository);
    }

    @BeforeEach
    void setUp() {
        reservationRepository.clear();
        reservationTimeRepository.clear();
        themeRepository.clear();

        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_2);
        themeRepository.save(Fixture.THEME_1);
        themeRepository.save(Fixture.THEME_2);
        reservationRepository.save(Fixture.RESERVATION_1);
        reservationRepository.save(Fixture.RESERVATION_2);
    }

    @Test
    @DisplayName("예약 생성 시 해당 데이터를 반환한다.")
    void createReservation() {
        // given
        var request = new CreateReservationRequest("포비", LocalDate.of(2124, 10, 10), 1L, 1L);

        // when
        var response = reservationService.createReservation(request);

        // then
        Assertions.assertAll(() -> assertThat(response.id()).isEqualTo(3L),
                () -> assertThat(response.name()).isEqualTo("포비"),
                () -> assertThat(response.date()).isEqualTo("2124-10-10"),
                () -> assertThat(response.time().id()).isEqualTo(1L),
                () -> assertThat(response.theme().id()).isEqualTo(1L));
    }

    @Test
    @DisplayName("예약 생성 시 해당하는 예약 시간 id 가 없는 경우 예외가 발생한다.")
    void createReservation_ifReservationTimeNotExist_throwException() {
        // given
        var request = new CreateReservationRequest("포비", LocalDate.of(10, 10, 10), 99999L, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request)).isInstanceOf(
                NoSuchElementException.class).hasMessage("해당하는 예약 시간이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("예약 생성 시 해당하는 테마 id가 없는 경우 예외가 발생한다.")
    void createReservation_ifThemeNotExist_throwException() {
        // given
        var request = new CreateReservationRequest("포비", LocalDate.of(10, 10, 10), 1L, 99999L);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request)).isInstanceOf(
                NoSuchElementException.class).hasMessage("해당하는 테마가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("예약 생성 시 동일한 테마, 날짜, 시간이 겹치는 경우 예외가 발생한다.")
    void createReservation_ifExistSameDateAndTime_throwException() {
        // given
        var request = new CreateReservationRequest("포비", LocalDate.of(2030, 4, 23), 1L, 1L);
        reservationService.createReservation(request);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request)).isInstanceOf(
                IllegalStateException.class).hasMessage("동일한 시간의 예약이 존재합니다.");
    }

    @Test
    @DisplayName("예약 생성 시 지나간 날짜와 시간에 대한 예약인 경우 예외가 발생한다.")
    void createReservation_validateReservationDateTime_throwException() {
        // given
        CreateReservationRequest createReservationRequest = new CreateReservationRequest("포비", LocalDate.of(1, 1, 1),
                1L, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(createReservationRequest)).isInstanceOf(
                IllegalArgumentException.class).hasMessage("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
    }

    @Test
    @DisplayName("예약 목록 조회 시 저장된 예약과 예약 시간에 대한 정보를 반환한다.")
    void getReservations() {
        // when
        List<FindReservationResponse> reservations = reservationService.getReservations();

        // then
        assertThat(reservations)
                .containsExactly(
                        FindReservationResponse.of(Fixture.RESERVATION_1),
                        FindReservationResponse.of(Fixture.RESERVATION_2));
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 대한 정보를 반환한다.")
    void getReservation() {
        // when & then
        assertThat(reservationService.getReservation(1L)).isEqualTo(FindReservationResponse.of(Fixture.RESERVATION_1));
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약이 없는 경우 예외가 발생한다.")
    void getReservation_ifNotExist_throwException() {
        // when & then
        assertThatThrownBy(() -> reservationService.getReservation(99999L)).isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 예약이 존재하지 않습니다.");
    }


    @Test
    @DisplayName("해당하는 날짜와 테마를 통해 가능한 예약 시간 정보를 반환한다.")
    void getAvailableTimes() {
        LocalDate date = Fixture.RESERVATION_1.getDate();
        LocalTime time1 = Fixture.RESERVATION_TIME_1.getTime();
        LocalTime time2 = Fixture.RESERVATION_TIME_2.getTime();

        // when & then
        assertThat(reservationService.getAvailableTimes(date, 1L)).isEqualTo(
                List.of(FindAvailableTimesResponse.of(1L, time1, true),
                        FindAvailableTimesResponse.of(2L, time2, false)));
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약을 삭제한다.")
    void deleteReservation() {
        // when
        reservationService.deleteReservation(2L);

        // then
        assertThat(reservationService.getReservations())
                .containsExactly(FindReservationResponse.of(Fixture.RESERVATION_1));
    }

    @Test
    @DisplayName("존재하지 않는 예약 id 를 삭제 시 예외가 발생한다.")
    void deleteReservation_ifNotExist_throwException() {
        // when & then
        assertThatThrownBy(() -> reservationService.deleteReservation(99999L)).isInstanceOf(
                NoSuchElementException.class).hasMessage("해당하는 예약이 존재하지 않습니다.");
    }
}
