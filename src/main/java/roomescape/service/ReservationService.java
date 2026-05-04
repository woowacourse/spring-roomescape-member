package roomescape.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(
        ReservationRepository reservationRepository,
        ReservationTimeRepository reservationTimeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationResponse createReservation(ReservationRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "[ERROR] 존재하지 않는 time id입니다.")
            );
        Reservation reservation = Reservation.of(
            request.name(),
            request.date(),
            time
        );
        Reservation saved = reservationRepository.save(reservation);
        return ReservationResponse.from(saved);
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        List<ReservationResponse> responses = new ArrayList<>();
        for (Reservation reservation : reservations) {
            ReservationResponse response = ReservationResponse.from(reservation);
            responses.add(response);
        }
        return responses;
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
