package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.TimeSlot;
import roomescape.repository.FakeReservationRepository;
import roomescape.repository.FakeThemeRepository;
import roomescape.repository.FakeTimeSlotRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationServiceTest {

    private ReservationService reservationService;
    private FakeReservationRepository reservationRepository;
    private FakeTimeSlotRepository timeSlotRepository; // 타입 오타 수정
    private FakeThemeRepository themeRepository;

    private TimeSlot savedTimeSlot;
    private Theme savedTheme;

    @BeforeEach
    void setUp() {
        timeSlotRepository = new FakeTimeSlotRepository();
        reservationRepository = new FakeReservationRepository();
        themeRepository = new FakeThemeRepository();

        reservationService = new ReservationService(reservationRepository, timeSlotRepository, themeRepository);

        savedTimeSlot = timeSlotRepository.save(TimeSlot.transientOf(LocalTime.of(10, 0)));
        savedTheme = themeRepository.save(Theme.transientOf("이름", "설명", "test.com"));
    }

    @Test
    @DisplayName("원시값을 받아 연관된 객체를 조회하여 조립한 뒤 예약을 생성한다.")
    void saveReservation() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Reservation reservation = reservationService.saveReservation("브라운", futureDate, savedTimeSlot.id(), savedTheme.id());
        assertThat(reservation.timeSlot().startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("중복된 예약을 생성하려 하면 예외가 발생한다.")
    void saveReservationDuplicate() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        reservationService.saveReservation("브라운", futureDate, savedTimeSlot.id(), savedTheme.id());

        assertThatThrownBy(
                () -> reservationService.saveReservation("토미", futureDate, savedTimeSlot.id(), savedTheme.id()))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessage("선택하신 시간과 테마는 이미 예약되었습니다.");
    }

    @Test
    @DisplayName("존재하는 예약을 식별자를 통해 삭제하면 목록에서 사라진다.")
    void removeReservation() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Reservation reservation = reservationService.saveReservation("브라운", futureDate, savedTimeSlot.id(), savedTheme.id());
        reservationService.removeReservation(reservation.id());
        assertThat(reservationService.allReservations()).isEmpty();
    }

    @Test
    @DisplayName("모든 예약 목록을 조회하여 반환한다.")
    void allReservations() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        reservationService.saveReservation("브라운", futureDate, savedTimeSlot.id(), savedTheme.id());
        List<Reservation> reservations = reservationService.allReservations();
        assertThat(reservations).hasSize(1);
    }

    @Test
    @DisplayName("식별자를 통해 특정 예약 객체를 조회한다.")
    void findReservation() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Reservation savedReservation = reservationService.saveReservation("브라운", futureDate, savedTimeSlot.id(), savedTheme.id());
        Reservation foundReservation = reservationService.findReservationById(savedReservation.id());
        assertThat(foundReservation.name()).isEqualTo("브라운");
    }

    @Test
    @DisplayName("지나간 날짜에 대한 예약 생성은 불가능하다.")
    void saveReservation_PastDate() {
        LocalDate pastDate = LocalDate.now().minusDays(1);

        assertThatThrownBy(
                () -> reservationService.saveReservation("브라운", pastDate, savedTimeSlot.id(), savedTheme.id()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 날짜로 예약하실 수 없습니다.");
    }

    @Test
    @DisplayName("지나간 시간에 대한 예약 생성은 불가능하다.")
    void saveReservation_PastTime() {
        LocalDate today = LocalDate.now();
        LocalTime pastTime = LocalTime.now().minusHours(1);
        TimeSlot pastTimeSlot = timeSlotRepository.save(TimeSlot.transientOf(pastTime));

        assertThatThrownBy(
                () -> reservationService.saveReservation("브라운", today, pastTimeSlot.id(), savedTheme.id()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 시간으로 예약하실 수 없습니다.");
    }

    @Test
    @DisplayName("이미 지난 예약을 삭제하려고 시도하면 예외가 발생한다.")
    void removeReservation_Past() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        Reservation pastReservation = reservationRepository.save(
                Reservation.transientOf("브라운", pastDate, savedTimeSlot, savedTheme)
        );

        assertThatThrownBy(() -> reservationService.removeReservation(pastReservation.id()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 지난 예약은 수정/삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("이미 지난 예약을 전체 수정(PUT)하려고 시도하면 예외가 발생한다.")
    void putReservation_Past() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        Reservation pastReservation = reservationRepository.save(
                Reservation.transientOf("브라운", pastDate, savedTimeSlot, savedTheme)
        );

        assertThatThrownBy(() -> reservationService.putReservation(
                pastReservation.id(), "네오", LocalDate.now().plusDays(1), savedTimeSlot.id(), savedTheme.id()
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 지난 예약은 수정/삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("이미 지난 예약을 부분 수정(PATCH)하려고 시도하면 예외가 발생한다.")
    void patchReservation_Past() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        Reservation pastReservation = reservationRepository.save(
                Reservation.transientOf("브라운", pastDate, savedTimeSlot, savedTheme)
        );

        assertThatThrownBy(() -> reservationService.patchReservation(
                pastReservation.id(), "네오", null, null, null
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 지난 예약은 수정/삭제할 수 없습니다.");
    }
}
