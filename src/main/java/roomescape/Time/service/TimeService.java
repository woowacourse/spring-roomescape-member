package roomescape.Time.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.Time.domain.Time;
import roomescape.Time.dto.ReservationTimeRequest;
import roomescape.Time.dto.ReservationTimeResponse;
import roomescape.Time.repository.TimeRepository;

@Service
public class TimeService {
    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public ReservationTimeResponse addReservationTime(ReservationTimeRequest reservationTimeRequest) {
        Time reservationTime = new Time(reservationTimeRequest.startAt());
        Time savedReservationTime = timeRepository.saveReservationTime(reservationTime);

        return toResponse(savedReservationTime);
    }

    public List<ReservationTimeResponse> findReservationTimes() {
        List<Time> reservationTimes = timeRepository.findAllReservationTimes();

        return reservationTimes.stream()
                .map(this::toResponse)
                .toList();
    }

    public void removeReservationTime(long reservationTimeId) {
        timeRepository.deleteReservationTimeById(reservationTimeId);
    }

    public ReservationTimeResponse toResponse(Time time) {
        return new ReservationTimeResponse(time.getId(), time.getStartAt());
    }
}
