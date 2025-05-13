package roomescape.domain.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.AlreadyInUseException;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.repository.UserRepository;
import roomescape.domain.reservation.dto.reservation.ReservationCreateDto;
import roomescape.domain.reservation.dto.reservation.ReservationResponse;
import roomescape.domain.reservation.dto.reservationtime.BookedReservationTimeResponse;
import roomescape.domain.reservation.dto.reservationtime.ReservationTimeResponse;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.domain.reservation.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              final ThemeRepository themeRepository, final UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getAll(final Long themeId, final Long userId, final LocalDate dateFrom,
                                            final LocalDate dateTo) {
        if (hasNoIdArguments(themeId, userId) && hasNoDateArguments(dateFrom, dateTo)) {
            return getAllReservationResponses(reservationRepository.findAll());
        }

        return getAllReservationResponses(reservationRepository.findReservations(themeId, userId, dateFrom, dateTo));
    }

    private boolean hasNoIdArguments(final Long themeId, final Long userId) {
        return themeId == null && userId == null;
    }

    private boolean hasNoDateArguments(final LocalDate dateFrom, final LocalDate dateTo) {
        return dateFrom == null && dateTo == null;
    }

    private List<ReservationResponse> getAllReservationResponses(final List<Reservation> reservationRepository) {
        return reservationRepository.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse create(final ReservationCreateDto createDto) {
        if (reservationRepository.existsByDateAndTimeId(createDto.date(), createDto.timeId())) {
            throw new AlreadyInUseException("해당 예약은 이미 존재합니다!");
        }

        final User user = getUser(createDto.userId());
        final ReservationTime reservationTime = getReservationTime(createDto.timeId());
        final Theme theme = getTheme(createDto.themeId());

        final Reservation reservation = Reservation.withoutId(user, createDto.date(), reservationTime, theme);
        reservation.validateNotPastReservation(LocalDateTime.now());

        final Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    private Theme getTheme(final Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException("theme not found id =" + themeId));
    }

    private ReservationTime getReservationTime(final Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException("reservationsTime not found id =" + timeId));
    }

    private User getUser(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저가 존재하지 않습니다! id =" + userId));
    }

    @Transactional
    public void delete(final Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<BookedReservationTimeResponse> getAvailableTimes(final LocalDate date, final Long themeId) {
        final Map<ReservationTime, Boolean> allTimes = processAlreadyBookedTimesMap(date, themeId);

        return allTimes.entrySet()
                .stream()
                .map(entry -> bookedReservationTimeResponseOf(entry.getKey(), entry.getValue()))
                .toList();
    }

    private Map<ReservationTime, Boolean> processAlreadyBookedTimesMap(final LocalDate date, final Long themeId) {
        final List<ReservationTime> allTimes = reservationTimeRepository.findAll();
        final Set<ReservationTime> bookedTimes = reservationRepository.findByDateAndThemeId(date, themeId)
                .stream()
                .map(Reservation::getReservationTime)
                .collect(Collectors.toSet());

        return allTimes.stream()
                .collect(Collectors.toMap(Function.identity(), bookedTimes::contains));
    }

    private BookedReservationTimeResponse bookedReservationTimeResponseOf(final ReservationTime reservationTime,
                                                                          final boolean alreadyBooked) {
        return new BookedReservationTimeResponse(ReservationTimeResponse.from(reservationTime), alreadyBooked);
    }
}
