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
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.repository.UserRepository;
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
    public List<ReservationResponse> getAll(final Long themeId, final Long memberId, final LocalDate dateFrom,
                                            final LocalDate dateTo) {
        if (hasNoArguments(themeId, memberId, dateFrom, dateTo)) {
            return getAllReservationResponses(reservationRepository.findAll());
        }

        final String name = userRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다! id = " + memberId))
                .getName();

        return getAllReservationResponses(reservationRepository.findReservations(themeId, name, dateFrom, dateTo));
    }

    private boolean hasNoArguments(final Long themeId, final Long memberId, final LocalDate dateFrom,
                                   final LocalDate dateTo) {
        return themeId == null && memberId == null && dateFrom == null && dateTo == null;
    }

    private List<ReservationResponse> getAllReservationResponses(final List<Reservation> reservationRepository) {
        return reservationRepository.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse create(final ReservationRequest request) {
        if (reservationRepository.existsByDateAndTimeId(request.date(), request.timeId())) {
            throw new AlreadyInUseException("해당 예약은 이미 존재합니다!");
        }

        final Reservation reservation = getReservation(request);
        reservation.validateNotPastReservation(LocalDateTime.now());

        final Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    private Reservation getReservation(final ReservationRequest request) {
        final ReservationTime reservationTime = getReservationTime(request);
        final Theme theme = getTheme(request);
        final Name name = new Name(request.name());

        return Reservation.withoutId(name, request.date(), reservationTime, theme);
    }

    private ReservationTime getReservationTime(final ReservationRequest request) {
        final Long timeId = request.timeId();

        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException("reservationsTime not found id =" + timeId));
    }

    private Theme getTheme(final ReservationRequest request) {
        final Long themeId = request.themeId();
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException("theme not found id =" + themeId));
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
