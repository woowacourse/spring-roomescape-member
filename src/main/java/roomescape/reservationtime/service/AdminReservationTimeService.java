package roomescape.reservationtime.service;

import java.time.LocalTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.DuplicateException;
import roomescape.exception.EntityInUseException;
import roomescape.exception.ErrorCode;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@Service
public class AdminReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public AdminReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }


    @Transactional
    public ReservationTime createReservationTime(LocalTime startAt) {
        try {
            return reservationTimeRepository.save(startAt);
        } catch (DuplicateKeyException e) {
            throw new DuplicateException(ErrorCode.DUPLICATE_TIME, "이미 존재하는 예약 시간입니다.");
        }
    }

    @Transactional
    public void deleteReservationTime(long id) {
        try {
            reservationTimeRepository.delete(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(ErrorCode.TIME_IN_USE, "예약이 있어 삭제할 수 없습니다.");
        }
    }
}
