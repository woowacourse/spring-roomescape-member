package roomescape.reservation.application;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.infra.ReservationTimeRepository;
import roomescape.reservation.presentation.dto.request.ReservationTimeSaveRequest;
import roomescape.reservation.presentation.dto.response.AvailableTimeFindResponse;
import roomescape.reservation.presentation.dto.response.ReservationTimeFindResponse;
import roomescape.reservation.presentation.dto.response.ReservationTimeSaveResponse;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

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

    public void delete(Long id) {
        reservationTimeRepository.deleteById(id);
    }

    public List<AvailableTimeFindResponse> findTimesByDateAndThemeId(LocalDate date, long themeId){
        // schedule에서 존재하는 타임 id 모두 조회
        List<ReservationTime> times = scheduleRepository.findByDateAndThemeId();

        // date와 themeId에 해당하는 reservation를 모두 조회
        List<ReservationTime> notAvailableTimes = reservationRepository.findTimesByDateAndThemeId(date, themeId);
    }
}
