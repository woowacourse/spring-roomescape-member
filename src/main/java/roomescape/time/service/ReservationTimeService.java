package roomescape.time.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.time.controller.dto.request.CreateReservationTimeRequest;
import roomescape.time.controller.dto.response.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.time.repository.dto.CreateReservationTimeParams;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    @Transactional
    public ReservationTimeResponse addReservationTime(CreateReservationTimeRequest request) {
        CreateReservationTimeParams params = new CreateReservationTimeParams(request.startAt());
        ReservationTime savedReservationTime = reservationTimeRepository.save(params);

        return ReservationTimeResponse.from(savedReservationTime);
    }

    public List<ReservationTimeResponse> findAllReservationTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public void removeRegisteredReservationTime(Long id) {
        reservationTimeRepository.deleteById(id);
    }
}
