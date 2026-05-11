package roomescape.reservation.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.application.dto.ReservationCreateCommand;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.exception.ReservationInUseException;
import roomescape.reservation.domain.exception.ReservationNotFoundException;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private static final int NONE_EFFECTED = 0;

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    @Transactional(readOnly = true)
    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public Reservation addReservation(ReservationCreateCommand command) {
        ReservationTime time = timeRepository.getById(command.timeId());
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
}
