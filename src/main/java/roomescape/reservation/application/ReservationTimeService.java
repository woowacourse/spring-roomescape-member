package roomescape.reservation.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.infra.ReservationTimeRepository;
import roomescape.reservation.presentation.dto.request.ReservationTimeSaveRequest;
import roomescape.reservation.presentation.dto.response.ReservationTimeFindResponse;
import roomescape.reservation.presentation.dto.response.ReservationTimeSaveResponse;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeSaveResponse save(ReservationTimeSaveRequest body) {
        ReservationTime reservationTime = reservationTimeRepository.save(body.startAt());

        return new ReservationTimeSaveResponse(reservationTime.getId(), reservationTime.getStartAt());
    }

    public List<ReservationTimeFindResponse> findAll(){
        return reservationTimeRepository.findAll().stream()
                .map(reservationTime -> new ReservationTimeFindResponse(
                        reservationTime.getId(),
                        reservationTime.getStartAt()
                ))
                .toList();
    }

    public void delete(Long id){
        reservationTimeRepository.deleteById(id);
    }
}
