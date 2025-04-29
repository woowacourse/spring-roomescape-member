package roomescape.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.common.exception.DeleteReservationException;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public ReservationTimeService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTime saveReservationTime(ReservationTimeRequest request) {
        return reservationTimeRepository.saveReservationTime(request.toReservationTime());
    }

    public List<ReservationTime> readReservationTime() {
        return reservationTimeRepository.readReservationTimes();
    }

    public void deleteReservationTime(Long id) {
        boolean existsByTimeId = reservationRepository.existsByTimeId(id);
        if (existsByTimeId) {
            throw new DeleteReservationException("해당 시간을 사용하는 예약이 존재하기 때문에 삭제할 수 없습니다.");
        }

        reservationTimeRepository.deleteReservationTime(id);
    }
}
