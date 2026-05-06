package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeDao;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ReservationTimeCommandService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTime create(LocalTime startAt) {
        return reservationTimeDao.save(ReservationTime.pending(startAt));

    }

    public void delete(long reservationTimeId) {
        try {
            reservationTimeDao.deleteByTimeId(reservationTimeId);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("예약이 존재하는 시간은 삭제할 수 없습니다.");
        }
    }
}
