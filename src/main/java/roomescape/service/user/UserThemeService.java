package roomescape.service.user;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;

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
