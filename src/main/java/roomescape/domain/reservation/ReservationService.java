package roomescape.domain.reservation;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.admin.dto.ReservationResponse;
import roomescape.domain.reservation.dto.CreateReservationRequest;
import roomescape.domain.reservation.dto.CreateReservationResponse;
import roomescape.domain.reservation.dto.UpdateReservationRequest;
import roomescape.domain.reservation.dto.UserReservationResponse;
import roomescape.domain.reservationdate.ReservationDate;
import roomescape.domain.reservationdate.ReservationDateRepository;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.support.exception.BadRequestException;
import roomescape.support.exception.NotFoundException;
import roomescape.support.exception.ReservationDateErrorCode;
import roomescape.support.exception.ReservationErrorCode;
import roomescape.support.exception.ReservationTimeErrorCode;
import roomescape.support.exception.ThemeErrorCode;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationDateRepository reservationDateRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    @Transactional
    public CreateReservationResponse createReservation(CreateReservationRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
            .orElseThrow(() -> new NotFoundException(ReservationTimeErrorCode.RESERVATION_TIME_NOT_EXIST));
        ReservationDate reservationDate = reservationDateRepository.findById(request.dateId())
            .orElseThrow(() -> new NotFoundException(ReservationDateErrorCode.RESERVATION_DATE_NOT_EXIST));
        validateReservationScheduleToCreate(reservationDate, reservationTime);
        Theme theme = themeRepository.findById(request.themeId())
            .orElseThrow(() -> new NotFoundException(ThemeErrorCode.THEME_NOT_EXIST));
        validateDuplicated(reservationTime, reservationDate, theme);
        Reservation savedReservation = reservationRepository.save(
            request.toEntity(reservationDate, reservationTime, theme));
        return CreateReservationResponse.from(savedReservation);
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
            .map(ReservationResponse::from)
            .toList();
    }

    public UserReservationResponse getUserReservations(String name) {
        List<Reservation> reservations = reservationRepository.findByName(name);
        return UserReservationResponse.of(name, reservations);
    }

    @Transactional
    public void deleteReservationByAdmin(Long id) {
        int deletedCount = reservationRepository.deleteById(id);
        if (deletedCount == 0) {
            log.warn("이미 삭제된 예약 삭제 요청이 들어왔습니다. reservationId={}", id);
        }
    }

    @Transactional
    public void deleteUserReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ReservationErrorCode.RESERVATION_NOT_FOUND));
        validateUserCanDeleteReservation(reservation);
        reservationRepository.deleteById(id);
    }

    @Transactional
    public void updateReservation(Long id, UpdateReservationRequest request) {
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ReservationErrorCode.RESERVATION_NOT_FOUND));
        ReservationTime reservationTime = reservation.getTime();
        ReservationDate reservationDate = reservation.getDate();
        reservationTime = getReservationTime(request, reservationTime);
        reservationDate = getReservationDate(request, reservationDate);
        validateReservationScheduleToCreate(reservationDate, reservationTime);
        validateDuplicatedWithOther(id, reservationTime, reservationDate, reservation.getTheme());
        Reservation updatedReservation = Reservation.createWithoutId(
            reservation.getName(),
            reservationDate,
            reservationTime,
            reservation.getTheme()
        );
        reservationRepository.update(id, updatedReservation)
            .orElseThrow(() -> new NotFoundException(ReservationErrorCode.RESERVATION_NOT_FOUND));
    }

    private void validateReservationScheduleToCreate(
        ReservationDate reservationDate,
        ReservationTime reservationTime
    ) {
        LocalDate today = LocalDate.now(clock);
        LocalTime now = LocalTime.now(clock);
        if (isPastDate(reservationDate, today)) {
            throw new BadRequestException(ReservationDateErrorCode.RESERVATION_DATE_MUST_BE_TODAY_OR_LATER, today);
        }
        if (isPastTimeToday(reservationDate, reservationTime, today, now)) {
            throw new BadRequestException(ReservationTimeErrorCode.RESERVATION_TIME_SHOULD_BE_NOW_OR_LATER, now);
        }
    }

    private void validateDuplicated(ReservationTime reservationTime, ReservationDate reservationDate, Theme theme) {
        if (isExistReservation(reservationTime, reservationDate, theme)) {
            throw new BadRequestException(ReservationErrorCode.DUPLICATED_RESERVATION);
        }
    }

    private boolean isExistReservation(ReservationTime reservationTime, ReservationDate reservationDate, Theme theme) {
        return reservationRepository.existsReservation(reservationTime.getId(), reservationDate.getId(), theme.getId());
    }

    private void validateDuplicatedWithOther(
        Long id,
        ReservationTime reservationTime,
        ReservationDate reservationDate,
        Theme theme
    ) {
        if (reservationRepository.existsOtherReservation(
            id,
            reservationTime.getId(),
            reservationDate.getId(),
            theme.getId()
        )) {
            throw new BadRequestException(ReservationErrorCode.DUPLICATED_RESERVATION);
        }
    }

    private void validateUserCanDeleteReservation(Reservation reservation) {
        LocalDate today = LocalDate.now(clock);
        LocalTime now = LocalTime.now(clock);
        if (isPastDate(reservation.getDate(), today)) {
            throw new BadRequestException(ReservationDateErrorCode.PAST_RESERVATION_DATE_CANNOT_BE_DELETED, today);
        }
        if (isPastTimeToday(reservation.getDate(), reservation.getTime(), today, now)) {
            throw new BadRequestException(ReservationTimeErrorCode.PAST_RESERVATION_TiME_CANNOT_BE_DELETED, now);
        }
    }

    private boolean isPastDate(ReservationDate reservationDate, LocalDate today) {
        return reservationDate.isBefore(today);
    }

    private boolean isPastTimeToday(
        ReservationDate reservationDate,
        ReservationTime reservationTime,
        LocalDate today,
        LocalTime now
    ) {
        return reservationDate.isSame(today) && reservationTime.isBefore(now);
    }

    private ReservationDate getReservationDate(UpdateReservationRequest request, ReservationDate reservationDate) {
        if (request.startWhen() != null) {
            reservationDate = reservationDateRepository.findByDate(request.startWhen())
                .orElseThrow(() -> new NotFoundException(ReservationDateErrorCode.RESERVATION_DATE_NOT_EXIST));
        }
        return reservationDate;
    }

    private ReservationTime getReservationTime(UpdateReservationRequest request, ReservationTime reservationTime) {
        if (request.startAt() != null) {
            reservationTime = reservationTimeRepository.findByStartAt(request.startAt())
                .orElseThrow(() -> new NotFoundException(ReservationTimeErrorCode.RESERVATION_TIME_NOT_EXIST));
        }
        return reservationTime;
    }
}
