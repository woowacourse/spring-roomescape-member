package roomescape.reservationtime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.DuplicateException;
import roomescape.exception.ResourceInUseException;
import roomescape.reservation.ReservationRepository;

import java.time.LocalTime;

@Service
@Transactional(readOnly = true)
public class AdminReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public AdminReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                       ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }


    @Transactional
    public ReservationTime createReservationTime(LocalTime startAt) {
        try {
            return reservationTimeRepository.save(startAt);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateException("이미 존재하는 예약 시간입니다.");
        }
    }

    @Transactional
    public void deleteReservationTime(long id) {
        int reservationCount = reservationRepository.countByTimeId(id);

        if (reservationCount > 0) {
            throw new ResourceInUseException("해당 시간에 예약이 존재하여 삭제할 수 없습니다.");
        }

        reservationTimeRepository.delete(id);
    }
}
