package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.exception.DuplicatedReservationRequestException;
import roomescape.exception.ErrorMessage;
import roomescape.exception.NotFoundResourceException;
import roomescape.repository.theme.ThemeRepository;
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

    @Transactional
    public void deleteReservation(long id) {
        reservationRepository.deleteReservation(id);
    }
}
