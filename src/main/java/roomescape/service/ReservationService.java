package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.reservation.ReservationCreateRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private static final String RESERVATION_NOT_FOUND = "존재하지 않는 예약입니다.";
    private static final String RESERVATION_TIME_NOT_FOUND = "존재하지 않는 예약 시간입니다.";

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponse> readReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse readReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESERVATION_NOT_FOUND));
        return ReservationResponse.from(reservation);
    }

    public ReservationResponse createReservation(ReservationCreateRequest request) {
        ReservationTime reservationTime = findReservationTimeById(request);
        Reservation reservation = request.toReservation(reservationTime);

        validateRequestedTime(reservation, reservationTime);

        Reservation newReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(newReservation);
    }

    private void validateRequestedTime(Reservation reservation, ReservationTime reservationTime) {
        LocalDateTime requestedDateTime = LocalDateTime.of(reservation.getDate(), reservationTime.getStartAt());
        if (requestedDateTime.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("이미 지난 날짜는 예약할 수 없습니다.");
        }
    }

    private ReservationTime findReservationTimeById(ReservationCreateRequest request) {
        return reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new ResourceNotFoundException(RESERVATION_TIME_NOT_FOUND));
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
