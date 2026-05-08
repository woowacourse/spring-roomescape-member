package roomescape.reservation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.Schedule;
import roomescape.reservation.infra.ReservationRepository;
import roomescape.reservation.infra.ScheduleRepository;
import roomescape.reservation.presentation.dto.request.ReservationSaveRequest;
import roomescape.reservation.presentation.dto.response.ReservationFindResponse;
import roomescape.reservation.presentation.dto.response.ReservationSaveResponse;
import roomescape.reservation.presentation.dto.response.dto.TimeInformation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ScheduleRepository scheduleRepository;

    public ReservationSaveResponse save(ReservationSaveRequest body) {
        Schedule schedule = scheduleRepository.findByDateAndTimeIdAndThemeId(body.date(), body.timeId(), body.themeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 조건을 가진 일정이 없습니다. date: " + body.date() + "timeId: " + body.timeId() + "themeId: " + body.themeId()));

        Reservation reservation = reservationRepository.save(body.toDomain(schedule));
        return ReservationSaveResponse.from(reservation);
    }

    public List<ReservationFindResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return ReservationFindResponse.from(reservations);
    }

    public void delete(long id) {
        reservationRepository.deleteById(id);
    }
}
