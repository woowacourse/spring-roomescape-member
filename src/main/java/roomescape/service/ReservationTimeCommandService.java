package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.repository.ReservationTimeDao;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ReservationTimeCommandService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeResponse create(LocalTime startAt) {
        ReservationTime savedReservationTime = reservationTimeDao.save(ReservationTime.pending(startAt));
        return ReservationTimeResponse.from(savedReservationTime);
    }

    public void delete(long reservationTimeId) {
        try {
            reservationTimeDao.deleteByTimeId(reservationTimeId);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("예약이 존재하는 시간은 삭제할 수 없습니다.");
        }
    }
}
