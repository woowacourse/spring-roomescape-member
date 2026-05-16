package roomescape.reservation.service;

import static roomescape.reservation.domain.ReservationStatus.CANCELED;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.closeddate.repository.ClosedDateRepository;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@Slf4j
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ClosedDateRepository closedDateRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ClosedDateRepository closedDateRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.closedDateRepository = closedDateRepository;
        this.themeRepository = themeRepository;
    }


    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllByName(String name) {
        return reservationRepository.findAllByNameOrderByDateAndTime(name);
    }

    @Transactional
    public Reservation create(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime reservationTime = getReservationTime(timeId);
        Theme theme = getTheme(themeId);

        validateNotClosedDate(date);
        validateNotAlreadyBookedByOthers(date, reservationTime.startAt(), theme);
        validateUserHasNoReservationAtSameTime(name, date, reservationTime);

        Reservation savedReservation = reservationRepository.save(
                Reservation.create(name, date, reservationTime.startAt(), theme));
        log.info("Reservation created: name={}, date={}", name, date);
        return savedReservation;
    }

    @Transactional
    public Reservation cancel(Long id) {
        Reservation reservation = getReservation(id);
        validateNotPastReservation(reservation, "취소");

        reservation.updateStatus(CANCELED);
        Reservation canceledReservation =  reservationRepository.updateStatus(reservation);
        log.info("Reservation canceled: id={}", canceledReservation.id());
        return canceledReservation;
    }

    @Transactional
    public Reservation change(Long id, LocalDate newDate, Long newTimeId) {
        Reservation reservation = getReservation(id);
        validateNotPastReservation(reservation, "변경");

        ReservationTime newTime = getReservationTime(newTimeId);
        validateNotClosedDate(newDate);
        validateNotPastDateTime(newDate, newTime.startAt());
        validateNotAlreadyBookedByOthers(newDate, newTime.startAt(), reservation.theme(), id);

        reservation.updateDateAndTime(newDate, newTime.startAt());
        Reservation changedReservation = reservationRepository.updateDateAndTime(reservation);
        log.info("Reservation changed: id={}, date={}, time={}", changedReservation.id(), changedReservation.theme(), changedReservation.time());
        return changedReservation;
    }

    @NonNull
    private ReservationTime getReservationTime(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> {
                    log.warn("Reservation time not found: id={}", timeId);
                    return new NotFoundException("존재하지 않는 예약 시간입니다.");
                });
    }

    @NonNull
    private Theme getTheme(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> {
                    log.warn("Theme not found: id={}", themeId);
                    return new NotFoundException("해당 테마가 존재하지 않습니다.");
                });
    }

    @NonNull
    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Reservation not found: id={}", id);
                    return new NotFoundException("존재하지 않는 예약입니다.");
                });
    }

    private void validateNotPastReservation(Reservation reservation, String action) {
        if (LocalDateTime.of(reservation.date(), reservation.time()).isBefore(LocalDateTime.now())) {
            log.warn("Cannot {} past reservation: id={}", action, reservation.id());
            throw new IllegalArgumentException("이미 지난 예약은 " + action + "할 수 없습니다.");
        }
    }

    private void validateNotClosedDate(LocalDate date) {
        if (closedDateRepository.existsByDate(date)) {
            log.warn("Cannot reserve closed date: date={}", date);
            throw new IllegalArgumentException("예약 불가능한 날짜입니다.");
        }
    }

    private void validateNotPastDateTime(LocalDate date, LocalTime time) {
        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now())) {
            log.warn("Cannot reserve past date/time: date={}, time={}", date, time);
            throw new IllegalArgumentException("과거 날짜/시간으로는 예약할 수 없습니다.");
        }
    }

    private void validateNotAlreadyBookedByOthers(LocalDate date, LocalTime time, Theme theme) {
        if (reservationRepository.existsByDateAndTimeAndThemeId(date, time, theme.id())) {
            log.warn("Reservation already exists: date={}, time={}, theme={}", date, time, theme.name());
            throw new ConflictException("해당 날짜/시간/테마는 이미 예약되었습니다.");
        }
    }

    private void validateNotAlreadyBookedByOthers(LocalDate date, LocalTime time, Theme theme, Long excludeId) {
        if (reservationRepository.existsByDateAndTimeAndThemeId(date, time, theme.id(), excludeId)) {
            log.warn("Reservation already exists: date={}, time={}, theme={}", date, time, theme.name());
            throw new ConflictException("해당 날짜/시간/테마는 이미 예약되었습니다.");
        }
    }

    private void validateUserHasNoReservationAtSameTime(String name, LocalDate date, ReservationTime time) {
        if (reservationRepository.existsByNameAndDateAndTime(name, date, time.startAt())) {
            log.warn("User already has a reservation at the same time: name={}, date={}, time={}", name, date, time.startAt());
            throw new ConflictException("동일한 날짜와 시간에 예약이 존재합니다.");
        }
    }
}
