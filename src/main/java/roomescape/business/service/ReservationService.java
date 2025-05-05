package roomescape.business.service;

import org.springframework.stereotype.Service;
import roomescape.business.model.entity.Reservation;
import roomescape.business.model.entity.ReservationTime;
import roomescape.business.model.entity.Theme;
import roomescape.business.model.entity.User;
import roomescape.business.model.repository.ReservationRepository;
import roomescape.business.model.repository.ReservationTimeRepository;
import roomescape.business.model.repository.ThemeRepository;
import roomescape.business.model.repository.UserRepository;
import roomescape.exception.impl.AlreadyReservedException;
import roomescape.exception.impl.ReservationNotFoundException;
import roomescape.exception.impl.ThemeNotFoundException;
import roomescape.exception.impl.UserNotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            final UserRepository userRepository, ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository
    ) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public Reservation addAndGet(
            final LocalDate date,
            final long timeId,
            final long themeId,
            final String email
    ) {
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(ReservationNotFoundException::new);
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(ThemeNotFoundException::new);
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (reservationRepository.isDuplicateDateAndTimeAndTheme(date, reservationTime.getStartAt(), theme)) {
            throw new AlreadyReservedException();
        }

        Reservation reservation = Reservation.beforeSave(user, date, reservationTime, theme);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public void delete(final long id) {
        if (!reservationRepository.existById(id)) {
            throw new ReservationNotFoundException();
        }
        reservationRepository.deleteById(id);
    }
}
