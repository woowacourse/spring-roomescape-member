package roomescape.business.service;

import org.springframework.stereotype.Service;
import roomescape.business.model.entity.Reservation;
import roomescape.business.model.entity.ReservationTime;
import roomescape.business.model.entity.Theme;
import roomescape.exception.impl.AlreadyReservedException;
import roomescape.exception.impl.ReservationNotFoundException;
import roomescape.business.model.repository.ReservationRepository;
import roomescape.business.model.repository.ReservationTimeRepository;
import roomescape.business.model.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public Reservation createReservation(
            final String name,
            final LocalDate date,
            final long timeId,
            final long themeId
    ) {
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId);
        Theme theme = themeRepository.findById(themeId);

        if (reservationRepository.isDuplicateDateAndTimeAndTheme(date, reservationTime.getStartAt(), theme)) {
            throw new AlreadyReservedException();
        }

        Reservation reservation = Reservation.beforeSave(name, date, reservationTime, theme);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public void delete(final long id) {
        if (reservationRepository.findById(id) == null) {
            throw new ReservationNotFoundException();
        }
        reservationRepository.deleteById(id);
    }
}
