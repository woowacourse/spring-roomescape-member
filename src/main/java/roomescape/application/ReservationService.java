package roomescape.application;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final Clock clock;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              Clock clock) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.clock = clock;
    }

    @Transactional
    public ReservationResponse create(ReservationRequest request) {
        ReservationTime reservationTime = getReservationTime(request);
        LocalDateTime dateTime = LocalDateTime.of(request.date(), reservationTime.getStartAt());
        validateRequestDateAfterCurrentTime(dateTime);
        validateUniqueReservation(request);
        Reservation reservation = request.toReservation(reservationTime);
        return ReservationResponse.from(reservationRepository.save(reservation));
    }

    private ReservationTime getReservationTime(ReservationRequest request) {
        return reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간 입니다."));
    }

    private void validateRequestDateAfterCurrentTime(LocalDateTime dateTime) {
        LocalDateTime currentTime = LocalDateTime.now(clock);
        if (dateTime.isBefore(currentTime)) {
            throw new IllegalArgumentException("현재 시간보다 과거로 예약할 수 없습니다.");
        }
    }

    private void validateUniqueReservation(ReservationRequest request) {
        if (reservationRepository.existByNameAndDateAndTimeId(request.name(), request.date(), request.timeId())) {
            throw new IllegalStateException("이미 존재하는 예약입니다.");
        }
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return convertToReservationResponses(reservations);
    }

    private List<ReservationResponse> convertToReservationResponses(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public void deleteById(long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 입니다."));
        reservationRepository.deleteById(reservation.getId());
    }
}
