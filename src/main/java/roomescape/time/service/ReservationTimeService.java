package roomescape.time.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.time.domain.ReservationTime;
import roomescape.time.controller.dto.CreateResrvationTimeRequest;
import roomescape.time.controller.dto.ReservationTimeResponse;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.time.repository.dto.CreateReservationTimeParams;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponse addReservationTime(CreateResrvationTimeRequest request) {
        CreateReservationTimeParams params = new CreateReservationTimeParams(request.getStartAt());
        ReservationTime savedReservationTime = reservationTimeRepository.save(params);

        return ReservationTimeResponse.from(savedReservationTime);
    }

    public List<ReservationTimeResponse> findAllReservationTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void removeRegisteredReservationTime(Long id) {
        reservationTimeRepository.deleteById(id);
    }
}
