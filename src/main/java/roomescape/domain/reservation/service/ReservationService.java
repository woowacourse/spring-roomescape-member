package roomescape.domain.reservation.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

    public List<ReservationResponse> getAll() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse create(ReservationRequest request) {
        if (reservationRepository.existsByDateAndTimeId(request.date(), request.timeId())) {
            throw new AlreadyInUseException("reservation is already in use");
        }

        Reservation reservation = getReservation(request);

        validateDateTime(now(), reservation.getReservationDate(), reservation.getReservationStratTime());

        Reservation saved = reservationRepository.save(reservation);

        return ReservationResponse.from(saved);
    }

    private Reservation getReservation(ReservationRequest request) {
        Long timeId = request.timeId();
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException("reservationsTime not found id =" + timeId));

        Long themeId = request.themeId();
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException("theme not found id =" + themeId));

        return Reservation.withoutId(request.name(), request.date(), reservationTime, theme);
    }

    private void validateDateTime(LocalDateTime now, LocalDate date, LocalTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        if (now.isAfter(dateTime)) {
            throw new IllegalArgumentException("이미 지난 예약 시간입니다.");
        }
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    private LocalDateTime now() {
        return LocalDateTime.now(clock);
    }

    public List<BookedReservationTimeResponse> getAvailableTimes(LocalDate date, Long themeId) {
        Map<ReservationTime, Boolean> allTimes = processAlreadyBookedTimesMap(
                date, themeId);

        return allTimes.entrySet()
                .stream()
                .map(this::bookedReservationTimeResponseOf)
                .toList();
    }

    private Map<ReservationTime, Boolean> processAlreadyBookedTimesMap(LocalDate date, Long themeId) {
        Map<ReservationTime, Boolean> allTimes = reservationTimeRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Function.identity(), t -> false));

        reservationRepository.findByDateAndThemeId(date, themeId)
                .stream()
                .map(Reservation::getReservationTime)
                .forEach(bookedTime -> allTimes.put(bookedTime, true));

        return allTimes;
    }

    private BookedReservationTimeResponse bookedReservationTimeResponseOf(
            Entry<ReservationTime, Boolean> entry) {
        return new BookedReservationTimeResponse(
                ReservationTimeResponse.from(entry.getKey()), entry.getValue());
    }
}
