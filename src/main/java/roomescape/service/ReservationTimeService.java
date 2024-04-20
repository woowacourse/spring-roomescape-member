package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.request.ReservationTimeRequest;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeRepository.findAllReservationTimes();
    }

    public ReservationTime addReservationTime(ReservationTimeRequest request) {
        ReservationTime reservationTime = new ReservationTime(request.startAt());
        return reservationTimeRepository.addReservationTime(reservationTime);
    }

    public ReservationTime findReservationTime(long id) {
        return reservationTimeRepository.findReservationById(id);
    }

    public void deleteReservationTime(long id) {
        reservationTimeRepository.deleteReservationTime(id);
    }
}
