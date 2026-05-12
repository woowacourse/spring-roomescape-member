package roomescape.domain.reservation.service;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.exception.ReservationTimeDeleteConflictException;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.domain.reservation.request.ReservationTimeCreateRequest;
import roomescape.domain.reservation.response.ReservationTimeResponse;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTimeResponse> findAllReservationTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public ReservationTimeResponse saveReservationTime(ReservationTimeCreateRequest request) {
        ReservationTime reservationTime = new ReservationTime(
                request.startAt()
        );

        ReservationTime savedTime = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResponse.from(savedTime);
    }

    @Transactional
    public void deleteReservationTimeBy(Long id) {
        try {
            reservationTimeRepository.deleteById(id);
        } catch (DataIntegrityViolationException exception) {
            throw new ReservationTimeDeleteConflictException(exception);
        }
    }
}
