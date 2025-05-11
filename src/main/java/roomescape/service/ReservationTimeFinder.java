package roomescape.service;

import org.springframework.stereotype.Component;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

@Component
public class ReservationTimeFinder {
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeFinder(final ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTime getReservationTimeById(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다. id: " + id));
    }
}
