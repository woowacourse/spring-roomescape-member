package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeEntity;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.ReservationTimeCreateCommand;
import roomescape.service.dto.ReservationTimeResult;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTimeResult> findAll() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResult::from)
                .toList();
    }

    public ReservationTimeResult create(ReservationTimeCreateCommand command) {
        ReservationTime time = new ReservationTime(command.getStartAt());

        ReservationTimeEntity saved = reservationTimeRepository.save(time);

        return ReservationTimeResult.from(saved);
    }

    public void delete(Long id) {
        reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTimeResult> findAvailable(LocalDate date, Long themeId) {
        return reservationTimeRepository.findAvailable(date, themeId).stream()
                .map(ReservationTimeResult::from)
                .toList();
    }

}
