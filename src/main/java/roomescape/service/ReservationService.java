package roomescape.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationResponse saveReservation(ReservationRequest request) {
        Reservation createdReservation = reservationRepository.saveReservation(request.toReservation());
        ReservationTime reservationTime = reservationTimeRepository.readReservationTime(createdReservation.getTimeId())
                .orElseThrow(() -> new IllegalArgumentException("올바른 예약 시간을 찾을 수 없습니다. 나중에 다시 시도해주세요."));

        return ReservationResponse.of(createdReservation, reservationTime);
    }

    public List<ReservationResponse> readReservation() {
        List<Reservation> reservations = reservationRepository.readReservations();
        List<ReservationResponse> reservationResponses = reservations.stream().map(reservation -> {
            ReservationTime reservationTime = reservationTimeRepository.readReservationTime(reservation.getTimeId())
                    .orElseThrow(() -> new IllegalStateException("더 이상 유효하지 않은 시간 ID의 예약입니다. 관리자가 해당 시간을 삭제했을 수 있습니다."));

            return ReservationResponse.of(reservation, reservationTime);
        }).toList();

        return reservationResponses;
    }

    public void delete(Long id) {
        reservationRepository.deleteReservation(id);
    }
}
