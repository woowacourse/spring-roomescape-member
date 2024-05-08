package roomescape.theme.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;

@Service
public class UserThemeService {

    private final ReservationRepository reservationRepository;

    public UserThemeService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Theme> getThemeRanking() {
        return reservationRepository.findThemeOrderByReservationCount();
    }
}
