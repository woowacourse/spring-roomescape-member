package roomescape.service.reservationtime;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.SaveReservationTimeRequest;

@Service
public class ReservationTimeCreateService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeCreateService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTime createReservationTime(SaveReservationTimeRequest request) {
        ReservationTime newReservationTime = SaveReservationTimeRequest.toEntity(request);
        return reservationTimeRepository.save(newReservationTime);
    }
}
