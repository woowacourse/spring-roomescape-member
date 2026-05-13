package roomescape.reservationtime.service;

import java.time.LocalTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.DuplicateException;
import roomescape.exception.ResourceInUseException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@Service
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
            throw new ResourceInUseException("예약이 있어 삭제할 수 없습니다.");
        }

        reservationTimeRepository.delete(id);
    }
}
