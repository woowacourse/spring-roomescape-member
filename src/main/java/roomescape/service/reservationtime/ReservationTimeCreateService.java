package roomescape.service.reservationtime;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.SaveReservationTimeRequest;

@Service
public class ReservationTimeCreateService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeCreateService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTime createReservationTime(SaveReservationTimeRequest request) {
        if (reservationTimeRepository.findByStartAt(request.startAt()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 예약 시간입니다.");
        }

        ReservationTime newReservationTime = SaveReservationTimeRequest.toEntity(request);
        return reservationTimeRepository.save(newReservationTime);
    }
}
