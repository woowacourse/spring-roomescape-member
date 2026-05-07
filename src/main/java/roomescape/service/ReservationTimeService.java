package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.dto.reservationTime.CreateReservationTimeRequest;
import roomescape.repository.ReservationTimeJdbcRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeJdbcRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> getReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    @Transactional
    public ReservationTime createReservationTime(CreateReservationTimeRequest request) {
        ReservationTime newReservationTime = new ReservationTime(null, request.startAt());
        Long newReservationTimeId = reservationTimeRepository.save(newReservationTime);
        return reservationTimeRepository.findById(newReservationTimeId);
    }

    public void deleteReservationTime(Long id) {
        reservationTimeRepository.deleteById(id);
    }
}
