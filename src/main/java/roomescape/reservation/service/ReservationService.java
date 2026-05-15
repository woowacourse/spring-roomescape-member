package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.policy.PastReservationNotAllowedException;
import roomescape.global.exception.policy.ReservationConflictException;
import roomescape.global.exception.policy.ReservationUpdateNotAllowedException;
import roomescape.global.exception.validation.ThemeNotFoundException;
import roomescape.reservation.controller.dto.CreateReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.dto.CreateReservationParams;
import roomescape.reservation.repository.dto.DuplicateReservationCondition;
import roomescape.reservation.repository.dto.UpdateReservationParams;
import roomescape.reservation.service.dto.RescheduleReservationInfo;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public List<ReservationResponse> findAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> findAllReservationsByName(String name) {
        return reservationRepository.findByName(name).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse reserve(CreateReservationRequest request) {
        validateReservationAvailable(request.date(), request.timeId(), request.themeId());
        CreateReservationParams params = new CreateReservationParams(request.name(), request.date(),
                request.timeId(), request.themeId());
        Reservation reservation = reservationRepository.save(params);

        return ReservationResponse.from(reservation);
    }

    @Transactional
    public ReservationResponse rescheduleReservation(RescheduleReservationInfo rescheduleReservationInfo) {
        Reservation reservation = reservationRepository.findById(rescheduleReservationInfo.id());
        validateReservationAvailable(rescheduleReservationInfo.date(), rescheduleReservationInfo.timeId(), reservation.getThemeId());

        ReservationTime reservedTime = reservationTimeRepository.findById(rescheduleReservationInfo.timeId());
        Reservation rescheduledReservation = reservation.reschedule(rescheduleReservationInfo.date(), reservedTime);
        reservationRepository.update(UpdateReservationParams.from(rescheduledReservation));

        return ReservationResponse.from(rescheduledReservation);
    }

    private void validateReservationAvailable(LocalDate date, Long timeId, Long themeId) {
        validateThemeExists(themeId);
        LocalDateTime reservationDateTime = LocalDateTime.of(date,
                reservationTimeRepository.findById(timeId).getStartAt());

        if (isBeforeNow(reservationDateTime)) {
            throw new PastReservationNotAllowedException();
        }
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(
                new DuplicateReservationCondition(date, timeId, themeId))) {
            throw new ReservationConflictException();
        }
    }

    private void validateThemeExists(Long id) {
        if (themeRepository.existsById(id)) {
            return;
        }
        throw new ThemeNotFoundException();
    }

    @Transactional
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id);
        validateFutureOrPresent(reservation);

        reservationRepository.deleteById(id);
    }

    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id);
        validateAlreadyCancelled(reservation);
        validateFutureOrPresent(reservation);

        reservationRepository.cancelById(id);
    }

    private void validateFutureOrPresent(Reservation reservation) {
        if (isBeforeNow(reservation.getReservationDateTime())) {
            throw new ReservationUpdateNotAllowedException();
        }
    }

    private static void validateAlreadyCancelled(Reservation reservation) {
        if (reservation.isCancel()) {
            throw new ReservationUpdateNotAllowedException(ErrorCode.RESERVATION_ALREADY_CANCELLED);
        }
    }

    private boolean isBeforeNow(LocalDateTime reservationDateTime) {
        return reservationDateTime.isBefore(LocalDateTime.now());
    }
}
