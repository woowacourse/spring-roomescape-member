package roomescape.reservation.service;


import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.controller.dto.ReservationTimeRequest;
import roomescape.reservation.controller.dto.ReservationTimeResponse;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponse add(ReservationTimeRequest request) {
        ReservationTime newTime = request.toTimeWithoutId();
        Long id = reservationTimeRepository.saveAndReturnId(request.toTimeWithoutId());
        return ReservationTimeResponse.from(newTime.withId(id));
    }

    public void remove(Long id) {
        reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTimeResponse> getTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

}
