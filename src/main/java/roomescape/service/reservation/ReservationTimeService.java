package roomescape.service.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.reservation.ReservationTimeRequest;
import roomescape.dto.reservation.ReservationTimeResponse;
import roomescape.entity.ReservationTime;
import roomescape.exceptions.reservation.ReservationTimeDuplicateException;
import roomescape.repository.reservation.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTimeResponse> readAllReservationTime() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public ReservationTimeResponse postReservationTime(ReservationTimeRequest request) {
        if (reservationTimeRepository.existsByStartAt(request.startAt())) {
            throw new ReservationTimeDuplicateException("중복된 예약 시간이 존재합니다.", request.startAt());
        }

        ReservationTime newReservation = reservationTimeRepository.save(request.toEntity());
        return ReservationTimeResponse.from(newReservation);
    }

    @Transactional
    public void deleteReservationTime(long id) {
        reservationTimeRepository.deleteById(id);
    }
}
