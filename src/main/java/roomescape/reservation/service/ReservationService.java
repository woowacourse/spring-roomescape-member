package roomescape.reservation.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.controller.dto.AvailableTimeResponse;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.reservation.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository,
                              Clock clock) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    public List<ReservationResponse> getAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse add(ReservationRequest request) {
        Reservation reservation = createReservationWithoutId(request);
        Long id = reservationRepository.saveAndReturnId(reservation);
        return ReservationResponse.from(reservation.withId(id));
    }

    public void remove(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<AvailableTimeResponse> getAvailableTimes(LocalDate date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findAllByThemeId(themeId)
                .stream()
                .filter(reservation -> reservation.hasSameDate(date))
                .toList();

        List<ReservationTime> times = reservationTimeRepository.findAll();
        return convertTimeToResponses(reservations, times);
    }

    private List<AvailableTimeResponse> convertTimeToResponses(List<Reservation> reservations,
                                                               List<ReservationTime> times) {
        return times.stream()
                .map(time -> AvailableTimeResponse.from(time.getStartAt(), time.getId(),
                        isAlreadyBooked(time, reservations)))
                .toList();
    }

    private Reservation createReservationWithoutId(ReservationRequest request) {
        ReservationTime findTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 시간 정보가 존재하지 않습니다."));

        validateDateAndTime(request.date(), findTime.getStartAt());
        validateDuplicateReservation(request.date(), request.timeId(), request.themeId());

        Theme findTheme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 테마가 존재하지 않습니다."));

        return request.toReservationWithoutId(findTime, findTheme);
    }

    private Boolean isAlreadyBooked(ReservationTime time, List<Reservation> reservations) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.hasTimeId(time.getId()));
    }

    private void validateDateAndTime(LocalDate date, LocalTime time) {
        LocalDate now = LocalDate.now(clock);
        if (date.isBefore(now)) {
            throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
        }
        if (date.equals(now) && time.isBefore(LocalTime.now(clock))) {
            throw new IllegalArgumentException("지난 시각은 예약할 수 없습니다.");
        }
    }

    private void validateDuplicateReservation(LocalDate date, Long timeId, Long themeId) {
        boolean hasDuplicatedReservation = reservationRepository.findAllByTimeId(timeId)
                .stream()
                .anyMatch(reservation -> reservation.hasThemeId(themeId) && reservation.hasSameDate(date));

        if (hasDuplicatedReservation) {
            throw new IllegalArgumentException("해당 날짜, 시간, 테마에 대한 동일한 예약이 존재합니다.");
        }
    }

}
