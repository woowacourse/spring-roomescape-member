package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.time.dto.AvailabilityTimeResponse;
import roomescape.controller.time.dto.ReadTimeResponse;
import roomescape.controller.time.dto.TimeRequest;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.exception.TimeUsedException;

import java.time.LocalDate;
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

    public List<ReadTimeResponse> getTimes() {
        return timeRepository.findAll().stream()
                .map(ReadTimeResponse::from)
                .toList();
    }

    public List<AvailabilityTimeResponse> getTimeAvailable(final String date, final String themeId) {
        final List<ReservationTime> times = timeRepository.findAll()
                .stream()
                .toList();
        final Set<ReservationTime> bookedTimes = reservationRepository
                .findAllByDateAndThemeId(LocalDate.parse(date), Long.valueOf(themeId))
                .stream()
                .map(Reservation::getTime)
                .collect(Collectors.toSet());

        return times.stream()
                .map(time -> AvailabilityTimeResponse.from(time, bookedTimes.contains(time)))
                .toList();
    }

    public AvailabilityTimeResponse addTime(final TimeRequest timeRequest) {
        final ReservationTime parsedTime = timeRequest.toDomain();
        final ReservationTime savedTime = timeRepository.save(parsedTime);
        return AvailabilityTimeResponse.from(savedTime, false);
    }

    public int deleteTime(final Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new TimeUsedException("예약된 시간은 삭제할 수 없습니다.");
        }
        return timeRepository.deleteById(id);
    }
}
