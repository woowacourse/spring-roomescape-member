package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.ReservationTimeCreateCommand;
import roomescape.service.dto.ReservationTimeResult;
import roomescape.service.exception.ReservationTimeConflictException;
import roomescape.service.exception.ReservationTimeInUseException;
import roomescape.service.exception.ReservationTimeNotFoundException;
import roomescape.service.exception.ThemeNotFoundException;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ReservationTimeService(
            ReservationTimeRepository reservationTimeRepository,
            ReservationRepository reservationRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationTimeResult> findAll() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResult::from)
                .toList();
    }

    public ReservationTimeResult create(ReservationTimeCreateCommand command) {
        if (reservationTimeRepository.existsByStartAt(command.startAt())) {
            throw new ReservationTimeConflictException(
                    "이미 등록된 시간입니다: " + command.startAt());
        }
        ReservationTime saved = reservationTimeRepository.save(new ReservationTime(null, command.startAt()));
        return ReservationTimeResult.from(saved);
    }

    public void delete(Long id) {
        if (!reservationTimeRepository.existsById(id)) {
            throw new ReservationTimeNotFoundException("존재하지 않는 시간입니다: timeId=" + id);
        }
        if (reservationRepository.existsByTimeId(id)) {
            throw new ReservationTimeInUseException(
                    "예약이 존재하는 시간은 삭제할 수 없습니다: timeId=" + id);
        }
        reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTimeResult> findAvailable(LocalDate date, Long themeId) {
        if (!themeRepository.existsById(themeId)) {
            throw new ThemeNotFoundException("존재하지 않는 테마입니다: themeId=" + themeId);
        }
        return reservationTimeRepository.findAvailable(date, themeId).stream()
                .map(ReservationTimeResult::from)
                .toList();
    }

}
