package roomescape.reservation.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.ReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.getAll().stream()
                .map(reservation -> {
                    ReservationTime time = reservation.getTime();
                    return ReservationResponse.from(reservation, time);
                })
                .toList();
    }

    public void delete(long id) {
        if (!reservationRepository.deleteById(id)) {
            throw new IllegalArgumentException("요청한 id와 일치하는 예약 정보가 없습니다.");
        }
    }

    public ReservationResponse create(final ReservationCreateRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new NoSuchElementException("요청한 id와 일치하는 예약 시간 정보가 없습니다."));
        Reservation newReservation = reservationRepository.put(
                Reservation.withUnassignedId(request.name(), request.date(), time));
        return ReservationResponse.from(newReservation, time);
    }
}
