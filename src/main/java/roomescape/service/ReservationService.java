package roomescape.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.TimeSlot;
import roomescape.exception.InvalidOwnershipException;
import roomescape.exception.PastReservationControlException;
import roomescape.exception.PastTimeException;
import roomescape.exception.ReservationNotFoundException;
import roomescape.exception.ThemeNotFoundException;
import roomescape.exception.TimeSlotNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeSlotRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeSlotRepository timeSlotRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    public Reservation findReservationById(long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public List<Reservation> findReservationByName(String name) {
        return reservationRepository.findByName(name);
    }

    public Reservation saveReservation(String name, LocalDate date, Long timeId, Long themeId) {
        Reservation transientReservation = createTransientWithValidField(name, date, timeId, themeId);
        validDuplicatedReservation(transientReservation);
        return reservationRepository.save(transientReservation);
    }

    public void removeReservation(long reservationId, String userName) {
        existsAndModifiableReservation(reservationId, userName);
        reservationRepository.deleteById(reservationId);
    }

    public void putReservation(long id, String userName, @NotBlank String name, @NotNull LocalDate date,
                               @NotNull Long timeId, @NotNull Long themeId) {
        existsAndModifiableReservation(id, userName);
        Reservation transientReservation = createTransientWithValidField(name, date, timeId, themeId);
        Reservation reservation = new Reservation(id, transientReservation.name(), transientReservation.date(),
                transientReservation.timeSlot(), transientReservation.theme());
        validDuplicatedReservation(reservation);
        reservationRepository.update(reservation);
    }

    public void patchReservation(long id, String userName, String name, LocalDate date, Long timeId, Long themeId) {
        Reservation existing = findReservationById(id);
        existing.validateModifiable(userName, LocalDateTime.now());
        Reservation patched = existing.patch(
                name,
                date,
                findOptionalTime(timeId),
                findOptionalTheme(themeId),
                LocalDateTime.now()
        );
        validDuplicatedReservation(patched);
        reservationRepository.update(patched);
    }

    @NonNull
    private Reservation existsAndModifiableReservation(long id, String userName) {
        Reservation existingReservation = findReservationById(id);
        validOwnership(existingReservation.name(), userName);
        validNotPast(existingReservation.date(), existingReservation.timeSlot().startAt());
        return existingReservation;
    }

    private void validOwnership(String ownerName, String requesterName) {
        if (!ownerName.equals(requesterName)) {
            throw new InvalidOwnershipException();
        }
    }

    private void validNotPast(LocalDate date, LocalTime time) {
        if (date.isBefore(LocalDate.now()) || (date.isEqual(LocalDate.now()) && time.isBefore(LocalTime.now()))) {
            throw new PastReservationControlException();
        }
    }

    private Reservation createTransientWithValidField(String name, LocalDate date, Long timeId, Long themeId) {
        TimeSlot timeSlot = findTimeSlot(timeId);
        Theme theme = findTheme(themeId);
        validDateTime(date, timeSlot.startAt());
        return Reservation.transientOf(name, date, timeSlot, theme);
    }

    private void validDateTime(LocalDate date, LocalTime time) {
        if (date.isBefore(LocalDate.now())) {
            throw new PastTimeException("지난 날짜로 예약하실 수 없습니다.");
        }
        if (date.isEqual(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            throw new PastTimeException("지난 시간으로 예약하실 수 없습니다.");
        }
    }

    private void validDuplicatedReservation(Reservation reservation) {
        reservationRepository.findByDateAndTimeIdAndThemeId(reservation.date(), reservation.timeSlot().id(),
                        reservation.theme().id())
                .filter(existing -> reservation.id() == null || !reservation.id().equals(existing.id()))
                .ifPresent(existing -> {
                    throw new DuplicateKeyException("선택하신 시간과 테마는 이미 예약되었습니다.");
                });
    }

    private TimeSlot findOptionalTime(Long id) {
        return Optional.ofNullable(id).map(this::findTimeSlot).orElse(null);
    }

    private Theme findOptionalTheme(Long id) {
        return Optional.ofNullable(id).map(this::findTheme).orElse(null);
    }

    private TimeSlot findTimeSlot(Long id) {
        return timeSlotRepository.findById(id).orElseThrow(() -> new TimeSlotNotFoundException(id));
    }

    private Theme findTheme(Long id) {
        return themeRepository.findById(id).orElseThrow(() -> new ThemeNotFoundException(id));
    }
}
