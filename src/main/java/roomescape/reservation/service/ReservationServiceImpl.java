package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import roomescape.holiday.service.HolidayService;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.exception.DuplicateReservationException;
import roomescape.reservation.exception.PastReservationException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.service.TimeService;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeService timeService;
    private final ThemeRepository themeRepository;
    private final HolidayService holidayService;

    public ReservationServiceImpl(
            ReservationRepository reservationRepository,
            TimeService timeService,
            ThemeRepository themeRepository,
            HolidayService holidayService
    ) {
        this.reservationRepository = reservationRepository;
        this.timeService = timeService;
        this.themeRepository = themeRepository;
        this.holidayService = holidayService;
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    @Override
    public Reservation create(ReservationSaveServiceDto reservation) {
        ReservationTime time = findTime(reservation.timeId());
        Long themeId = reservation.themeId();
        LocalDate date = reservation.date();
        validatePast(date, time);
        validateThemeId(themeId);
        validateNotHoliday(date);
        validateDuplicatedReservation(themeId, time, date);
        Reservation newReservation = new Reservation(reservation.name(), date, time, themeId);
        Reservation saved = reservationRepository.save(newReservation);
        return saved.withTheme(themeRepository.findById(themeId));
    }

    private void validatePast(LocalDate date, ReservationTime time) {
        if (date.isBefore(LocalDate.now())) {
            throw PastReservationException.pastDate();
        }

        if (date.equals(LocalDate.now()) && time.isStartBefore(LocalTime.now())) {
            throw PastReservationException.pastTime();
        }
    }

    private void validateThemeId(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("테마는 필수입니다.");
        }
        if (!themeRepository.existsById(themeId)) {
            throw new ThemeNotFoundException(themeId);
        }
    }

    private void validateNotHoliday(LocalDate date) {
        if (holidayService.isHoliday(date)) {
            throw new IllegalArgumentException("휴일은 예약이 불가합니다.");
        }
    }

    private void validateDuplicatedReservation(Long themeId, ReservationTime time, LocalDate date) {
        if (reservationRepository.isDuplicated(themeId, time, date)) {
            throw new DuplicateReservationException();
        }
    }

    private ReservationTime findTime(Long timeId) {
        if (timeId == null) {
            throw new IllegalArgumentException("예약 시간은 필수입니다.");
        }

        return timeService.findById(timeId);
    }

    @Override
    public void cancel(Long id) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        reservationRepository.deleteById(id);
    }

    @Override
    public List<Reservation> getByName(String name) {
        return reservationRepository.findByName(name);
    }

    @Override
    public void cancelForUser(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        validateNotPastForCancel(reservation);
        reservationRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Reservation update(Long id, LocalDate date, Long timeId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        validatePastReservation(reservation.getDate(), reservation.getTime());
        ReservationTime newTime = findTime(timeId);
        validatePast(date, newTime);
        validateDuplicatedReservation(reservation.getThemeId(), newTime, date);
        reservationRepository.update(id, date, timeId);
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    private void validatePastReservation(LocalDate date, ReservationTime time) {
        if (isPast(date, time)) {
            throw PastReservationException.pastUpdate();
        }
    }

    private boolean isPast(LocalDate date, ReservationTime time) {
        return date.isBefore(LocalDate.now()) || (date.equals(LocalDate.now()) && time.isStartBefore(LocalTime.now()));
    }

    private void validateNotPastForCancel(Reservation reservation) {
        LocalDate date = reservation.getDate();
        LocalTime startAt = reservation.getTime().getStartAt();
        if (date.isBefore(LocalDate.now())) {
            throw PastReservationException.pastCancel();
        }
        if (date.equals(LocalDate.now()) && startAt.isBefore(LocalTime.now())) {
            throw PastReservationException.pastCancel();
        }
    }
}
