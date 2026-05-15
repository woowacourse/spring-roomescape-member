package roomescape.service;

import java.time.LocalTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.request.ReservationCreateRequest;
import roomescape.service.dto.request.ReservationModifyRequest;
import roomescape.service.dto.response.AvailableDateResponse;
import roomescape.service.dto.response.ReservationResponse;
import roomescape.service.dto.response.ReservationTimeResponse;
import roomescape.service.dto.response.ReservationTimeStatusResponse;
import roomescape.service.dto.response.ThemeResponse;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    private static final int RESERVABLE_DAYS_RANGE = 14;

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationService::mapDomainToDto)
                .toList();
    }

    public List<ReservationTimeStatusResponse> getReservationTimeStatuses(final LocalDate date, final Long themeId) {
        return reservationRepository.findReservationTimeStatusesByDateAndThemeId(date, themeId)
                .stream()
                .map(reservationTimesWithStatus -> new ReservationTimeStatusResponse(
                        reservationTimesWithStatus.id(),
                        reservationTimesWithStatus.startAt(),
                        reservationTimesWithStatus.reserved()
                ))
                .toList();
    }

    public ReservationResponse create(final ReservationCreateRequest data) {
        final LocalDate date = data.date();
        validateDate(date);
        final Long timeId = data.timeId();
        final ReservationTime reservationTime = getReservationTime(timeId);
        validateReservationTime(date, reservationTime);
        final Long themeId = data.themeId();
        final Theme theme = getTheme(themeId);

        validateAvailable(date, timeId, themeId);

        final Reservation newReservation = Reservation.create(data.name(), date, reservationTime, theme);

        final Reservation savedReservation = reservationRepository.save(newReservation);
        return mapDomainToDto(savedReservation);
    }

    public void delete(final Long reservationId) {
        final boolean deleted = reservationRepository.deleteById(reservationId);

        if (!deleted) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }
    }

    public void deleteWithValidation(final Long reservationId, final String name) {
        Reservation reservation = getReservation(reservationId);
        validateReservationOwner(reservation, name);
        final LocalDate date = reservation.getDate();
        validateDate(date);
        final ReservationTime reservationTime = reservation.getTime();
        validateReservationTime(date, reservationTime);
        delete(reservationId);
    }

    public AvailableDateResponse getReservationOptions() {
        LocalDate today = LocalDate.now(clock);
        List<LocalDate> dates = today.datesUntil(today.plusDays(RESERVABLE_DAYS_RANGE)).toList();

        return new AvailableDateResponse(dates);
    }

    public List<ReservationResponse> getReservationsByName(final String name) {
        List<Reservation> reservations = reservationRepository.findByName(name);
        return reservations.stream()
                .map(ReservationService::mapDomainToDto)
                .toList();
    }

    public ReservationResponse modify(final ReservationModifyRequest reservationModifyRequest) {
        final Long reservationId = reservationModifyRequest.reservationId();
        final Reservation originalReservation = getReservation(reservationId);
        final String personName = reservationModifyRequest.name();
        validateReservationOwner(originalReservation, personName);

        final LocalDate originalDate = originalReservation.getDate();
        final ReservationTime originalTime = originalReservation.getTime();
        validateDate(originalDate);
        validateReservationTime(originalDate, originalTime);

        final Long timeId = Objects.requireNonNullElse(
                reservationModifyRequest.timeId(),
                originalTime.getId()
        );
        final ReservationTime reservationTime = getReservationTime(timeId);
        final Reservation modifiedReservation = originalReservation.modify(
                reservationModifyRequest.date(),
                reservationTime
        );

        final LocalDate date = modifiedReservation.getDate();
        validateDate(date);
        validateReservationTime(date, reservationTime);
        validateAvailable(date, timeId, modifiedReservation.getTheme().getId());

        reservationRepository.updateDateAndTime(modifiedReservation);
        return mapDomainToDto(modifiedReservation);
    }

    private static ReservationResponse mapDomainToDto(final Reservation reservation) {
        final ReservationTime reservationTime = reservation.getTime();
        final Theme theme = reservation.getTheme();

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                new ReservationTimeResponse(
                        reservationTime.getId(),
                        reservationTime.getStartAt(),
                        reservationTime.getEndAt()
                ),
                new ThemeResponse(
                        theme.getId(),
                        theme.getName(),
                        theme.getDescription(),
                        theme.getThumbnailUrl()
                )
        );
    }

    private Reservation getReservation(final Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
    }

    private ReservationTime getReservationTime(final Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));
    }

    private Theme getTheme(final Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
    }

    private void validateDate(final LocalDate date) {
        final LocalDate today = LocalDate.now(clock);
        if (date.isBefore(today)) {
            throw new IllegalArgumentException("이미 지난 날짜입니다.");
        }
    }

    private void validateReservationTime(final LocalDate date, final ReservationTime reservationTime) {
        final LocalDate today = LocalDate.now(clock);
        final LocalTime now = LocalTime.now(clock);
        if (date.equals(today) && reservationTime.isBefore(now)) {
            throw new IllegalArgumentException("이미 지난 시간대입니다.");
        }
    }

    private void validateAvailable(final LocalDate date, final Long timeId, final Long themeId) {
        boolean isAlreadyReserved = reservationRepository.existsByDateAndTimeIdAndThemeId(
                date,
                timeId,
                themeId
        );

        if (isAlreadyReserved) {
            throw new IllegalArgumentException("예약이 마감되었습니다.");
        }
    }

    private void validateReservationOwner(final Reservation reservation, final String name) {
        final String reservationOwnerName = reservation.getName();
        final boolean isUserNameMatched = reservationOwnerName.equals(name);

        if (!isUserNameMatched) {
            throw new IllegalArgumentException("예약자와 사용자 이름이 일치하지 않습니다.");
        }
    }
}
