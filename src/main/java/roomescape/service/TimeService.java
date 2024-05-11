package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.time.Time;
import roomescape.dto.time.TimeRequest;
import roomescape.dto.time.TimeResponse;
import roomescape.global.exception.ApplicationException;
import roomescape.global.exception.ExceptionType;
import roomescape.repository.ReservationRepository;
import roomescape.repository.TimeRepository;

import java.util.List;

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
            throw new ApplicationException(ExceptionType.TIME_ALREADY_EXIST);
        }

        Time time = timeRequest.toTime();
        Time savedTime = timeRepository.save(time);

        return TimeResponse.from(savedTime);
    }

    public void deleteTime(Long id) {
        List<Reservation> usingTimeReservations = reservationRepository.findByTimeId(id);
        if (usingTimeReservations.size() > 0) {
            throw new ApplicationException(ExceptionType.RESERVATION_EXIST_ON_TIME);
        }
        timeRepository.delete(id);
    }
}
