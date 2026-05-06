package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.ReservationTimeCreateCommand;
import roomescape.service.dto.ReservationTimeResult;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime create(ReservationTimeCreateCommand command) {
        return reservationTimeRepository.save(new ReservationTime(null, command.getStartAt()));
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
