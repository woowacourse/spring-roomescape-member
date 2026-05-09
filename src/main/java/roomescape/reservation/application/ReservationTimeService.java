package roomescape.reservation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.infra.ReservationRepository;
import roomescape.reservation.infra.ReservationTimeRepository;
import roomescape.reservation.presentation.dto.request.ReservationTimeSaveRequest;
import roomescape.reservation.presentation.dto.response.AvailableTimeFindResponse;
import roomescape.reservation.presentation.dto.response.ReservationTimeFindResponse;
import roomescape.reservation.presentation.dto.response.ReservationTimeSaveResponse;
import roomescape.reservation.presentation.dto.response.dto.TimeInformation;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationTimeSaveResponse save(ReservationTimeSaveRequest body) {
        ReservationTime reservationTime = reservationTimeRepository.save(body.startAt());

        return new ReservationTimeSaveResponse(reservationTime.getId(), reservationTime.getStartAt());
    }

    public List<ReservationTimeFindResponse> findAll() {
        return reservationTimeRepository.findAll().stream()
                .map(reservationTime -> new ReservationTimeFindResponse(
                        reservationTime.getId(),
                        reservationTime.getStartAt()
                ))
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        reservationTimeRepository.deleteById(id);
    }

    public List<AvailableTimeFindResponse> findTimesByDateAndThemeId(LocalDate date, long themeId) {
        // schedule에서 존재하는 타임 id 모두 조회
        List<ReservationTime> totalTimes = reservationTimeRepository.findTimesByDateAndThemeId(date, themeId);

        // date와 themeId에 해당하는 reservation를 모두 조회
        List<Long> notAvailableTimeIds = reservationRepository.findTimeIdByDateAndThemeId(date, themeId);

        return totalTimes.stream()
                .map(time -> new AvailableTimeFindResponse(
                        new TimeInformation(time.getId(), time.getStartAt()),
                        !notAvailableTimeIds.contains(time.getId())
                ))
                .toList();
    }
}
