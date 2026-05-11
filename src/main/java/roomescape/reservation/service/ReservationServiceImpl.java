package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.holiday.service.HolidayService;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;
import roomescape.time.service.TimeService;
import roomescape.theme.repository.ThemeRepository;

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


    @Override
    public Reservation create(ReservationSaveServiceDto reservation) {
        ReservationTime time = findTime(reservation.timeId());
        Long themeId = reservation.themeId();
        LocalDate date = reservation.date();
        validateThemeId(themeId);
        validateNotHoliday(date);
        validateDuplicatedReservation(themeId, time, date);
        Reservation newReservation = new Reservation(reservation.name(), date, time, themeId);
        Reservation saved = reservationRepository.save(newReservation);
        return saved.withTheme(themeRepository.findById(themeId));
    }

    private void validateThemeId(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("테마는 필수입니다.");
        }
        if (!themeRepository.existsById(themeId)) {
            throw new IllegalArgumentException("테마가 존재하지 않습니다. id=" + themeId);
        }
    }

    private void validateNotHoliday(LocalDate date) {
        if (holidayService.isHoliday(date)) {
            throw new IllegalArgumentException("휴일은 예약이 불가합니다.");
        }
    }

    private void validateDuplicatedReservation(Long themeId, ReservationTime time, LocalDate date) {
        if (reservationRepository.isDuplicated(themeId, time, date)) {
            throw new IllegalArgumentException("중복 예약은 불가합니다.");
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
        if (!reservationRepository.deleteById(id)) {
            throw new ReservationNotFoundException(id);
        }
    }
}
