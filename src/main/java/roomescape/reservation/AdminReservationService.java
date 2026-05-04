package roomescape.reservation;

import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.ReservationTimeException;
import roomescape.reservationtime.ReservationTimeNotFoundException;
import roomescape.reservationtime.ReservationTimeRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeNotFoundException;
import roomescape.theme.ThemeRepository;

@Service
public class AdminReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public AdminReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Reservation createForceReservation(Long themeId, String name, LocalDate date, Long timeId) {
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new ReservationTimeNotFoundException("해당 날짜를 찾을 수 없습니다."));

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException("해당 테마를 찾을 수 없습니다."));

        return reservationRepository.save(theme, name, date, reservationTime);
    }

    @Transactional
    public void forceDeleteReservation(long id) {
        reservationRepository.delete(id);
    }
}
