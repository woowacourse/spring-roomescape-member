package roomescape.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTime saveReservationTime(ReservationTimeRequest request) {
        return reservationTimeRepository.saveReservationTime(request.toReservationTime());
    }

    public List<ReservationTime> readReservationTime() {
        return reservationTimeRepository.readReservationTimes();
    }

    public void deleteReservationTime(Long id) {
        reservationTimeRepository.deleteReservationTime(id);
    }
}
