package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.testFixture.Fixture.THEME_1;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.application.dto.ReservationDto;
import roomescape.application.dto.TimeDto;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.NotFoundException;
import roomescape.presentation.dto.request.ReservationRequest;
import roomescape.presentation.dto.response.AdminReservationResponse;
import roomescape.presentation.dto.response.TimeResponse;
import roomescape.testRepository.FakeReservationRepository;
import roomescape.testRepository.FakeThemeRepository;
import roomescape.testRepository.FakeTimeRepository;

class ReservationServiceTest {

    private ReservationService reservationService;
    private FakeReservationRepository reservationRepository;
    private FakeTimeRepository timeRepository;
    private FakeThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        timeRepository = new FakeTimeRepository();
        themeRepository = new FakeThemeRepository();
        TimeService timeService = new TimeService(timeRepository);
        ThemeService themeService = new ThemeService(themeRepository);
        reservationService = new ReservationService(reservationRepository, timeService, themeService);
    }

    @DisplayName("예약을 정상적으로 등록한다.")
    @Test
    void register_Reservation() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        timeRepository.save(ReservationTime.withoutId(time));
        themeRepository.save(THEME_1);
        LocalDate date = LocalDate.now().plusDays(1);
        String name = "멍구";
        Long timeId = 1L;

        ReservationRequest reservationRequest = new ReservationRequest(1L, date, name, timeId);

        // when
        ReservationDto reservationDto = reservationService.registerReservation(reservationRequest);

        // then
        assertAll(
                () -> assertThat(reservationDto.id()).isEqualTo(1),
                () -> assertThat(reservationDto.name()).isEqualTo("멍구"),
                () -> assertThat(reservationDto.date()).isEqualTo(date),
                () -> assertThat(reservationDto.time()).isEqualTo(new TimeDto(1L, time))
        );
    }

    @DisplayName("존재하지 않는 timeId로 예약 생성 시 예외 발생")
    @Test
    void error_when_timeId_notExist() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        String name = "멍구";
        Long timeId = 999L;

        ReservationRequest reservationRequest = new ReservationRequest(1L, date, name, timeId);

        // when & then
        assertThatThrownBy(() -> reservationService.registerReservation(reservationRequest))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("중복된 일시에 예약 생성 시 예외 발생")
    @Test
    void error_when_dateTime_Duplicate() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        timeRepository.save(ReservationTime.withoutId(time));

        LocalDate date = LocalDate.now().plusDays(1);
        String name = "멍구";
        Long timeId = 1L;

        ReservationTime reservationTime = ReservationTime.of(1L, time);
        Reservation reservation = Reservation.of(1L, name, THEME_1, date, reservationTime);
        reservationRepository.save(reservation);

        // when
        ReservationRequest reservationRequest = new ReservationRequest(1L, date, "아이나", timeId);

        // then
        assertThatThrownBy(() -> reservationService.registerReservation(reservationRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 예약을 조회할 수 있다.")
    @Test
    void getAllReservations() {
        // given
        ReservationTime time1 = ReservationTime.of(1L, LocalTime.of(10, 0));
        ReservationTime time2 = ReservationTime.of(2L, LocalTime.of(11, 0));

        reservationRepository.save(Reservation.of(1L, "브라운", THEME_1, LocalDate.of(2024, 4, 1), time1));
        reservationRepository.save(Reservation.of(2L, "솔라", THEME_1, LocalDate.of(2024, 4, 1), time2));
        reservationRepository.save(Reservation.of(3L, "브리", THEME_1, LocalDate.of(2024, 4, 2), time1));

        // when
        List<ReservationDto> allReservations = reservationService.getAllReservations();

        // then
        assertAll(
                () -> assertThat(allReservations).hasSize(3),
                () -> assertThat(allReservations)
                        .extracting(ReservationDto::name)
                        .containsExactly("브라운", "솔라", "브리")
        );
    }

    @DisplayName("예약을 id로 취소할 수 있다.")
    @Test
    void cancelReservation() {
        // given
        ReservationTime time = ReservationTime.of(1L, LocalTime.of(10, 0));
        reservationRepository.save(Reservation.of(1L, "브라운", THEME_1, LocalDate.of(2024, 4, 1), time));

        assertThat(reservationRepository.findAll()).hasSize(1);

        // when
        reservationService.deleteReservation(1L);

        // then
        assertThat(reservationRepository.findAll()).hasSize(0);
    }

    @DisplayName("과거 일시로 예약할 수 없다.")
    @Test
    void cannot_register_when_past() {
        // given
        LocalDate date = LocalDate.now().minusDays(1);
        Long timeId = timeRepository.save(ReservationTime.withoutId(LocalTime.of(10, 0)));

        // when
        ReservationRequest request = new ReservationRequest(1L, date, "멍구", timeId);

        // then
        assertThatThrownBy(() -> reservationService.registerReservation(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("중복된 일시로 예약할 수 없다.")
    @Test
    void cannot_register_when_duplicate() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        Long timeId = timeRepository.save(ReservationTime.withoutId(LocalTime.of(10, 0)));
        ReservationTime reservationTime = ReservationTime.of(timeId, LocalTime.of(10, 0));
        Reservation reservation = Reservation.of(1L, "아이나", THEME_1, date, reservationTime);
        reservationRepository.save(reservation);

        // when
        ReservationRequest request = new ReservationRequest(1L, date, "멍구", timeId);

        // then
        assertThatThrownBy(() -> reservationService.registerReservation(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
