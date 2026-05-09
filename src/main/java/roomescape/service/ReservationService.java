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
import roomescape.service.dto.ReservationResult;
import roomescape.service.exception.ReservationConflictException;

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
        ReservationTime time = reservationTimeRepository.findById(command.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다: " + command.timeId()));

        Theme theme = themeRepository.findById(command.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다: " + command.themeId()));

        validateNoConflict(command);

        Reservation reservation = new Reservation(null, command.name(), command.date(), time, theme);
        Reservation saved = reservationRepository.save(reservation);
        return ReservationResult.from(saved);
    }

    private void validateNoConflict(ReservationCreateCommand command) {
        boolean conflict = reservationRepository.existsByDateAndTimeIdAndThemeId(
                command.date(), command.timeId(), command.themeId());
        if (conflict) {
            throw new ReservationConflictException(
                    "이미 예약된 시간입니다: %s, timeId=%d, themeId=%d"
                            .formatted(command.date(), command.timeId(), command.themeId())
            );
        }
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}
