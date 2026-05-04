package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Reservation save(ReservationCommand command) {
        ReservationTime time = reservationTimeRepository.findById(command.timeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 예약 시간이 존재하지 않습니다."));

        Theme theme = themeRepository.findById(command.themeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 테마가 존재하지 않습니다."));

        Reservation reservation = Reservation.create(command.name(), command.date(), time, theme);

        if (reservationRepository.existsByDateAndTimeIdAndThemeId(reservation.getDate(), command.timeId(),
                command.themeId())) {
            throw new IllegalStateException("이미 해당 날짜와 시간에 예약이 존재합니다.");
        }

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public List<Theme> findPopularThemes(int period, int limit) {
        return reservationRepository.findPopularThemes(period, limit);

    }
}
