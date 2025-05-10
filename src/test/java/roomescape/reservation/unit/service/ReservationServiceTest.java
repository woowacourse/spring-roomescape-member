package roomescape.reservation.unit.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.error.exception.ConflictException;
import roomescape.global.error.exception.NotFoundException;
import roomescape.reservation.dto.request.ReservationRequest.ReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse.ReservationCreateResponse;
import roomescape.reservation.dto.response.ReservationResponse.ReservationReadResponse;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.entity.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.unit.repository.FakeReservationRepository;
import roomescape.reservation.unit.repository.FakeReservationTimeRepository;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.unit.repository.FakeThemeRepository;

class ReservationServiceTest {

    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;
    private ReservationService reservationService;

    @BeforeEach
    void beforeEach() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        themeRepository = new FakeThemeRepository();
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);
    }

    @Test
    @DisplayName("모든 예약을 조회한다.")
    void getReservations() {
        // given
        long id = 1L;
        String name = "브라운";
        LocalDate date = LocalDate.of(2023, 8, 5);
        long themeId = 1L;
        ReservationTime reservationTime = new ReservationTime(
                1L,
                LocalTime.of(10, 0)
        );
        Theme theme = new Theme(
                1L,
                "미소",
                "미소 테마",
                "https://miso.com"
        );
        Reservation reservation = new Reservation(
                id,
                name,
                date,
                reservationTime,
                themeId
        );

        reservationRepository.save(reservation);
        themeRepository.save(theme);

        // when
        List<ReservationReadResponse> responses = reservationService.getAllReservations();

        // then
        ReservationReadResponse response = responses.getFirst();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.name()).isEqualTo(name);
        assertThat(response.date()).isEqualTo(date);
        assertThat(response.time().id()).isEqualTo(reservationTime.getId());
        assertThat(response.theme().id()).isEqualTo(themeId);
    }

    @Test
    @Disabled
    @DisplayName("예약을 저장한다.")
    void createReservation() {
        // given
        ReservationTime reservationTime = new ReservationTime(
                1L,
                LocalTime.of(10, 0)
        );
        reservationTimeRepository.save(reservationTime);

        LocalDate date = LocalDate.of(2025, 4, 21);
        String name = "미소";
        long timeId = 1L;
        long themeId = 1L;
        ReservationCreateRequest request = new ReservationCreateRequest(
                date,
                name,
                timeId,
                themeId
        );

        // when
        ReservationCreateResponse response = reservationService.createReservation(request);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.date()).isEqualTo(date);
        assertThat(response.name()).isEqualTo(name);
        assertThat(response.time().id()).isEqualTo(themeId);
        assertThat(response.theme().id()).isEqualTo(themeId);
    }

    @Test
    @DisplayName("id로 예약을 삭제한다.")
    void deleteReservation() {
        // given
        ReservationTime reservationTime = new ReservationTime(
                1L,
                LocalTime.of(10, 0)
        );
        Reservation reservation = new Reservation(
                1L,
                "브라운",
                LocalDate.of(2023, 8, 5),
                reservationTime,
                1L
        );
        reservationRepository.save(reservation);

        // when
        reservationService.deleteReservation(1L);

        // then
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations.size()).isEqualTo(0);
    }


    @DisplayName("존재하지 않는 timeId로 생성할 수 없다.")
    @Test
    void notExistTimeId() {
        // given
        LocalDate now = LocalDate.now();
        ReservationCreateRequest request = new ReservationCreateRequest(now.plusDays(1), "test", 1L, 1L);

        // when & then
        assertThatThrownBy(() -> {
            reservationService.createReservation(request);
        }).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("과거 날짜/시간 예약은 생성할 수 없다.")
    @Test
    void pastReservation() {
        // given
        LocalDate now = LocalDate.now();
        ReservationCreateRequest requestDto = new ReservationCreateRequest(now.minusDays(1), "test", 1L, 1L);

        // when & then
        assertThatThrownBy(() -> {
            reservationService.createReservation(requestDto);
        }).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("같은 날짜, 같은 시각에 이미 예약이 존재하는 경우, 재생성할 수 없다.")
    @Test
    @Disabled
    void duplicateReservation() {
        // given
        LocalDate now = LocalDate.now();
        LocalDate date = now.plusDays(1);

        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));
        Reservation reservation = new Reservation(1L, "test", date, reservationTime, 1L);
        reservationTimeRepository.save(reservationTime);
        reservationRepository.save(reservation);

        ReservationCreateRequest request = new ReservationCreateRequest(date, "test", reservationTime.getId(), 1L);

        // when & then
        assertThatThrownBy(() -> {
            reservationService.createReservation(request);
        }).isInstanceOf(ConflictException.class);
    }
}
