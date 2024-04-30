package roomescape.time.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.RequestTime;
import roomescape.time.dto.ResponseTime;
import roomescape.time.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public Long save(RequestTime requestTime) {
        ReservationTime reservationTime = new ReservationTime(requestTime.startAt());
        return reservationTimeRepository.save(reservationTime);
    }

    public ResponseTime findById(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id);
        return new ResponseTime(reservationTime.getId(), reservationTime.getStartAt());
    }

    public List<ResponseTime> findAll() {
        return reservationTimeRepository.findAll().stream()
                .map(time -> new ResponseTime(time.getId(), time.getStartAt()))
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        reservationTimeRepository.delete(id);
    }
}
