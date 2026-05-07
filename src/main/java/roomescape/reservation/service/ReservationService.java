package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationService(
            final ReservationRepository reservationRepository,
            final ReservationTimeService reservationTimeService,
            final ThemeService themeService
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public Reservation save(final String name, final LocalDate date, final Long themeId, final Long timeId) {
        Theme theme = themeService.getById(themeId);
        ReservationTime reservationTime = reservationTimeService.getById(timeId);

        if(reservationRepository.existsByDateAndThemeIdAndTimeId(date, themeId, timeId)){
            throw new IllegalArgumentException("[ERROR] 동일한 시기에 예약을 할 수 없습니다.");
        }

        Reservation nonIdReservation = Reservation.createNew(name, date, theme, reservationTime);
        return reservationRepository.save(nonIdReservation);
    }

    public void deleteById(final long id) {
        reservationRepository.deleteById(id);
    }
}
