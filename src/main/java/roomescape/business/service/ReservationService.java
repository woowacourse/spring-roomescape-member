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
import roomescape.exception.auth.ForbiddenException;
import roomescape.exception.business.DuplicatedException;
import roomescape.exception.business.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public Reservation addAndGet(final LocalDate date, final String timeId, final String themeId, final String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));

        if (reservationRepository.isDuplicateDateAndTimeAndTheme(date, reservationTime.startAt(), theme)) {
            throw new DuplicatedException("해당 테마는 해당 시간에 이미 예약이 존재합니다.");
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
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));
        if (!reservation.isSameReserver(userId)) {
            throw new ForbiddenException();
        }
        reservationRepository.deleteById(reservationId);
    }
}
