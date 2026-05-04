package roomescape.domain.reservation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.dto.CreateReservationRequest;
import roomescape.domain.reservation.dto.CreateReservationResponse;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRepository;
import roomescape.support.exception.ReservationTimeErrorCode;
import roomescape.support.exception.RoomescapeException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public CreateReservationResponse createReservation(CreateReservationRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
            .orElseThrow(() -> new RoomescapeException(ReservationTimeErrorCode.RESERVATION_TIME_NOT_EXIST));
        Reservation savedReservation = reservationRepository.save(request.toEntity(reservationTime));
        return CreateReservationResponse.from(savedReservation);
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
            .map(ReservationResponse::from)
            .toList();
    }

    public void deleteReservation(Long id) {
        int deletedCount = reservationRepository.deleteById(id);
        if (deletedCount == 0) {
            log.warn("이미 삭제된 예약 삭제 요청이 들어왔습니다. reservationId={}", id);
        }
    }
}
