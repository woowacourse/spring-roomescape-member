package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.Reservation.ReservationCommand;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.Theme.Theme;
import roomescape.exception.DuplicatedReservationRequestException;
import roomescape.exception.ErrorMessage;
import roomescape.exception.NotFoundResourceException;
import roomescape.repository.Theme.ThemeRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

@Service
public class RoomReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public RoomReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> getAllReservation() {
        return reservationRepository.getAllReservation();
    }

    public List<Reservation> getAllReservationByName(String name) {
        return reservationRepository.getAllReservationByName(name);
    }

    @Transactional
    public Reservation addReservation(ReservationCommand reservationCommand) {
        ReservationTime reservationTime = reservationTimeRepository.getReservationTime(reservationCommand.timeId())
                .orElseThrow(() -> new NotFoundResourceException(ErrorMessage.INVALID_RESERVATION_TIME_ID));

        Theme theme = themeRepository.getTheme(reservationCommand.themeId())
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
