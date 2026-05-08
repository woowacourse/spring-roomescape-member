package roomescape.reservation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.infra.ReservationRepository;
import roomescape.reservation.infra.ReservationTimeRepository;
import roomescape.reservation.infra.ScheduleRepository;
import roomescape.reservation.presentation.dto.request.ReservationTimeSaveRequest;
import roomescape.reservation.presentation.dto.response.AvailableTimeFindResponse;
import roomescape.reservation.presentation.dto.response.ReservationTimeFindResponse;
import roomescape.reservation.presentation.dto.response.ReservationTimeSaveResponse;
import roomescape.reservation.presentation.dto.response.dto.TimeInformation;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ScheduleRepository scheduleRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeSaveResponse save(ReservationTimeSaveRequest body) {
        return ReservationTimeSaveResponse.from(reservationTimeRepository.save(body.toDomain()));
    }

    public List<ReservationTimeFindResponse> findAll() {
        return ReservationTimeFindResponse.from(reservationTimeRepository.findAll());
    }

    public void delete(long id) {
        if (scheduleRepository.existsByTimeId(id)) {
            throw new IllegalStateException("timeId= " + id + " 인 시간을 사용하는 스케줄이 있어 삭제할 수 없습니다.");
        }
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
