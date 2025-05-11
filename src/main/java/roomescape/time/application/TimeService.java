package roomescape.time.application;

import static roomescape.time.exception.TimeErrorCode.TIME_DELETE_CONFLICT;

import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.time.application.dto.TimeDto;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.repository.TimeRepository;
import roomescape.reservation.domain.repository.dto.TimeDataWithBookingInfo;
import roomescape.global.exception.BusinessException;
import roomescape.global.exception.NotFoundException;
import roomescape.time.presentation.dto.request.TimeRequest;

@Service
public class TimeService {

    private final TimeRepository repository;

    public TimeService(TimeRepository repository) {
        this.repository = repository;
    }

    public List<TimeDto> getAllTimes() {
        List<ReservationTime> reservationTimes = repository.findAll();
        return TimeDto.from(reservationTimes);
    }

    public TimeDto registerNewTime(TimeRequest request) {
        ReservationTime newReservationTime = ReservationTime.createNew(request.startAt());
        Long id = repository.save(newReservationTime);

        ReservationTime saved = ReservationTime.assignId(id, newReservationTime);

        return TimeDto.from(saved);
    }

    public void deleteTime(Long id) {
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(TIME_DELETE_CONFLICT);
        }
    }

    public ReservationTime getTimeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("찾으려는 시간 id", id));
    }

    public List<TimeDataWithBookingInfo> getTimesWithBookingInfo(LocalDate date, Long themeId) {
        return repository.getTimesWithBookingInfo(date, themeId);
    }
}
