package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.exception.ResourceNotFoundException;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(final ReservationRepository reservationRepository, final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse saveReservation(final ReservationSaveRequest reservationSaveRequest) {
        final ReservationTime time = reservationTimeRepository.findById(reservationSaveRequest.timeId())
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 예약 시간입니다."));
        final Reservation reservation = new Reservation(reservationSaveRequest.name(), reservationSaveRequest.date(), time);
        final Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    public void deleteReservation(final Long id) {
        final boolean isDeleted = reservationRepository.deleteById(id);
        if (isDeleted) {
            return;
        }
        throw new ResourceNotFoundException("존재하지 않는 예약 시간입니다.");
    }
}
