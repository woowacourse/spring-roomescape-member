package roomescape.reservationtime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.ReservationRepository;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTime> findReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    @Transactional
    public ReservationTime createReservationTime(LocalTime startAt) {
        try {
            return reservationTimeRepository.save(startAt);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateReservationTimeException("이미 존재하는 예약 시간입니다");
        }
    }

    @Transactional
    public void deleteReservationTime(long id) {
        int reservationCount = reservationRepository.countByTimeId(id);

        if (reservationCount > 0) {
            throw new ReservationTimeNotEmptyException("예약이 있어 삭제할 수 없습니다");
        }

        reservationTimeRepository.delete(id);
    }

    public ScheduleResponse getSchedules(LocalDate date, Long themeId) {
        List<AvailableTimeDto> schedules = reservationTimeRepository.findAvailableTimes(date, themeId);
        return new ScheduleResponse(themeId, date, schedules);
    }
}
