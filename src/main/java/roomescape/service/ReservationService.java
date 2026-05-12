package roomescape.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.TimeSlot;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeSlotRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            TimeSlotRepository timeSlotRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    public Reservation findReservationById(long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 식별자로 데이터를 찾을 수 없습니다. id: " + id));
    }

    public Reservation saveReservation(
            String name,
            LocalDate date,
            Long timeId,
            Long themeId
    ) {
        validDate(date);
        validDuplicatedReservation(date, timeId, themeId);
        TimeSlot timeSlot = timeSlotRepository.findById(timeId)
                .orElseThrow(() -> new NoSuchElementException("해당 식별자로 데이터를 찾을 수 없습니다. id: " + timeId));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NoSuchElementException("해당 식별자로 데이터를 찾을 수 없습니다. id: " + themeId));
        Reservation transientReservation = Reservation.transientOf(name, date, timeSlot, theme);
        return reservationRepository.save(transientReservation);
    }

    public void removeReservation(long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    private void validDate(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("지난 날짜로 예약하실 수 없습니다.");
        }
    }

    private void validDuplicatedReservation(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new DuplicateKeyException("선택하신 시간과 테마는 이미 예약되었습니다.");
        }
    }

    public void putReservation(
            long id,
            @NotBlank(message = "이름은 필수입니다.") String name,
            @NotNull(message = "날짜는 필수입니다.") LocalDate date,
            @NotNull Long timeId,
            @NotNull Long themeId
    ) {
        validDate(date);
        validDuplicatedReservation(date, timeId, themeId);
        TimeSlot timeSlot = timeSlotRepository.findById(timeId)
                .orElseThrow(() -> new NoSuchElementException("해당 식별자로 데이터를 찾을 수 없습니다. id: " + timeId));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NoSuchElementException("해당 식별자로 데이터를 찾을 수 없습니다. id: " + themeId));
        Reservation reservation = new Reservation(id, name, date, timeSlot, theme);
        reservationRepository.update(reservation);
    }

    public void patchReservation(long id, String name, LocalDate date, Long timeId, Long themeId) {
        Reservation reservation = findReservationById(id);
        TimeSlot timeSlot = findOptionalTime(timeId);
        Theme theme = findOptionalTheme(themeId);
        Reservation patched = reservation.patch(name, date, timeSlot, theme);
        validDate(patched.date());
        reservationRepository.update(patched);
    }

    private TimeSlot findOptionalTime(Long id) {
        return Optional.ofNullable(id).map(this::findTimeSlot).orElse(null);
    }

    private Theme findOptionalTheme(Long id) {
        return Optional.ofNullable(id).map(this::findTheme).orElse(null);
    }

    private TimeSlot findTimeSlot(Long id) {
        return timeSlotRepository.findById(id).orElseThrow();
    }

    private Theme findTheme(Long id) {
        return themeRepository.findById(id).orElseThrow();
    }
}
