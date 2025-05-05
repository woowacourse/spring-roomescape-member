package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.model.Reservation;
import roomescape.model.Theme;
import roomescape.model.TimeSlot;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeSlotRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
        final ReservationRepository reservationRepository,
        final TimeSlotRepository timeSlotRepository,
        final ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.themeRepository = themeRepository;
    }

    public Reservation reserve(final String name, final LocalDate date, final long timeId, final long themeId) {
        var timeSlot = findTimeSlot(timeId);
        var theme = findTheme(themeId);
        validateDuplicateReservation(date, timeSlot);

        var reservation = Reservation.reserveNewly(name, date, timeSlot, theme);
        var id = reservationRepository.save(reservation);
        return Reservation.of(id, name, date, timeSlot, theme);
    }

    private void validateDuplicateReservation(final LocalDate date, final TimeSlot timeSlot) {
        var reservations = findAllReservations();
        var isDuplicated = reservations.stream()
            .anyMatch(r -> r.isDateEquals(date) && r.isTimeSlotEquals(timeSlot));
        if (isDuplicated) {
            throw new IllegalArgumentException("이미 예약된 날짜와 시간에 대한 예약은 불가능합니다.");
        }
    }

    private TimeSlot findTimeSlot(final long timeId) {
        return timeSlotRepository.findById(timeId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 타임 슬롯입니다."));
    }

    private Theme findTheme(final long themeId) {
        return themeRepository.findById(themeId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }

    public boolean removeById(final long id) {
        return reservationRepository.removeById(id);
    }
}
