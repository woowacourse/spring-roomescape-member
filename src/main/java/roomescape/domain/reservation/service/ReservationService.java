package roomescape.domain.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.dto.request.ReservationCreateRequestDTO;
import roomescape.domain.reservation.dto.response.ReservationResponseDTO;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
    }

    public List<ReservationResponseDTO> getReservations() {
        List<Reservation> reservations = reservationRepository.findAllReservations();
        return convertReservationsToDTO(reservations);
    }

    private List<ReservationResponseDTO> convertReservationsToDTO(List<Reservation> reservations) {
        return reservations.stream()
            .map(Reservation::toResponseDTO)
            .toList();
    }

    public ReservationResponseDTO saveReservation(ReservationCreateRequestDTO requestDTO) {
        Reservation reservation = createReservation(requestDTO);
        return reservationRepository.save(reservation).toResponseDTO();
    }

    private Reservation createReservation(ReservationCreateRequestDTO requestDTO) {
        Time time = timeRepository.findTimeById(requestDTO.timeId());
        return Reservation.create(requestDTO.name(), requestDTO.date(), time);
    }

    public void deleteReservationById(Long id) {
        reservationRepository.deleteReservationById(id);
    }
}
