package roomescape.application;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.application.dto.ReservationTimeCreationRequest;
import roomescape.domain.time.ReservationTime;
import roomescape.domain.time.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTime register(ReservationTimeCreationRequest request) {
        if (reservationTimeRepository.existsByStartAt(request.startAt())) {
            throw new IllegalArgumentException("이미 존재하는 예약 시간입니다.");
        }
        ReservationTime reservationTime = new ReservationTime(request.startAt());
        return reservationTimeRepository.save(reservationTime);
    }

    public ReservationTime getReservationTime(long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));
    }

    public List<ReservationTime> getReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    public void delete(long id) {
        ReservationTime reservationTime = getReservationTime(id);
        reservationTimeRepository.deleteById(reservationTime.getId());
    }
}
