package roomescape.application;

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
    public ReservationTimeResponse saveTime(ReservationTimeRequest request) {
        ReservationTime reservationTime = ReservationTime.createWithNullId(
                request.startAt()
        );
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);
        return ReservationTimeResponse.from(savedReservationTime);
    }

    public List<ReservationTimeResponse> getTimes() {
        List<ReservationTime> times = reservationTimeRepository.findAll();
        return times.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public void deleteTime(Long id) {
        if (id == null) {
            throw new ReservationTimeException(ErrorCode.RESERVATION_TIME_ID_NULL);
        }
        if (reservationRepository.existsByReservationTimeId(id)) {
            throw new ReservationTimeException(ErrorCode.RESERVATION_TIME_ALREADY_USED);
        }
        reservationTimeRepository.deleteById(id);
    }
}
