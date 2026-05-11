package roomescape.reservation.service;

import static roomescape.reservation.domain.ReservationStatus.CANCELED;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> {
                    log.warn("Reservation time not found: id={}", timeId);
                    return new NotFoundException("존재하지 않는 예약 시간입니다.");
                });

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> {
                    log.warn("Theme not found: id={}", themeId);
                    return new NotFoundException("해당 테마가 존재하지 않습니다.");
                });

        if (closedDateRepository.existsByDate(date)) {
            log.warn("Closed date exists: date={}", date);
            throw new IllegalArgumentException("예약 불가능한 날짜입니다.");
        }

        LocalTime startAt = reservationTime.startAt();
        validateNotAlreadyBookedByOthers(date, startAt, theme);
        validateUserHasNoReservationAtSameTime(name, date, reservationTime);

        Reservation savedReservation = reservationRepository.save(
                Reservation.create(name, date, startAt, theme));

        log.info("Reservation created: name={}, date={}", name, date);
        return savedReservation;
    }

    private void validateNotAlreadyBookedByOthers(LocalDate date, LocalTime time, Theme theme) {
        if (reservationRepository.existsByDateAndTimeAndThemeId(date, time, theme.id())) {
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

    @Transactional
    public Reservation cancel(Long id) {
        Reservation reservation = getReservation(id);
        reservation.updateStatus(CANCELED);
        reservationRepository.updateStatus(reservation);
        log.info("Reservation canceled: id={}, name={}, date={}, time={}, theme={}", reservation.id(), reservation.name(), reservation.date(), reservation.time(), reservation.theme().name());
        return reservation;
    }

    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Reservation not found: id={}", id);
                    return new NotFoundException("존재하지 않는 예약입니다.");
                });
    }

}
