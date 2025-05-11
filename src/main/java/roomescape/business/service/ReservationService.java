package roomescape.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.business.model.entity.Reservation;
import roomescape.business.model.entity.ReservationTime;
import roomescape.business.model.entity.Theme;
import roomescape.business.model.entity.User;
import roomescape.business.model.repository.ReservationRepository;
import roomescape.business.model.repository.ReservationTimeRepository;
import roomescape.business.model.repository.ThemeRepository;
import roomescape.business.model.repository.UserRepository;
import roomescape.exception.auth.AuthorizationException;
import roomescape.exception.business.DuplicatedException;
import roomescape.exception.business.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static roomescape.exception.ErrorCode.RESERVATION_DUPLICATED;
import static roomescape.exception.ErrorCode.RESERVATION_NOT_EXIST;
import static roomescape.exception.ErrorCode.THEME_NOT_EXIST;
import static roomescape.exception.ErrorCode.USER_NOT_EXIST;
import static roomescape.exception.SecurityErrorCode.AUTHORITY_LACK;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public Reservation addAndGet(final LocalDate date, final String timeId, final String themeId, final String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundException(RESERVATION_NOT_EXIST));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException(THEME_NOT_EXIST));

        if (reservationRepository.isDuplicateDateAndTimeAndTheme(date, reservationTime.startAt(), theme)) {
            throw new DuplicatedException(RESERVATION_DUPLICATED);
        }

        Reservation reservation = Reservation.create(user, date, reservationTime, theme);
        reservationRepository.save(reservation);
        return reservation;
    }

    public List<Reservation> getAll(final String themeId, final String userId, final LocalDate dateFrom, final LocalDate dateTo) {
        return reservationRepository.findAllWithFilter(themeId, userId, dateFrom, dateTo);
    }

    public void delete(final String reservationId, final String userId) {
        final Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(RESERVATION_NOT_EXIST));
        if (!reservation.isSameReserver(userId)) {
            throw new AuthorizationException(AUTHORITY_LACK);
        }
        reservationRepository.deleteById(reservationId);
    }
}
