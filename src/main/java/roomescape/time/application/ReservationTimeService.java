package roomescape.time.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.global.exception.exceptions.NotExistingEntryException;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeRepository;
import roomescape.time.dto.ReservationTimeCreateRequest;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.dto.ReservationTimeResponses;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponses findAll() {
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
        return new ReservationTimeResponses(reservationTimeResponses);
    }

    public ReservationTimeResponse findByReservationTimeId(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findByTimeId(id);
        return ReservationTimeResponse.from(reservationTime);
    }

    public ReservationTimeResponse create(ReservationTimeCreateRequest reservationTimeCreateRequest) {
        ReservationTime reservationTime = new ReservationTime(reservationTimeCreateRequest.startAt());
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);
        return ReservationTimeResponse.from(savedReservationTime);
    }

    public void delete(Long id) {
        if (reservationTimeRepository.deleteById(id) == 0) {
            throw new NotExistingEntryException("삭제할 예약 시간이 존재하지 않습니다");
        }
    }
}
