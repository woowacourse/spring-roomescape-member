package roomescape.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.dto.request.ReservationSaveRequest;
import roomescape.reservation.dto.request.ReservationUpdateRequest;
import roomescape.reservation.dto.response.ReservationDetailFindResponse;
import roomescape.reservation.dto.response.ReservationSaveResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.projection.ReservationDetailProjection;
import roomescape.schedule.ScheduleService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ScheduleService scheduleService;

    public ReservationSaveResponse save(ReservationSaveRequest body) {
        long scheduleId = scheduleService.findScheduleIdByDateAndTimeIdAndThemeId(body.date(), body.timeId(), body.themeId());
        if (reservationRepository.existsByScheduleId(scheduleId)) {
            throw new IllegalStateException("해당 스케줄을 사용중인 예약이 이미 존재합니다.");
        }
        scheduleService.validateSchedule(body.date(), body.timeId(), body.themeId());
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

    public ReservationSaveResponse update(ReservationUpdateRequest request, long reservationId, String name) {
        ReservationDetailProjection oldReservation = reservationRepository.findDetailByIdAndName(reservationId, name)
                .orElseThrow(() -> new IllegalStateException("해당 id와 name을 가진 예약이 존재하지 않습니다. id=" + reservationId + "name=" + name));

        if (request.date() == null && request.timeId() == null) {
            throw new IllegalStateException("수정될 내용이 존재하지 않습니다.");
        }

        LocalDate newDate = Objects.requireNonNullElse(request.date(), oldReservation.date());
        long newTimeId = Objects.requireNonNullElse(request.timeId(), oldReservation.getTimeId());
        long scheduleId = scheduleService.findScheduleIdByDateAndTimeIdAndThemeId(newDate, newTimeId, oldReservation.getThemeId());

        if (reservationRepository.existsByScheduleIdAndIdNot(scheduleId, reservationId)) {
            throw new IllegalStateException("중복된 예약이 있어 예약을 수정할 수 없습니다.");
        }

        int affectedRow = reservationRepository.updateScheduleByIdAndName(oldReservation.id(), name, scheduleId);
        if (affectedRow != 1) {
            throw new IllegalStateException("예약을 수정하는 데 알 수 없는 오류가 발생하였습니다.");
        }
        Reservation newReservation = reservationRepository.findByIdAndName(reservationId, name)
                .orElseThrow(() -> new IllegalStateException("예약 수정 후 데이터를 조회하지 못했습니다. reservationId=" + reservationId));

        return ReservationSaveResponse.from(newReservation);
    }
}
