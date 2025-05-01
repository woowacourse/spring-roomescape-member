package roomescape.time.service;


import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.time.controller.dto.ReservationTimeRequest;
import roomescape.time.controller.dto.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse add(ReservationTimeRequest request) {
        ReservationTime newTime = request.toTimeWithoutId();
        if (reservationTimeRepository.existSameStartAt(request.startAt())) {
            throw new IllegalArgumentException("해당 시간은 이미 존재합니다.");
        }
        Long id = reservationTimeRepository.saveAndReturnId(request.toTimeWithoutId());
        return ReservationTimeResponse.from(newTime.withId(id));
    }

    public void remove(Long id) {
        if (!reservationRepository.existReservationByTimeId(id)){
            reservationTimeRepository.deleteById(id);
            return;
        }
        throw new IllegalStateException("해당 시간에 대한 예약이 존재합니다.");
    }

    public List<ReservationTimeResponse> getTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }


}
