package roomescape.time.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.time.controller.request.ReservationTimeCreateRequest;
import roomescape.time.controller.response.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;

@Service
public class ReservationTimeServiceImpl implements ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeServiceImpl(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponse create(ReservationTimeCreateRequest request) {
        ReservationTime reservationTime = ReservationTime.create(request.startAt());
        ReservationTime created = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResponse.from(created);
    }

    public List<ReservationTimeResponse> getAll() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        return ReservationTimeResponse.from(reservationTimes);
    }

    public void deleteById(Long id) {
        ReservationTime reservationTime = getReservationTime(id);
        reservationTimeRepository.deleteById(reservationTime.getId());
    }

    public ReservationTime getReservationTime(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 예약 시간을 찾을 수 없습니다."));
    }
}
