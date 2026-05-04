package roomescape.reservation.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.exception.ReservationException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.service.ReservationTimeService;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;

    public List<ReservationResponse> findAllReservations() {
        List<Reservation> result = reservationRepository.findAll();

        return result.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse saveReservation(ReservationCreateRequest request) {
        ReservationTime reservationTime = reservationTimeService.findById(request.timeId());
        validateDuplicateReservation(request);
        Reservation reservation = request.toEntity(reservationTime);

        return ReservationResponse.from(reservationRepository.save(reservation));
    }

    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }

    private void validateDuplicateReservation(ReservationCreateRequest request) {
        Boolean existsByDateAndTime = reservationRepository.existsByDateAndTime(request.date(), request.timeId());
        if (existsByDateAndTime) {
            throw new ReservationException("[ERROR] 이미 해당 날짜와 시간에 예약이 존재합니다.");
        }
    }
}
