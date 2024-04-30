package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationRepository;
import roomescape.dao.ReservationTimeRepository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.ReservationRequest;
import roomescape.service.dto.ReservationResponse;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationResponse create(final ReservationRequest reservationRequest) {
        validateDuplicated(reservationRequest);
        ReservationTime reservationTime = findTimeById(reservationRequest.timeId());
        Reservation reservation = new Reservation(reservationRequest.name(), reservationRequest.date(),
                reservationTime);
        Reservation newReservation = reservationRepository.save(reservation);
        return new ReservationResponse(newReservation);
    }

    private void validateDuplicated(ReservationRequest reservationRequest) {
        if (reservationRepository.existsByDateAndTime(reservationRequest.date(), reservationRequest.timeId())) {
            throw new InvalidReservationException("이미 같은 일정으로 예약이 존재합니다.");
        }
    }

    private ReservationTime findTimeById(final long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(
                        () -> new InvalidReservationException("존재하지 않는 예약 시간입니다. id: " + timeId));
    }


    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }


    public void deleteById(final long id) {
        reservationRepository.deleteById(id);
    }
}
