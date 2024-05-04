package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.time.dto.AvailabilityTimeRequest;
import roomescape.controller.time.dto.AvailabilityTimeResponse;
import roomescape.controller.time.dto.CreateTimeRequest;
import roomescape.controller.time.dto.ReadTimeResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.exception.TimeNotFoundException;
import roomescape.service.exception.TimeUsedException;

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

    public List<AvailabilityTimeResponse> getTimeAvailable(final AvailabilityTimeRequest request) {
        final List<ReservationTime> times = timeRepository.findAll();
        final Set<ReservationTime> bookedTimes = reservationRepository
                .findAllByDateAndThemeId(request.date(), request.themeId())
                .stream()
                .map(Reservation::getTime)
                .collect(Collectors.toSet());
        // TODO 현재 시간보다 낮은 것도 안보여야함
        return times.stream()
                .map(time -> AvailabilityTimeResponse.from(time, bookedTimes.contains(time)))
                .toList();
    }

    public AvailabilityTimeResponse addTime(final CreateTimeRequest createTimeRequest) {
        final ReservationTime parsedTime = createTimeRequest.toDomain();
        //TODO 중복 시간 저장 불가해야함
        final ReservationTime savedTime = timeRepository.save(parsedTime);
        return AvailabilityTimeResponse.from(savedTime, false);
    }

    public void deleteTime(final Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new TimeUsedException("예약된 시간은 삭제할 수 없습니다.");
        }
        final ReservationTime findTime = timeRepository.findById(id)
                .orElseThrow(() -> new TimeNotFoundException("존재하지 않는 시간입니다."));
        timeRepository.deleteById(findTime.getId());
    }
}
