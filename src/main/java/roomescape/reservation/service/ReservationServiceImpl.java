package roomescape.reservation.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.error.ErrorCode;
import roomescape.error.RoomescapeException;
import roomescape.holiday.service.HolidayService;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.TimeService;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeService timeService;
    private final ThemeRepository themeRepository;
    private final HolidayService holidayService;
    private final Clock clock;

    public ReservationServiceImpl(
            ReservationRepository reservationRepository,
            TimeService timeService,
            ThemeRepository themeRepository,
            HolidayService holidayService,
            Clock clock
    ) {
        this.reservationRepository = reservationRepository;
        this.timeService = timeService;
        this.themeRepository = themeRepository;
        this.holidayService = holidayService;
        this.clock = clock;
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation create(ReservationSaveServiceDto reservation) {
        validateRequiredFields(reservation);

        ReservationTime time = timeService.findById(reservation.timeId());
        Theme theme = findTheme(reservation.themeId());
        LocalDate date = reservation.date();

        validatePastReservation(date, time);
        validateHoliday(date);
        validateDuplicatedReservation(theme.getId(), time, date);

        Reservation newReservation = new Reservation(
                reservation.name(),
                date,
                time,
                theme
        );
        return reservationRepository.save(newReservation);
    }

    private void validateRequiredFields(ReservationSaveServiceDto reservation) {
        if (reservation.name() == null || reservation.name().isBlank()) {
            throw new RoomescapeException(ErrorCode.INVALID_REQUEST);
        }
        if (reservation.date() == null || reservation.themeId() == null || reservation.timeId() == null) {
            throw new RoomescapeException(ErrorCode.INVALID_REQUEST);
        }
    }

    private void validatePastReservation(LocalDate date, ReservationTime time) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        if (reservationDateTime.isBefore(LocalDateTime.now(clock))) {
            throw new RoomescapeException(ErrorCode.PAST_RESERVATION_NOT_ALLOWED);
        }
    }

    private void validateHoliday(LocalDate date) {
        if (holidayService.isHoliday(date)) {
            throw new RoomescapeException(ErrorCode.INVALID_REQUEST);
        }
    }

    private void validateDuplicatedReservation(Long themeId, ReservationTime time, LocalDate date) {
        if (reservationRepository.isDuplicated(themeId, time, date)) {
            throw new RoomescapeException(ErrorCode.DUPLICATE_RESERVATION);
        }
    }

    private Theme findTheme(Long themeId) {
        if (!themeRepository.existsById(themeId)) {
            throw new ThemeNotFoundException(themeId);
        }
        return themeRepository.findById(themeId);
    }

    @Override
    public void cancel(Long id) {
        if (!reservationRepository.deleteById(id)) {
            throw new ReservationNotFoundException(id);
        }
    }
}
