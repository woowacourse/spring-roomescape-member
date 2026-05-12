package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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

class ReservationServiceTest {

    private ReservationService reservationService;
    private TimeSlot savedTimeSlot;
    private Theme savedTheme;

    @BeforeEach
    void setUp() {
        FakeTimeSlotRepository fakeReservationTimeRepository = new FakeTimeSlotRepository();
        FakeReservationRepository fakeReservationRepository = new FakeReservationRepository();
        FakeThemeRepository fakeThemeRepository = new FakeThemeRepository();
        reservationService = new ReservationService(fakeReservationRepository, fakeReservationTimeRepository,
                fakeThemeRepository);
        savedTimeSlot = fakeReservationTimeRepository.save(TimeSlot.transientOf(LocalTime.of(10, 0)));
        savedTheme = fakeThemeRepository.save(Theme.transientOf("이름", "설명", "test.com"));
    }

    @Test
    @DisplayName("원시값을 받아 연관된 객체를 조회하여 조립한 뒤 예약을 생성한다.")
    void saveReservation() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Reservation reservation = reservationService.saveReservation("브라운", futureDate, savedTimeSlot.id(),
                savedTheme.id());
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
        Reservation reservation = reservationService.saveReservation("브라운", futureDate, savedTimeSlot.id(),
                savedTheme.id());
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
        Reservation savedReservation = reservationService.saveReservation("브라운", futureDate, savedTimeSlot.id(),
                savedTheme.id());
        Reservation foundReservation = reservationService.findReservationById(savedReservation.id());
        assertThat(foundReservation.name()).isEqualTo("브라운");
    }
}
