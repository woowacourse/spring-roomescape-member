package roomescape.reservation.application.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.global.exception.DuplicateReservationException;
import roomescape.global.exception.GetTimeException;
import roomescape.global.exception.PastTimeException;
import roomescape.reservation.application.dto.CreateReservationRequest;
import roomescape.reservation.application.repository.ReservationRepository;
import roomescape.reservation.application.repository.ReservationTimeRepository;
import roomescape.reservation.domain.aggregate.ReservationDate;
import roomescape.reservation.domain.aggregate.ReservationName;
import roomescape.reservation.domain.aggregate.ReservationTime;
import roomescape.reservation.presentation.dto.ReservationRequest;
import roomescape.reservation.presentation.dto.ReservationResponse;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationResponse createReservation(final ReservationRequest reservationRequest) {
        ReservationDate reservationDate = new ReservationDate(reservationRequest.getDate());
        ReservationTime reservationTime = getReservationTime(reservationRequest.getTimeId());
        validateReservationDateTime(reservationDate, reservationTime);

        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                new ReservationName(reservationRequest.getName()),
                reservationDate,
                reservationTime
        );

        return new ReservationResponse(reservationRepository.insert(createReservationRequest));
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAllReservations().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public void deleteReservation(final Long id) {
        reservationRepository.delete(id);
    }

    private ReservationTime getReservationTime(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new GetTimeException("[ERROR] 예약 시간 정보를 찾을 수 없습니다."));
    }

    private void validateReservationDateTime(ReservationDate reservationDate, ReservationTime reservationTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate.getReservationDate(),
                reservationTime.getStartAt());

        validateIsPast(reservationDateTime);
        validateIsDuplicate(reservationDateTime);
    }

    private static void validateIsPast(LocalDateTime reservationDateTime) {
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new PastTimeException("[ERROR] 지난 일시에 대한 예약 생성은 불가능합니다.");
        }
    }

    private void validateIsDuplicate(LocalDateTime reservationDateTime) {
        if(reservationRepository.existsByDateTime(reservationDateTime)){
            throw new DuplicateReservationException("[ERROR] 중복된 일시의 예약은 불가능합니다.");
        }
    }
}
