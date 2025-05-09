package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.exception.exception.DataNotFoundException;
import roomescape.exception.exception.DeletionNotAllowedException;
import roomescape.repository.RoomescapeRepository;
import roomescape.repository.RoomescapeTimeRepository;

@Service
public class ReservationTimeService {

    private final RoomescapeRepository roomescapeRepository;
    private final RoomescapeTimeRepository timeRepository;

    public ReservationTimeService(final RoomescapeRepository roomescapeRepository,
                                  RoomescapeTimeRepository timeRepository) {
        this.roomescapeRepository = roomescapeRepository;
        this.timeRepository = timeRepository;
    }

    public List<ReservationTimeResponse> findReservationTimes() {
        List<ReservationTime> reservationTimes = timeRepository.findAll();
        return reservationTimes.stream().map(ReservationTimeResponse::of).toList();
    }

    public ReservationTimeResponse addReservationTime(final ReservationTimeRequest request) {
        ReservationTime reservationTime = new ReservationTime(request.startAt());
        ReservationTime saved = timeRepository.save(reservationTime);
        return ReservationTimeResponse.of(saved);
    }

    public void removeReservationTime(final long timeId) {
        if (roomescapeRepository.existsByTimeId(timeId)) {
            throw new DeletionNotAllowedException("[ERROR] 예약이 연결된 시간은 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요.");
        }
        if (!timeRepository.deleteById(timeId)) {
            throw new DataNotFoundException(String.format("[ERROR] 예약 시간 %d번에 해당하는 시간이 없습니다.", timeId));
        }
    }

}
