package roomescape.time.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.ConflictException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.Time;
import roomescape.time.dto.ReservationTimeRequest;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.repository.TimeRepository;

@Service
public class TimeService {
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TimeService(TimeRepository timeRepository, ReservationRepository reservationRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse addReservationTime(ReservationTimeRequest reservationTimeRequest) {
        validateDuplicateTime(reservationTimeRequest.startAt());
        Time reservationTime = new Time(reservationTimeRequest.startAt());
        Time savedReservationTime = timeRepository.saveReservationTime(reservationTime);

        return toResponse(savedReservationTime);
    }

    public List<ReservationTimeResponse> findReservationTimes() {
        List<Time> reservationTimes = timeRepository.findAllReservationTimes();

        return reservationTimes.stream()
                .map(this::toResponse)
                .toList();
    }

    public void removeReservationTime(long reservationTimeId) {
        validateReservationExistence(reservationTimeId);
        timeRepository.deleteReservationTimeById(reservationTimeId);
    }

    public ReservationTimeResponse toResponse(Time time) {
        return new ReservationTimeResponse(time.getId(), time.getStartAt());
    }

    public void validateDuplicateTime(LocalTime startAt) {
        int duplicateTimeCount = timeRepository.findByStartAt(startAt);
        if (duplicateTimeCount > 0) {
            throw new ConflictException("이미 존재하는 예약 시간입니다.");
        }
    }

    public void validateReservationExistence(long timeId) {
        int reservationCount = reservationRepository.countReservationByTimeId(timeId);
        if (reservationCount > 0) {
            throw new ConflictException("삭제를 요청한 시간에 예약이 존재합니다.");
        }
    }
}
