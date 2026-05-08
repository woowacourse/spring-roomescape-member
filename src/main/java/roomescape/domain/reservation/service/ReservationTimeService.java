package roomescape.domain.reservation.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.exception.ReservationTimeDeleteConflictException;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.domain.reservation.request.ReservationTimeCreateRequest;
import roomescape.domain.reservation.response.ReservationTimeResponse;
import roomescape.domain.reservation.response.ReservationTimesResponse;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimesResponse findAllReservationTimes() {
        List<ReservationTimeResponse> times = reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
        return new ReservationTimesResponse(times);
    }

    @Transactional
    public ReservationTimeResponse saveReservationTime(ReservationTimeCreateRequest request) {
        ReservationTime reservationTime = ReservationTime.create(request.startAt());

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
