package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.ForbiddenReservationException;
import roomescape.exception.InvalidInputException;
import roomescape.exception.NotFoundException;
import roomescape.exception.PastReservationException;
import roomescape.exception.PastReservationLockedException;
import roomescape.exception.UnchangedReservationException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.result.TimeAvailabilityResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> findByName(String name) {
        return reservationRepository.findByName(name);
    }

    @Transactional
    public Reservation create(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = findReservationTime(timeId);
        validateNotPast(date, time);
        validateAlreadyReserved(date, timeId, themeId);
        Theme theme = findTheme(themeId);
        Reservation reservation = new Reservation(null, name, date, time, theme);
        Long id = reservationRepository.insert(reservation);
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("생성된 예약을 찾을 수 없습니다."));
    }

    @Transactional
    public void delete(Long id, String name) {
        Reservation reservation = findReservation(id);
        validateOwner(reservation, name);
        validateReservationNotLocked(reservation);
        reservationRepository.delete(id);
    }

    @Transactional
    public Reservation update(Long id, String name, LocalDate date, Long timeId) {
        Reservation reservation = findReservation(id);
        validateUpdatableReservation(reservation, name);

        Reservation updatedReservation = createUpdatedReservation(reservation, date, timeId);
        validateUpdatePolicy(reservation, updatedReservation);

        reservationRepository.update(updatedReservation);
        return findUpdatedReservation(id);
    }

    public List<TimeAvailabilityResult> findAvailableTime(Long themeId, LocalDate date) {
        validateThemeExists(themeId);
        List<ReservationTime> times = reservationTimeRepository.findAll();
        List<Reservation> reservations = reservationRepository.findReservationsByThemeAndDate(themeId, date);

        return times.stream()
                .map(time -> new TimeAvailabilityResult(
                        time.getId(),
                        time.getStartAt(),
                        isAvailable(time, reservations)
                ))
                .toList();
    }

    private ReservationTime findReservationTime(Long timeId) {
        return reservationTimeRepository.findBy(timeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 시간입니다."));
    }

    private Reservation findReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));
    }

    private Reservation findUpdatedReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정된 예약을 찾을 수 없습니다."));
    }

    private void validateNotPast(LocalDate date, ReservationTime time) {
        if (isPast(date, time)) {
            throw new PastReservationException("이미 지난 시간으로는 예약할 수 없습니다.");
        }
    }

    private void validateReservationNotLocked(Reservation reservation) {
        if (reservation.isPast()) {
            throw new PastReservationLockedException("이미 지난 예약은 변경하거나 취소할 수 없습니다.");
        }
    }

    private boolean isPast(LocalDate date, ReservationTime time) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        return reservationDateTime.isBefore(LocalDateTime.now());
    }

    private boolean isAvailable(ReservationTime time, List<Reservation> reservations) {
        return reservations.stream()
                .noneMatch(reservation -> reservation.hasTime(time));
    }

    private void validateAlreadyReserved(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsWith(date, timeId, themeId)) {
            throw new DuplicateReservationException("이미 예약된 시간입니다.");
        }
    }

    private void validateUpdatableReservation(Reservation reservation, String name) {
        validateOwner(reservation, name);
        validateReservationNotLocked(reservation);
    }

    private void validateUpdatePolicy(Reservation reservation, Reservation updatedReservation) {
        validateScheduleChanged(reservation, updatedReservation);
        validateNotPast(updatedReservation.getDate(), updatedReservation.getTime());
        validateAlreadyReserved(
                updatedReservation.getDate(),
                updatedReservation.getTime().getId(),
                updatedReservation.getTheme().getId());
    }

    private void validateScheduleChanged(Reservation reservation, Reservation updatedReservation) {
        if (reservation.hasSameSchedule(updatedReservation)) {
            throw new UnchangedReservationException("기존 예약과 같은 날짜·시간으로는 변경할 수 없습니다.");
        }
    }

    private void validateOwner(Reservation reservation, String name) {
        if (!reservation.isOwnedBy(name)) {
            throw new ForbiddenReservationException("본인의 예약만 변경하거나 취소할 수 있습니다.");
        }
    }

    private Reservation createUpdatedReservation(Reservation reservation, LocalDate date, Long timeId) {
        validateUpdateValueExists(date, timeId);

        return new Reservation(
                reservation.getId(),
                reservation.getName(),
                resolveUpdateDate(reservation, date),
                resolveUpdateTime(reservation, timeId),
                reservation.getTheme());
    }

    private LocalDate resolveUpdateDate(Reservation reservation, LocalDate date) {
        if (date != null) {
            return date;
        }
        return reservation.getDate();
    }

    private ReservationTime resolveUpdateTime(Reservation reservation, Long timeId) {
        if (timeId != null) {
            return findReservationTime(timeId);
        }
        return reservation.getTime();
    }

    private void validateUpdateValueExists(LocalDate date, Long timeId) {
        if (date == null && timeId == null) {
            throw new InvalidInputException("변경할 날짜 또는 시간이 필요합니다.");
        }
    }

    private void validateThemeExists(Long themeId) {
        findTheme(themeId);
    }

    private Theme findTheme(Long themeId) {
        return themeRepository.findBy(themeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));
    }
}
