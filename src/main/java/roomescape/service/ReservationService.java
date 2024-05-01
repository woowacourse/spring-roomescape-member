package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.service.dto.ReservationRequestDto;
import roomescape.service.dto.ReservationResponseDto;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationResponseDto> findAllReservations() {
        return reservationRepository.findAllReservations()
                .stream()
                .map(ReservationResponseDto::new)
                .toList();
    }

    public ReservationResponseDto createReservation(ReservationRequestDto requestDto) {
        Reservation reservation = reservationRepository.insertReservation(requestDto.toReservation());
        return new ReservationResponseDto(reservation);
    }

    public void deleteReservation(long id) {
        if (!reservationRepository.isExistReservationOf(id)) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }
        reservationRepository.deleteReservationById(id);
    }
}
