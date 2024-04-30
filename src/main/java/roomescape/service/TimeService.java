package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.time.TimeRequest;
import roomescape.controller.time.TimeResponse;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.util.List;

@Service
public class TimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;

    public TimeService(final ReservationRepository reservationRepository, final ReservationTimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
    }

    public List<TimeResponse> getTimes() {
        return timeRepository.findAll().stream()
                .map(TimeResponse::from)
                .toList();
    }

    public TimeResponse addTime(final TimeRequest timeRequest) {
        ReservationTime parsedTime = timeRequest.toDomain();
        ReservationTime savedTime = timeRepository.save(parsedTime);
        return TimeResponse.from(savedTime);
    }

    public int deleteTime(final Long id) {
//        reservationRepository.findByTimeId(id);
        return timeRepository.deleteById(id);
    }
}
