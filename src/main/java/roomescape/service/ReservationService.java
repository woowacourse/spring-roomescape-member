package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTimeRepository;
import roomescape.service.dto.ReservationRequestDto;
import roomescape.service.dto.ReservationResponseDto;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponseDto> findAllReservations() {
        return reservationRepository.findAllReservations()
                .stream()
                .map(ReservationResponseDto::new)
                .toList();
    }

    public ReservationResponseDto createReservation(ReservationRequestDto requestDto) {
        Reservation reservation = requestDto.toReservation();
        if (!reservationTimeRepository.isExistTimeOf(reservation.getTimeId())) {
            throw new IllegalArgumentException("예약 하려는 시간이 저장되어 있지 않습니다.");
        }
        if (reservationRepository.isExistReservationAtDateTime(reservation)) {
            throw new IllegalArgumentException("같은 시간에 이미 예약이 존재합니다.");
        }
        Reservation savedReservation = reservationRepository.insertReservation(reservation);
        return new ReservationResponseDto(savedReservation);
    }

    public void deleteReservation(long id) {
        if (!reservationRepository.isExistReservationOf(id)) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }
        reservationRepository.deleteReservationById(id);
    }
}
