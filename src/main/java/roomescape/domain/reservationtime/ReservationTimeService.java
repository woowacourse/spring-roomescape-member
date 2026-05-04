package roomescape.domain.reservationtime;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservationtime.dto.CreateTimeRequest;
import roomescape.domain.reservationtime.dto.CreateTimeResponse;
import roomescape.domain.reservationtime.dto.ReservationTimeResponse;
import roomescape.support.exception.ReservationTimeErrorCode;
import roomescape.support.exception.RoomescapeException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public CreateTimeResponse createReservationTime(CreateTimeRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.save(request.toEntity());
        return CreateTimeResponse.from(reservationTime);
    }

    public List<ReservationTimeResponse> getAllReservationTime() {
        return reservationTimeRepository.findAll().stream()
            .map(ReservationTimeResponse::from)
            .toList();
    }

    public void deleteReservationTime(Long id) {
        if (reservationRepository.countByTimeId(id) > 0) {
            throw new RoomescapeException(ReservationTimeErrorCode.RESERVATION_TIME_IN_USE);
        }
        int deletedCount = reservationTimeRepository.deleteById(id);
        if (deletedCount == 0) {
            log.warn("이미 삭제된 예약 시간 삭제 요청이 들어왔습니다. timeId={}", id);
        }
    }
}
