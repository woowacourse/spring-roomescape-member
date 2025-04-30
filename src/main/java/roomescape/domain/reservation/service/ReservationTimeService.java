package roomescape.domain.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.exception.AlreadyInUseException;
import roomescape.domain.reservation.dto.ReservationTimeRequest;
import roomescape.domain.reservation.dto.ReservationTimeResponse;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTimeResponse> getAll() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse create(ReservationTimeRequest request) {
        ReservationTime reservationTime = ReservationTime.withoutId(request.startAt());

        ReservationTime saved = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResponse.from(saved);
    }

    public void delete(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new AlreadyInUseException("Reservation is already in use");
        }

        reservationTimeRepository.deleteById(id);
    }
}
