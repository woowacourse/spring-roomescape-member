package roomescape.reservation.application;

import java.time.Clock;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.application.dto.ReservationChangeCommand;
import roomescape.reservation.application.dto.ReservationCreateCommand;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.application.exception.ReservationInUseException;
import roomescape.reservation.application.exception.ReservationNotFoundException;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private static final int NONE_EFFECTED = 0;

    private final Clock clock;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    @Transactional(readOnly = true)
    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByName(String username) {
        return reservationRepository.findAllByName(username);
    }

    public Reservation addReservation(ReservationCreateCommand command) {
        ReservationTime time = timeRepository.getById(command.timeId());
        time.checkRegisterable(command.date(), clock);
        Theme theme = themeRepository.getById(command.themeId());
        if (reservationRepository.existsByReservationTimeAndThemeAndDate(time.getId(), theme.getId(), command.date())) {
            throw new ReservationInUseException("이미 예약이 존재합니다.");
        }
        return reservationRepository.save(command.toEntity(time, theme));
    }

    public void deleteReservation(Long id) {
        if (reservationRepository.deleteById(id) == NONE_EFFECTED) {
            throw new ReservationNotFoundException("존재하지 않는 예약ID 입니다.");
        }
    }

    public void cancelReservation(Long id, String username) {
        if (!reservationRepository.existsByIdAndUsernameAndActive(id, username)) {
            throw new ReservationNotFoundException("해당 예약을 찾을 수 없거나 취소할 권한이 없습니다.");
        }
        reservationRepository.cancelById(id);
    }

    public Reservation changeReservation(Long id, ReservationChangeCommand command) {
        Reservation reservation = reservationRepository.getById(id);
        reservation.checkChangeable(command.username(), clock);
        ReservationTime time = timeRepository.getById(command.timeId());
        time.checkChangeable(command.date(), clock);
        Theme theme = themeRepository.getById(command.themeId());
        if (reservationRepository.existsByReservationTimeAndThemeAndDate(time.getId(), theme.getId(), command.date())) {
            throw new ReservationInUseException("이미 다른 예약이 존재합니다.");
        }
        Reservation changedReservation = reservation.changeTime(command.date(), time, theme);
        reservationRepository.updateByIdAndUsername(id, command.username(), changedReservation);
        return reservation;
    }
}
