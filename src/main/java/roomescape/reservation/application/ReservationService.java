package roomescape.reservation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.infra.ReservationRepository;
import roomescape.reservation.presentation.dto.request.ReservationSaveRequest;
import roomescape.reservation.presentation.dto.response.ReservationDetailFindResponse;
import roomescape.reservation.presentation.dto.response.ReservationSaveResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ScheduleService scheduleService;

    public ReservationSaveResponse save(ReservationSaveRequest body) {
        long scheduleId = scheduleService.findScheduleIdByDateAndTimeIdAndThemeId(body.date(), body.timeId(), body.themeId());
        Reservation reservation = reservationRepository.save(body.toDomain(scheduleId));

        return ReservationSaveResponse.from(reservation);
    }

    public List<ReservationDetailFindResponse> findAllDetails() {
        return ReservationDetailFindResponse.from(reservationRepository.findAllDetails());
    }

    public void delete(long id) {
        reservationRepository.deleteById(id);
    }
}
