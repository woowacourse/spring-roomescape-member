package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.holiday.service.HolidayService;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;
import roomescape.time.service.TimeService;
import roomescape.theme.domain.Theme;
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
        Theme theme = findTheme(reservation.themeId());
        Long themeId = theme.getId();
        LocalDate date = reservation.date();
        if (holidayService.isHoliday(reservation.date())) {
            throw new IllegalArgumentException("휴일은 예약이 불가합니다.");
        }
        validateDuplicatedReservation(themeId, time, date);
        Reservation newReservation = new Reservation(
                reservation.name(),
                reservation.date(),
                time,
                theme
        );
        return reservationRepository.save(newReservation);
    }

    private void validateDuplicatedReservation(Long themeId, ReservationTime time, LocalDate date) {
        if (isAlreadyReserved(themeId, time, date)) {
            throw new IllegalArgumentException("중복 예약은 불가합니다.");
        }
    }

    private boolean isAlreadyReserved(Long themeId, ReservationTime time, LocalDate date) {
        return reservationRepository.isDuplicated(themeId, time, date);
    }

    private ReservationTime findTime(Long timeId) {
        if (timeId == null) {
            throw new IllegalArgumentException("예약 시간은 필수입니다.");
        }
        return timeService.findById(timeId);
    }

    private Theme findTheme(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("테마는 필수입니다.");
        }
        if (!themeRepository.existsById(themeId)) {
            throw new IllegalArgumentException("테마가 존재하지 않습니다. id=" + themeId);
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
