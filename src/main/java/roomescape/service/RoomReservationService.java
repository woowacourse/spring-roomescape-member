package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTheme.ReservationTheme;
import roomescape.exception.DuplicatedReservationRequestException;
import roomescape.exception.ErrorMessage;
import roomescape.exception.NotFoundResourceException;
import roomescape.repository.reservationTheme.ReservationThemeRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

@Service
public class RoomReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;

    public RoomReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ReservationThemeRepository reservationThemeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationThemeRepository = reservationThemeRepository;
    }

    public List<Reservation> getAllReservation(String name) {
        return reservationRepository.getAllReservation(name);
    }

    @Transactional
    public Reservation addReservation(ReservationCommand reservationCommand) {
        ReservationTime reservationTime = reservationTimeRepository.getReservationTime(reservationCommand.timeId())
                .orElseThrow(() -> new NotFoundResourceException(ErrorMessage.INVALID_RESERVATION_TIME_ID));

        ReservationTheme theme = reservationThemeRepository.getTheme(reservationCommand.themeId())
                .orElseThrow(() -> new NotFoundResourceException(ErrorMessage.INVALID_THEME_ID));

        if (reservationRepository.existsByTimeIdAndThemeIdAndDate(reservationCommand.timeId(), reservationCommand.themeId(), reservationCommand.date())) {
            throw new DuplicatedReservationRequestException(ErrorMessage.DUPLICATED_RESERVATION_REQUEST);
        }

        return reservationRepository.addReservation(reservationCommand, reservationTime, theme);
    }

    public void deleteReservation(long id) {
        reservationRepository.deleteReservation(id);
    }
}
