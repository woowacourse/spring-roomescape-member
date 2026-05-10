package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.dto.reservationTime.CreateReservationTimeRequest;
import roomescape.dto.reservationTime.ReservationTimeResponses;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponses getReservationTimes(int page, int size) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll(size + 1, page * size);
        boolean hasNext = reservationTimes.size() > size;
        if (hasNext) {
            reservationTimes = reservationTimes.subList(0, size);
        }
        return ReservationTimeResponses.of(reservationTimes, hasNext);
    }

    public ReservationTime getReservationTime(Long id) {
        return reservationTimeRepository.findById(id);
    }

    @Transactional
    public ReservationTime createReservationTime(CreateReservationTimeRequest request) {
        ReservationTime newReservationTime = new ReservationTime(null, request.startAt());
        Long newReservationTimeId = reservationTimeRepository.save(newReservationTime);
        return newReservationTime.withId(newReservationTimeId);
    }

    public void deleteReservationTime(Long id) {
        reservationTimeRepository.deleteById(id);
    }
}
