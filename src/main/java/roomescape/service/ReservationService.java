package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public ReservationService(
        final ReservationRepository reservationRepository,
        final TimeSlotRepository timeSlotRepository,
        final ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.themeRepository = themeRepository;
    }

    public Reservation reserve(final String name, final LocalDate date, final Long timeId, final Long themeId) {
        var timeSlot = findTimeSlot(timeId);
        var theme = findTheme(themeId);
        var reservation = new Reservation(name, date, timeSlot, theme);
        // TODO : now 테스트 어려움 해결하기
        validatePastDateTime(reservation);
        validateDuplicateReservation(reservation);
        var id = reservationRepository.save(reservation);
        return new Reservation(id, name, date, timeSlot, theme);
    }

    private void validatePastDateTime(final Reservation reservation) {
        if (reservation.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이전 날짜로 예약할 수 없습니다.");
        }
    }

    private void validateDuplicateReservation(final Reservation reservation) {
        var reservations = allReservations();
        var hasDuplicate = reservations.stream()
            .anyMatch(r -> r.isSameDateTime(reservation));
        if (hasDuplicate) {
            throw new IllegalArgumentException("이미 예약된 날짜와 시간에 대한 예약은 불가능합니다.");
        }
    }

    private TimeSlot findTimeSlot(final Long timeId) {
        return timeSlotRepository.findById(timeId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 타임 슬롯입니다."));
    }

    private Theme findTheme(final Long themeId) {
        return themeRepository.findById(themeId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    public boolean removeById(long id) {
        return reservationRepository.removeById(id);
    }
}
