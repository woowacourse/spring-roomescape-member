package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.ReservationTimeCreateCommand;
import roomescape.service.dto.ReservationTimeResult;
import roomescape.service.exception.ReservationTimeConflictException;
import roomescape.service.exception.ReservationTimeInUseException;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            ReservationTimeRepository reservationTimeRepository,
            ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
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
        if (reservationRepository.existsByTimeId(id)) {
            throw new ReservationTimeInUseException(
                    "예약이 존재하는 시간은 삭제할 수 없습니다: timeId=" + id);
        }
        reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTimeResult> findAvailable(LocalDate date, Long themeId) {
        return reservationTimeRepository.findAvailable(date, themeId).stream()
                .map(ReservationTimeResult::from)
                .toList();
    }

}
