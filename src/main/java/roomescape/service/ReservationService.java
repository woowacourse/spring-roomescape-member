package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationResponseDto;
import roomescape.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationResponseDto> findAllReservationResponses() {
        List<Reservation> allReservations = reservationRepository.findAll();

        return allReservations.stream()
                .map(reservation -> ReservationResponseDto.from(reservation, reservation.getTime(), reservation.getTheme()))
                .toList();
    }
}
