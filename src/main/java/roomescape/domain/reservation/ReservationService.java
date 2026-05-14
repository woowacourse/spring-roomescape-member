package roomescape.domain.reservation;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.dto.ReservationCreationRequest;
import roomescape.domain.reservation.dto.ReservationCreationResponse;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dto.ReservationUpdateRequest;
import roomescape.domain.reservationdate.ReservationDate;
import roomescape.domain.reservationdate.ReservationDateService;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeService;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeService;
import roomescape.support.exception.ReservationErrorCode;
import roomescape.support.exception.ReservationTimeErrorCode;
import roomescape.support.exception.RoomescapeException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationDateService reservationDateService;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationCreationResponse createReservation(ReservationCreationRequest request) {
        ReservationDate reservationDate = reservationDateService.findById(request.dateId());
        ReservationTime reservationTime = reservationTimeService.findById(request.timeId());
        validateAvailableDateTime(reservationDate, reservationTime);
        Theme theme = themeService.findById(request.themeId());
        if (reservationRepository.existsByDateIdAndTimeIdAndThemeId(
            request.dateId(), request.timeId(), request.themeId())) {
            throw new RoomescapeException(ReservationErrorCode.RESERVATION_DUPLICATED);
        }
        Reservation savedReservation = reservationRepository.save(
            request.toEntity(reservationDate, reservationTime, theme));
        return ReservationCreationResponse.from(savedReservation);
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
            .map(ReservationResponse::from)
            .toList();
    }

    public void deleteReservation(Long id) {
        int deletedCount = reservationRepository.deleteById(id);
        if (deletedCount == 0) {
            log.warn("이미 삭제된 예약 삭제 요청이 들어왔습니다. reservationId={}", id);
        }
    }

    public List<ReservationResponse> getReservationsByName(String name) {
        return reservationRepository.findByName(name).stream()
            .map(ReservationResponse::from)
            .toList();
    }

    public void cancelReservation(Long id) {
        validateModifiable(id);
        reservationRepository.deleteById(id);
    }

    public ReservationResponse updateReservation(Long id, @Valid ReservationUpdateRequest request) {
        validateModifiable(id);
        int updatedCount = reservationRepository.updateReservation(id, request.dateId(), request.timeId());
        if (updatedCount == 0) {
            log.warn(" 수정할 예약 건이 없습니다. reservationId={}", id);
        }
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new RoomescapeException(ReservationErrorCode.RESERVATION_NOT_FOUND));
        return ReservationResponse.from(reservation);
    }

    private void validateModifiable(Long id) {
        LocalDate today = LocalDate.now();
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new RoomescapeException(ReservationErrorCode.RESERVATION_NOT_FOUND));
        LocalDate playDay = reservation.getDate().getPlayDay();
        if (playDay.isBefore(today) || playDay.isEqual(today)) {
            throw new RoomescapeException(ReservationErrorCode.RESERVATION_CANNOT_CANCEL);
        }
    }

    private void validateAvailableDateTime(ReservationDate reservationDate, ReservationTime reservationTime) {
        LocalDateTime dateTime = LocalDateTime.of(reservationDate.getPlayDay(), reservationTime.getStartAt());
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new RoomescapeException(ReservationTimeErrorCode.PAST_TIME_NOT_ALLOWED);
        }
    }
}
