package roomescape.user.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.user.theme.Theme;
import roomescape.admin.theme.AdminThemeRepository;
import roomescape.user.reservationtime.ReservationTime;
import roomescape.user.reservation.dto.ReservationRequest;
import roomescape.user.reservation.dto.ReservationResponse;
import roomescape.user.reservationtime.dto.TimeResponse;
import roomescape.user.reservationtime.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final AdminThemeRepository adminThemeRepository;

    public ReservationService(
        ReservationRepository reservationRepository,
        ReservationTimeRepository reservationTimeRepository, AdminThemeRepository adminThemeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.adminThemeRepository = adminThemeRepository;
    }

    public ReservationResponse createReservation(ReservationRequest request) {
        validateInvalidReservationTime(request);
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
            .orElseThrow(() -> new RoomescapeException(ErrorCode.TIME_ID_NOT_FOUND));
        Theme theme = adminThemeRepository.findById(request.themeId())
            .orElseThrow(() -> new RoomescapeException(ErrorCode.THEME_ID_NOT_FOUND));
        validateDuplicateReservation(request);

        Reservation reservation = Reservation.of(
            request.name(),
            request.date(),
            time,
            theme
        );

        Reservation saved = reservationRepository.save(reservation);
        return ReservationResponse.from(saved);
    }

    private void validateInvalidReservationTime(ReservationRequest request) {
        LocalDate reservationDate = request.date();
        LocalTime reservationTime = reservationTimeRepository.findById(request.timeId())
            .orElseThrow(() -> new RoomescapeException(ErrorCode.TIME_ID_NOT_FOUND))
            .getStartAt();

        if (reservationDate.isBefore(LocalDate.now())
            || (reservationDate.isEqual(LocalDate.now()) && reservationTime.isAfter(LocalTime.now()))) {
            throw new RoomescapeException(ErrorCode.RESERVATION_TIME_PASSED);
        }

    }

    private void validateDuplicateReservation(ReservationRequest request) {
        boolean isDuplicated = reservationRepository.existsByDateAndTimeIdAndThemeId(
            request.date(),
            request.timeId(),
            request.themeId());
        if (isDuplicated) {
            throw new RoomescapeException(ErrorCode.DUPLICATE_RESERVATION);
        }
    }

    public List<TimeResponse> getReservations(LocalDate date, Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        List<Long> bookedTimeIds = reservationRepository.findTimeByDateAndThemeId(date, themeId);

        return reservationTimes.stream()
            .filter(reservationTime -> !bookedTimeIds.contains(reservationTime.getId()))
            .map(TimeResponse::of)
            .toList();
    }

    public void deleteReservation(Long id) {
        validateReservationId(id);
        reservationRepository.deleteById(id);
    }

    private void validateReservationId(Long id) {
        boolean isValidId = reservationRepository.existsById(id);
        if (!isValidId) {
            throw new RoomescapeException(ErrorCode.RESERVATION_ID_NOT_FOUND);
        }
    }
}
