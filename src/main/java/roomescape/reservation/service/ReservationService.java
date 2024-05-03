package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.reservation.dto.request.CreateReservationRequest;
import roomescape.reservation.dto.response.CreateReservationResponse;
import roomescape.reservation.dto.response.FindAvailableTimesResponse;
import roomescape.reservation.dto.response.FindReservationResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public CreateReservationResponse createReservation(final CreateReservationRequest createReservationRequest) {
        ReservationTime reservationTime = findReservationTime(createReservationRequest.timeId());
        Theme theme = findTheme(createReservationRequest.themeId());
        Reservation reservation = createReservationRequest.toReservation(reservationTime, theme);

        validateReservationIsPast(reservation);
        validateAlreadyExistReservation(reservationTime, theme, reservation);

        return CreateReservationResponse.of(reservationRepository.save(reservation));
    }

    private Reservation findReservation(final Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 예약이 존재하지 않습니다."));
    }

    private ReservationTime findReservationTime(final Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 예약 시간이 존재하지 않습니다."));
    }

    private Theme findTheme(final Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 테마가 존재하지 않습니다."));
    }

    private void validateReservationIsPast(final Reservation reservation) {
        if (reservation.isBeforeDateTimeThanNow(LocalDateTime.now())) {
            throw new IllegalArgumentException("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
        }
    }

    private void validateAlreadyExistReservation(final ReservationTime reservationTime, final Theme theme,
                                                 final Reservation reservation) {
        if (reservationRepository.existsByDateAndTimeAndTheme(reservation.getDate(), reservationTime.getId(),
                theme.getId())) {
            throw new IllegalStateException("동일한 시간의 예약이 존재합니다.");
        }
    }

    public List<FindReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(FindReservationResponse::of)
                .toList();
    }

    public FindReservationResponse getReservation(final Long id) {
        Reservation reservation = findReservation(id);
        return FindReservationResponse.of(reservation);
    }

    public List<FindAvailableTimesResponse> getAvailableTimes(final LocalDate date, final Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        List<Reservation> reservations = reservationRepository.findAllByDateAndThemeId(date, themeId);
        return reservationTimes.stream()
                .map(reservationTime -> generateFindAvailableTimesResponse(reservations, reservationTime))
                .toList();
    }

    private static FindAvailableTimesResponse generateFindAvailableTimesResponse(final List<Reservation> reservations,
                                                                                 final ReservationTime reservationTime) {
        return FindAvailableTimesResponse.of(
                reservationTime,
                reservations.stream()
                        .anyMatch(reservation -> reservation.isSameTime(reservationTime)));
    }

    public void deleteReservation(final Long id) {
        validateExistReservation(id);
        reservationRepository.deleteById(id);
    }

    private void validateExistReservation(final Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new NoSuchElementException("해당하는 예약이 존재하지 않습니다.");
        }
    }
}
