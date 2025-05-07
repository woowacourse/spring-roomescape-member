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
import roomescape.exception.business.AlreadyReservedException;
import roomescape.exception.business.ReservationNotFoundException;
import roomescape.exception.business.ThemeNotFoundException;
import roomescape.exception.business.UserNotFoundException;

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

    public Reservation addAndGetWithEmail(
            final LocalDate date,
            final String timeId,
            final String themeId,
            final String userEmail
    ) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);
        return addAndGet(date, timeId, themeId, user);
    }

    public Reservation addAndGet(
            final LocalDate date,
            final String timeId,
            final String themeId,
            final String userId
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return addAndGet(date, timeId, themeId, user);
    }

    private Reservation addAndGet(final LocalDate date, final String timeId, final String themeId, final User user) {
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(ReservationNotFoundException::new);
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(ThemeNotFoundException::new);

        if (reservationRepository.isDuplicateDateAndTimeAndTheme(date, reservationTime.startAt(), theme)) {
            throw new AlreadyReservedException();
        }

        Reservation reservation = Reservation.create(user, date, reservationTime, theme);
        reservationRepository.save(reservation);
        return reservation;
    }

    public List<Reservation> getAll(final String themeId, final String userId, final LocalDate dateFrom, final LocalDate dateTo) {
        return reservationRepository.findAllWithFilter(themeId, userId, dateFrom, dateTo);
    }

    public void delete(final String id) {
        if (!reservationRepository.existById(id)) {
            throw new ReservationNotFoundException();
        }
        reservationRepository.deleteById(id);
    }
}
