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
        ReservationTime reservationTime = reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다"));

        return new ResponseTime(reservationTime.getId(), reservationTime.getStartAt());
    }

    public List<ResponseTime> findAll() {
        return reservationTimeRepository.findAll().stream()
                .map(time -> new ResponseTime(time.getId(), time.getStartAt()))
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        findById(id);
        if (reservationTimeRepository.findReservationInSameId(id).isPresent()) {
            throw new IllegalArgumentException("이미 해당 시간에 예약이 있습니다.");
        }
        reservationTimeRepository.delete(id);
    }
}
