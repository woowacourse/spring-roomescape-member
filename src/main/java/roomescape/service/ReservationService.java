package roomescape.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
    }

    public Long addReservation(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = timeRepository.findById(reservationRequest.timeId())
                .orElseThrow(
                        () -> new IllegalArgumentException("[ERROR] 잘못된 id 입니다. : " + reservationRequest.timeId()));

        Optional<Reservation> optional = reservationRepository.findByDateAndTimeId(reservationRequest.date(),
                reservationTime.getId());

        if (optional.isPresent()) {
            throw new IllegalArgumentException("[ERROR] 이미 해당 시간에 예약이 존재합니다.");
        }

        Reservation reservationToSave = reservationRequest.toEntity(reservationTime);
        return reservationRepository.save(reservationToSave);
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse getReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id);
        return ReservationResponse.from(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }
}
