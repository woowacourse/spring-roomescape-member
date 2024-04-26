package roomescape.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.dto.app.ReservationTimeAppRequest;

@Service
public class ReservationTimeService {
    
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTime save(ReservationTimeAppRequest request) {
        ReservationTime newReservationTime = new ReservationTime(request.startAt());

        return reservationTimeRepository.save(newReservationTime);
    }

    public void delete(Long id) {
        reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }
}
