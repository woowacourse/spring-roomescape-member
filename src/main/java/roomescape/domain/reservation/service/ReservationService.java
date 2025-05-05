package roomescape.domain.reservation.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private final Clock clock;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(Clock clock, ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.clock = clock;
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getAll() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse create(ReservationRequest request) {
        if (reservationRepository.existsByDateAndTimeId(request.date(), request.timeId())) {
            throw new AlreadyInUseException("해당 예약은 이미 존재합니다!");
        }

        Reservation reservation = getReservation(request);
        reservation.validateNotPastReservation(now());

        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    private Reservation getReservation(ReservationRequest request) {
        ReservationTime reservationTime = getReservationTime(request);
        Theme theme = getTheme(request);

        return Reservation.withoutId(request.name(), request.date(), reservationTime, theme);
    }

    private Theme getTheme(final ReservationRequest request) {
        Long themeId = request.themeId();
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException("theme not found id =" + themeId));
    }

    private ReservationTime getReservationTime(final ReservationRequest request) {
        Long timeId = request.timeId();

        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException("reservationsTime not found id =" + timeId));
    }

    @Transactional
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    private LocalDateTime now() {
        return LocalDateTime.now(clock);
    }

    @Transactional(readOnly = true)
    public List<BookedReservationTimeResponse> getAvailableTimes(LocalDate date, Long themeId) {
        Map<ReservationTime, Boolean> allTimes = processAlreadyBookedTimesMap(
                date, themeId);

        return allTimes.entrySet()
                .stream()
                .map(entry -> bookedReservationTimeResponseOf(entry.getKey(), entry.getValue()))
                .toList();
    }

    private Map<ReservationTime, Boolean> processAlreadyBookedTimesMap(LocalDate date, Long themeId) {
        List<ReservationTime> allTimes = reservationTimeRepository.findAll();
        Set<ReservationTime> bookedTimes = reservationRepository.findByDateAndThemeId(date, themeId)
                .stream()
                .map(Reservation::getReservationTime)
                .collect(Collectors.toSet());

        return allTimes.stream()
                .collect(Collectors.toMap(Function.identity(), bookedTimes::contains));
    }

    private BookedReservationTimeResponse bookedReservationTimeResponseOf(
            ReservationTime reservationTime, boolean alreadyBooked) {
        return new BookedReservationTimeResponse(
                ReservationTimeResponse.from(reservationTime), alreadyBooked);
    }
}
