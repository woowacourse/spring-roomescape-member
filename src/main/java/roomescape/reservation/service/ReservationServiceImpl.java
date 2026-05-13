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
import roomescape.reservation.service.dto.ReservationUpdateServiceDto;
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
    public List<Reservation> findByName(String name) {
        return reservationRepository.findByName(name);
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
    public Reservation update(ReservationUpdateServiceDto dto) {
        Reservation existing = reservationRepository.findById(dto.id())
                .orElseThrow(() -> new ReservationNotFoundException(dto.id()));
        validateOwner(existing, dto.requesterName());

        ReservationTime newTime = timeService.findById(dto.timeId());
        Theme newTheme = findTheme(dto.themeId());
        validatePastReservation(dto.date(), newTime);
        validateHoliday(dto.date());
        validateDuplicatedReservation(newTheme.getId(), newTime, dto.date());

        reservationRepository.update(dto.id(), dto.date(), newTime.getId(), newTheme.getId());
        return reservationRepository.findById(dto.id())
                .orElseThrow(() -> new ReservationNotFoundException(dto.id()));
    }

    @Override
    public void cancel(Long id) {
        if (!reservationRepository.deleteById(id)) {
            throw new ReservationNotFoundException(id);
        }
    }

    @Override
    public void cancel(Long id, String requesterName) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        validateOwner(reservation, requesterName);
        validatePastCancel(reservation.getDate(), reservation.getTime());
        reservationRepository.deleteById(id);
    }

    private void validateOwner(Reservation reservation, String requesterName) {
        if (!reservation.getName().equals(requesterName)) {
            throw new RoomescapeException(ErrorCode.RESERVATION_OWNER_MISMATCH);
        }
    }

    private void validatePastCancel(LocalDate date, ReservationTime time) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        if (reservationDateTime.isBefore(LocalDateTime.now(clock))) {
            throw new RoomescapeException(ErrorCode.PAST_RESERVATION_CANCEL_NOT_ALLOWED);
        }
    }
}
