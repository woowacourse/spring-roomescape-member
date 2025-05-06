package roomescape.domain.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import roomescape.common.exception.AlreadyInUseException;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.reservation.dto.BookedReservationTimeResponse;
import roomescape.domain.reservation.dto.ReservationRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dto.ReservationTimeResponse;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.domain.reservation.repository.ThemeRepository;

@Service
public class ReservationService {

    private static final boolean IS_ALREADY_BOOKED = true;
    private static final boolean IS_UNBOOKED = false;

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            final ReservationRepository reservationRepository,
            final ReservationTimeRepository reservationTimeRepository,
            final ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> getAll() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse create(final ReservationRequest request) {
        if (isAlreadyBooked(request)) {
            throw new AlreadyInUseException("reservation is already in use");
        }

        Reservation reservation = getReservation(request);
        LocalDateTime now = LocalDateTime.now();
        validateDateTime(now, reservation.getReservationDate(), reservation.getReservationStartTime());

        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    private boolean isAlreadyBooked(final ReservationRequest request) {
        return reservationRepository.existsByDateAndTimeIdAndThemeId(
                request.date(), request.timeId(), request.themeId()
        );
    }

    private Reservation getReservation(final ReservationRequest request) {
        ReservationTime reservationTime = gerReservationTime(request);
        Theme theme = getTheme(request);

        return Reservation.withoutId(request.name(), request.date(), reservationTime, theme);
    }

    private Theme getTheme(final ReservationRequest request) {
        Long themeId = request.themeId();
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException("theme not found id =" + themeId));
    }

    private ReservationTime gerReservationTime(final ReservationRequest request) {
        Long timeId = request.timeId();
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException("reservationsTime not found id =" + timeId));
    }

    private void validateDateTime(final LocalDateTime now, final LocalDate date, final LocalTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        if (now.isAfter(dateTime)) {
            throw new IllegalArgumentException("이미 지난 예약 시간입니다.");
        }
    }

    public void delete(final Long id) {
        reservationRepository.deleteById(id);
    }

    public List<BookedReservationTimeResponse> getAvailableTimes(final LocalDate date, final Long themeId) {
        Map<ReservationTime, Boolean> allTimes = processAlreadyBookedTimesMap(date, themeId);

        return allTimes.entrySet()
                .stream()
                .map(entry -> bookedReservationTimeResponseOf(entry.getKey(), entry.getValue()))
                .toList();
    }

    private Map<ReservationTime, Boolean> processAlreadyBookedTimesMap(final LocalDate date, final Long themeId) {
        Set<ReservationTime> alreadyBookedTimes = getAlreadyBookedTimes(date, themeId);

        return reservationTimeRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Function.identity(), time -> isAlreadyBookedTime(time, alreadyBookedTimes)));
    }

    private Boolean isAlreadyBookedTime(final ReservationTime time, final Set<ReservationTime> alreadyBookedTimes) {
        if (alreadyBookedTimes.contains(time)) {
            return IS_ALREADY_BOOKED;
        }
        return IS_UNBOOKED;
    }

    private BookedReservationTimeResponse bookedReservationTimeResponseOf(
            final ReservationTime reservationTime,
            final boolean isAlreadyBooked
    ) {
        return new BookedReservationTimeResponse(ReservationTimeResponse.from(reservationTime), isAlreadyBooked);
    }

    private Set<ReservationTime> getAlreadyBookedTimes(final LocalDate date, final Long themeId) {
        return reservationRepository.findByDateAndThemeId(date, themeId)
                .stream()
                .map(Reservation::getReservationTime)
                .collect(Collectors.toSet());
    }
}
