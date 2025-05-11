package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.entity.Member;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.exception.impl.AlreadyReservedException;
import roomescape.exception.impl.ReservationNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

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
            final Member member,
            final LocalDate date,
            final long timeId,
            final long themeId
    ) {
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId);
        Theme theme = themeRepository.findById(themeId);

        if (reservationRepository.isDuplicateDateAndTimeAndTheme(date, reservationTime.getStartAt(), theme)) {
            throw new AlreadyReservedException();
        }

        Reservation reservation = Reservation.beforeSave(date, member, reservationTime, theme);
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
