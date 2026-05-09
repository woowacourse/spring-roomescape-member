package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.projection.ReservationEntity;
import roomescape.repository.projection.ReservationTimeEntity;
import roomescape.repository.projection.ThemeEntity;
import roomescape.service.dto.ReservationCreateCommand;
import roomescape.service.dto.ReservationResult;

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


    public List<ReservationResult> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResult::from)
                .toList();
    }

    public ReservationResult create(ReservationCreateCommand command) {
        ReservationTimeEntity timeEntity = reservationTimeRepository.findById(command.getTimeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다: " + command.getTimeId()));

        ThemeEntity themeEntity = themeRepository.findById(command.getThemeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다: " + command.getThemeId()));

        Reservation reservation = new Reservation(
                command.getName(),
                command.getDate(),
                timeEntity.getTime(),
                themeEntity.getTheme()
        );

        ReservationEntity saved = reservationRepository.save(
                reservation,
                timeEntity.getId(),
                themeEntity.getId()
        );
        return ReservationResult.from(saved);
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}
