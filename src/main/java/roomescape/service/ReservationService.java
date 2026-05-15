package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(readOnly = true)
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
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public List<Reservation> findReservationByName(String name) {
        return reservationRepository.findByName(name);
    }

    @Transactional
    public Reservation saveReservation(String name, LocalDate date, Long timeId, Long themeId) {
        Reservation transientReservation = createTransientWithValidField(name, date, timeId, themeId);
        validDuplicatedReservation(transientReservation);
        return reservationRepository.save(transientReservation);
    }

    @Transactional
    public void removeReservation(long reservationId, String userName) {
        existsAndModifiableReservation(reservationId, userName);
        reservationRepository.deleteById(reservationId);
    }

    @Transactional
    public void putReservation(long id, String userName, String name, LocalDate date, Long timeId, Long themeId) {
        existsAndModifiableReservation(id, userName);
        Reservation transientReservation = createTransientWithValidField(name, date, timeId, themeId);
        Reservation reservation = new Reservation(
                id, transientReservation.getName(),
                transientReservation.getDate(),
                transientReservation.getTimeSlot(),
                transientReservation.getTheme()
        );
        validDuplicatedReservation(reservation);
        reservationRepository.update(reservation);
    }

    @Transactional
    public void patchReservation(long id, String userName, String name, LocalDate date, Long timeId, Long themeId) {
        Reservation existing = findReservationById(id);
        existing.validateModifiable(userName, LocalDateTime.now());
        Reservation patched = existing.reschedule(
                name,
                date,
                findOptionalTime(timeId),
                findOptionalTheme(themeId),
                LocalDateTime.now()
        );
        validDuplicatedReservation(patched);
        reservationRepository.update(patched);
    }

    private void existsAndModifiableReservation(long id, String userName) {
        Reservation existingReservation = findReservationById(id);
        validOwnership(existingReservation.getName(), userName);
        validNotPast(existingReservation.getDate(), existingReservation.getTimeSlot().getStartAt());
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
        validDateTime(date, timeSlot.getStartAt());
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
        reservationRepository.findByDateAndTimeIdAndThemeId(
                        reservation.getDate(),
                        reservation.getTimeSlot().getId(),
                        reservation.getTheme().getId()
                )
                .filter(existing -> reservation.getId() == null || !reservation.getId().equals(existing.getId()))
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
