package roomescape.domain.reservation.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.BusinessException;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.exception.ReservationErrorCode;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.request.ReservationCreateRequest;
import roomescape.domain.reservation.request.ReservationUpdateRequest;
import roomescape.domain.reservation.response.ReservationResponse;
import roomescape.domain.reservation.response.ReservationsResponse;
import roomescape.domain.reservationtime.entity.ReservationTime;
import roomescape.domain.reservationtime.exception.TimeErrorCode;
import roomescape.domain.reservationtime.repository.ReservationTimeRepository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.exception.ThemeErrorCode;
import roomescape.domain.theme.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    @Autowired
    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository,
            Clock clock
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    public ReservationsResponse findAllReservations() {
        List<ReservationResponse> reservations = reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();

        return new ReservationsResponse(reservations);
    }

    public ReservationsResponse findMyReservations(String username) {
        List<ReservationResponse> reservations = reservationRepository.findAllByUsername(username).stream()
                .map(ReservationResponse::from)
                .toList();

        return new ReservationsResponse(reservations);
    }

    @Transactional
    public ReservationResponse saveReservationByUser(ReservationCreateRequest request) {
        validateReservationDateIsNotInPast(request.date());

        ReservationTime time = findTimeByIdOrThrow(request.timeId());
        validateReservationTimeIsNotInPastWhenToday(request.date(), time.getStartAt());

        Theme theme = findThemeByIdOrThrow(request.themeId());

        validateDuplicateReservation(request.themeId(), request.date(), request.timeId());

        Reservation reservation = Reservation.create(
                request.username(),
                theme,
                request.date(),
                time
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    @Transactional
    public ReservationResponse saveReservationByAdmin(ReservationCreateRequest request) {
        ReservationTime time = findTimeByIdOrThrow(request.timeId());
        Theme theme = findThemeByIdOrThrow(request.themeId());

        validateDuplicateReservation(request.themeId(), request.date(), request.timeId());

        Reservation reservation = Reservation.create(
                request.username(),
                theme,
                request.date(),
                time
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    @Transactional
    public ReservationResponse updateReservationByUser(Long id, ReservationUpdateRequest request) {
        validateReservationDateIsNotInPast(request.date());

        ReservationTime newTime = findTimeByIdOrThrow(request.timeId());
        validateReservationTimeIsNotInPastWhenToday(request.date(), newTime.getStartAt());

        Reservation reservation = findReservationByIdOrThrow(id);
        validateReservationIsNotInPast(reservation);

        Theme newTheme = findThemeByIdOrThrow(request.themeId());

        validateDuplicateReservation(request.themeId(), request.date(), request.timeId());

        reservation.update(newTheme, request.date(), newTime);
        reservationRepository.update(id, reservation);

        return ReservationResponse.from(reservation);
    }

    @Transactional
    public ReservationResponse updateReservationByAdmin(Long id, ReservationUpdateRequest request) {
        Reservation reservation = findReservationByIdOrThrow(id);
        Theme newTheme = findThemeByIdOrThrow(request.themeId());
        ReservationTime newTime = findTimeByIdOrThrow(request.timeId());

        validateDuplicateReservation(request.themeId(), request.date(), request.timeId());

        reservation.update(newTheme, request.date(), newTime);
        reservationRepository.update(id, reservation);

        return ReservationResponse.from(reservation);
    }

    @Transactional
    public void deleteReservationByUser(Long id) {
        Reservation reservation = findReservationByIdOrThrow(id);

        validateReservationIsNotInPast(reservation);

        reservationRepository.deleteById(id);
    }

    @Transactional
    public void deleteReservationByAdmin(Long id) {
        int deletedCount = reservationRepository.deleteById(id);

        if (deletedCount == 0) {
            throw new BusinessException(ReservationErrorCode.RESERVATION_NOT_FOUND);
        }
    }

    private void validateReservationDateIsNotInPast(LocalDate date) {
        LocalDate nowDate = LocalDate.now(clock);

        if (date.isBefore(nowDate)) {
            throw new BusinessException(ReservationErrorCode.PAST_RESERVATION);
        }
    }

    private void validateReservationTimeIsNotInPastWhenToday(LocalDate date, LocalTime time) {
        LocalDate nowDate = LocalDate.now(clock);
        LocalTime nowTime = LocalTime.now(clock);

        if (date.isEqual(nowDate) && time.isBefore(nowTime)) {
            throw new BusinessException(ReservationErrorCode.PAST_RESERVATION);
        }
    }

    private void validateReservationIsNotInPast(Reservation reservation) {
        validateReservationDateIsNotInPast(reservation.getDate());
        validateReservationTimeIsNotInPastWhenToday(
                reservation.getDate(),
                reservation.getTime().getStartAt()
        );
    }

    private void validateDuplicateReservation(Long themeId, LocalDate date, Long timeId) {
        if (reservationRepository.existsByThemeIdAndDateAndTimeId(themeId, date, timeId)) {
            throw new BusinessException(ReservationErrorCode.DUPLICATE_RESERVATION);
        }
    }

    private Reservation findReservationByIdOrThrow(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ReservationErrorCode.RESERVATION_NOT_FOUND));
    }

    private ReservationTime findTimeByIdOrThrow(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new BusinessException(TimeErrorCode.RESERVATION_TIME_NOT_FOUND));
    }

    private Theme findThemeByIdOrThrow(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new BusinessException(ThemeErrorCode.THEME_NOT_FOUND));
    }
}
