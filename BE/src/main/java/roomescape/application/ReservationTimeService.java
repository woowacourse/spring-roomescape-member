package roomescape.application;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ReservationTimeException;
import roomescape.entity.ReservationRepository;
import roomescape.entity.ReservationTime;
import roomescape.entity.ReservationTimeRepository;
import roomescape.presentation.dto.ReservationTimeRequest;
import roomescape.presentation.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            ReservationTimeRepository reservationTimeRepository, ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationTime saveTime(LocalTime startAt) {
        ReservationTime reservationTime = ReservationTime.createWithNullId(startAt);
        return reservationTimeRepository.save(reservationTime);
    }

    public List<ReservationTime> getTimes() {
        return reservationTimeRepository.findAll();
//        return times.stream()
//                .map(ReservationTimeResponse::from)
//                .toList();
    }

    @Transactional
    public void deleteTime(Long id) {
        if (reservationRepository.existsByReservationTimeId(id)) {
            throw new ReservationTimeException(ErrorCode.RESERVATION_TIME_ALREADY_USED);
        }
        reservationTimeRepository.deleteById(id);
    }
}
