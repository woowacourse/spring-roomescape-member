package roomescape.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.ReservationTimeCreationRequest;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.time.ReservationTime;
import roomescape.domain.time.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private static final int INVALID_DELETED_COUNT = 0;

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTime register(ReservationTimeCreationRequest request) {
        ReservationTime reservationTime = request.toReservationTime();
        return reservationTimeRepository.save(reservationTime);
    }

    public List<ReservationTime> findReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    public void delete(long id) {
        validateReservedTime(id);
        int deletedCount = reservationTimeRepository.deleteById(id);
        if (deletedCount == INVALID_DELETED_COUNT) {
            throw new IllegalArgumentException("존재하지 않는 예약 시간입니다.");
        }
    }

    private void validateReservedTime(long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new IllegalArgumentException("해당 시간을 사용하는 예약이 존재합니다");
        }
    }
}
