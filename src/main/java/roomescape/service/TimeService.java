package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.time.Time;
import roomescape.dto.time.TimeRequest;
import roomescape.dto.time.TimeResponse;
import roomescape.global.exception.ApplicationException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.TimeRepository;

@Service
public class TimeService {

    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TimeService(TimeRepository timeRepository, ReservationRepository reservationRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<TimeResponse> findAllTimes() {
        return timeRepository.findAll()
                .stream()
                .map(TimeResponse::from)
                .toList();
    }

    public TimeResponse createTime(TimeRequest timeRequest) {
        List<Time> duplicateTimes = timeRepository.findByStartAt(timeRequest.startAt());
        if (duplicateTimes.size() > 0) {
            throw new ApplicationException("이미 존재하는 예약 시간입니다.");
        }

        Time time = timeRequest.toTime();
        Time savedTime = timeRepository.save(time);

        return TimeResponse.from(savedTime);
    }

    public void deleteTime(Long id) {
        List<Reservation> usingTimeReservations = reservationRepository.findByTimeId(id);
        if (usingTimeReservations.size() > 0) {
            throw new ApplicationException(String.format("[TimeId - %d] 해당 시간에 예약이 존재하여 시간을 삭제할 수 없습니다.", id));
        }
        timeRepository.delete(id);
    }
}
