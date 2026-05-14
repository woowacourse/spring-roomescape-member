package roomescape.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.exception.ErrorCode;
import roomescape.exception.reservation.ReservationAlreadyExistsException;
import roomescape.exception.reservation.ReservationInternalServerErrorException;
import roomescape.exception.reservation.ReservationNotFoundException;
import roomescape.exception.reservation.ReservationUpdateEmptyRequestException;
import roomescape.exception.reservation.ReservationUpdateFailedException;
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
        scheduleService.validateSchedule(body.date(), body.timeId(), body.themeId());
        long scheduleId = scheduleService.findScheduleIdByDateAndTimeIdAndThemeId(body.date(), body.timeId(), body.themeId());
        validateReservationAlreadyExistsNot(scheduleId);
        Reservation reservation = reservationRepository.save(body.toDomain(scheduleId));

        return ReservationSaveResponse.from(reservation);
    }

    public List<ReservationDetailFindResponse> findAllDetails() {
        return ReservationDetailFindResponse.from(reservationRepository.findAllDetails());
    }

    public void deleteByIdAndName(long reservationId, String name) {
        ReservationDetailProjection reservationDetail = reservationRepository.findDetailByIdAndName(reservationId, name)
                .orElse(null);
        if (reservationDetail == null) {
            return;
        }
        validateNotPast(reservationDetail);
        validateDeleteSuccessful(reservationId, name);
    }

    public List<ReservationDetailFindResponse> findDetailsByName(String name) {
        List<ReservationDetailProjection> reservationDetailProjection = reservationRepository.findDetailsByName(name);

        return ReservationDetailFindResponse.from(reservationDetailProjection);
    }

    public ReservationSaveResponse update(ReservationUpdateRequest body, long reservationId, String name) {
        ReservationDetailProjection oldReservation = getOldReservationDetailOrThrow(reservationId, name);
        validateNotPast(oldReservation);
        validateNotEmptyUpdateRequest(body);

        LocalDate newDate = Objects.requireNonNullElse(body.date(), oldReservation.date());
        long newTimeId = Objects.requireNonNullElse(body.timeId(), oldReservation.getTimeId());
        long scheduleId = scheduleService.findScheduleIdByDateAndTimeIdAndThemeId(newDate, newTimeId, oldReservation.getThemeId());
        validateDuplicatedReservationNot(reservationId, scheduleId);

        int affectedRow = reservationRepository.updateScheduleByIdAndName(oldReservation.id(), name, scheduleId);
        validateReservationUpdated(affectedRow);

        return ReservationSaveResponse.from(getNewReservationOrThrow(reservationId, name));
    }

    private static void validateReservationUpdated(int affectedRow) {
        if (affectedRow != 1) {
            throw new ReservationUpdateFailedException(ErrorCode.RESERVATION_UPDATE_FAILED);
        }
    }

    private static void validateNotEmptyUpdateRequest(ReservationUpdateRequest body) {
        if (body.date() == null && body.timeId() == null) {
            throw new ReservationUpdateEmptyRequestException(ErrorCode.RESERVATION_UPDATE_EMPTY);
        }
    }

    private Reservation getNewReservationOrThrow(long reservationId, String name) {
        return reservationRepository.findByIdAndName(reservationId, name)
                .orElseThrow(() -> new ReservationNotFoundException(ErrorCode.RESERVATION_NOT_FOUND_AFTER_UPDATE, name, reservationId));
    }

    private ReservationDetailProjection getOldReservationDetailOrThrow(long reservationId, String name) {
        return reservationRepository.findDetailByIdAndName(reservationId, name)
                .orElseThrow(() -> new ReservationNotFoundException(ErrorCode.RESERVATION_NOT_FOUND, name, reservationId));
    }

    private void validateDuplicatedReservationNot(long reservationId, long scheduleId) {
        if (reservationRepository.existsByScheduleIdAndIdNot(scheduleId, reservationId)) {
            throw new ReservationAlreadyExistsException(ErrorCode.RESERVATION_ALREADY_EXIST, scheduleId);
        }
    }

    private void validateDeleteSuccessful(long reservationId, String name) {
        if (reservationRepository.deleteByIdAndName(reservationId, name) <= 1) {
            return;
        }
        throw new ReservationInternalServerErrorException(ErrorCode.RESERVATION_DELETE_FAILED);
    }

    private void validateReservationAlreadyExistsNot(long scheduleId) {
        if (reservationRepository.existsByScheduleId(scheduleId)) {
            throw new ReservationAlreadyExistsException(ErrorCode.RESERVATION_ALREADY_EXIST, scheduleId);
        }
    }

    private void validateNotPast(ReservationDetailProjection reservationDetail) {
        scheduleService.validateNotPastDate(reservationDetail.date());
        scheduleService.validateNotPastTime(reservationDetail.date(), reservationDetail.getTime());
    }
}
