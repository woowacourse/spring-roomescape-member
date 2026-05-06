package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.ReservationCreateCommand;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation create(ReservationCreateCommand command) {
        ReservationTime time = reservationTimeRepository.findById(command.getTimeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다: " + command.getTimeId()));

        Theme theme = themeRepository.findById(command.getThemeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다: " + command.getThemeId()));

        Reservation reservation = new Reservation(null, command.getName(), command.getDate(), time, theme);
        return reservationRepository.save(reservation);
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}
