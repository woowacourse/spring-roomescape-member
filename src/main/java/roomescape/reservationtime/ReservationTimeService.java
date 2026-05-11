package roomescape.reservationtime;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservationtime.dto.request.ReservationTimeSaveRequest;
import roomescape.reservationtime.dto.response.AvailableTimeFindResponse;
import roomescape.reservationtime.dto.response.ReservationTimeFindResponse;
import roomescape.reservationtime.dto.response.ReservationTimeSaveResponse;
import roomescape.reservationtime.dto.response.TimeInformation;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.schedule.ScheduleService;
import roomescape.reservation.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ScheduleService scheduleService;
    private final ReservationRepository reservationRepository;

    public ReservationTimeSaveResponse save(ReservationTimeSaveRequest body) {
        return ReservationTimeSaveResponse.from(reservationTimeRepository.save(body.toDomain()));
    }

    public List<ReservationTimeFindResponse> findAll() {
        return ReservationTimeFindResponse.from(reservationTimeRepository.findAll());
    }

    public void delete(long id) {
        scheduleService.validateTimeDeletable(id);
        reservationTimeRepository.deleteById(id);
    }

    public List<AvailableTimeFindResponse> findTimesByDateAndThemeId(LocalDate date, long themeId) {
        // schedule에서 존재하는 시간 id 모두 조회
        List<ReservationTime> totalTimes = reservationTimeRepository.findTimesByDateAndThemeId(date, themeId);

        // date와 themeId에 해당하는 reservation의 시간 id들을 모두 조회
        Set<Long> notAvailableTimeIds = reservationRepository.findTimeIdByDateAndThemeId(date, themeId);

        return totalTimes.stream()
                .map(time -> new AvailableTimeFindResponse(
                        new TimeInformation(time.getId(), time.getStartAt()),
                        !notAvailableTimeIds.contains(time.getId())
                ))
                .toList();
    }
}
