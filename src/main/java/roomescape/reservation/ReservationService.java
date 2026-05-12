package roomescape.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.dto.request.ReservationSaveRequest;
import roomescape.reservation.dto.response.ReservationDetailFindResponse;
import roomescape.reservation.dto.response.ReservationSaveResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.projection.ReservationDetailProjection;
import roomescape.schedule.ScheduleService;

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

    public void deleteById(long id) {
        if (reservationRepository.deleteById(id) <= 1) {
            return;
        }
        throw new IllegalStateException("예약 삭제에 실패했습니다. reservationId=" + id);
    }

    public void deleteByIdAndName(long id, String name) {
        if (reservationRepository.deleteByIdAndName(id, name) <= 1) {
            return;
        }
        throw new IllegalStateException("예약 삭제에 실패했습니다. reservationId=" + id + ", name=" + name);
    }

    public List<ReservationDetailFindResponse> findDetailByName(String name) {
        List<ReservationDetailProjection> reservationDetailProjection = reservationRepository.findDetailsByName(name);

        return ReservationDetailFindResponse.from(reservationDetailProjection);
    }
}
