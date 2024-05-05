package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.time.TimeRequest;
import roomescape.controller.time.TimeResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.exception.InvalidDateException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.exception.TimeNotFoundException;
import roomescape.service.exception.TimeUsedException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;

    public TimeService(final ReservationRepository reservationRepository, final ReservationTimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
    }

    private static void validateNotFound(int deletedCount) {
        if (deletedCount == 0) {
            throw new TimeNotFoundException("예약 시간이 존재하지 않습니다.");
        }
    }

    public List<TimeResponse> getTimes() {
        return timeRepository.findAllByOrderByStartAt().stream()
                .map(time -> TimeResponse.from(time, false))
                .toList();
    }

    public List<TimeResponse> getTimesWithBooked(final String date, final Long themeId) {
        validateDateFormat(date);

        final List<ReservationTime> times = findAllTimes();
        final Set<ReservationTime> bookedTimes = findAllBookedTimes(date, themeId);

        return times.stream()
                .map(time -> TimeResponse.from(time, bookedTimes.contains(time)))
                .toList();
    }

    private void validateDateFormat(String date) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new InvalidDateException("날짜 형식이 올바르지 않습니다.");
        }
    }

    private List<ReservationTime> findAllTimes() {
        return timeRepository.findAllByOrderByStartAt()
                .stream()
                .toList();
    }

    private Set<ReservationTime> findAllBookedTimes(String date, Long themeId) {
        return reservationRepository
                .findAllByDateAndThemeId(LocalDate.parse(date), themeId)
                .stream()
                .map(Reservation::getTime)
                .collect(Collectors.toSet());
    }

    // TODO: validate duplicate
    public TimeResponse addTime(final TimeRequest timeRequest) {
        final ReservationTime parsedTime = timeRequest.toDomain();
        final ReservationTime savedTime = timeRepository.save(parsedTime);

        return TimeResponse.from(savedTime, false);
    }

    public int deleteTime(final Long id) {
        validateUsed(id);
        
        final int deletedCount = timeRepository.delete(id);
        validateNotFound(deletedCount);

        return deletedCount;
    }

    private void validateUsed(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new TimeUsedException("예약된 시간은 삭제할 수 없습니다.");
        }
    }
}
