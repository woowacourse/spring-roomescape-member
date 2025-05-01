package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.time.controller.dto.AvailableTimeResponse;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> getAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse add(ReservationRequest request) {
        ReservationTime findTime = reservationTimeRepository.findById(request.timeId());

        validateDateAndTime(request.date(), findTime.getStartAt());
        validateDuplicateReservation(request.date(), request.timeId(), request.themeId());

        Theme findTheme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        Reservation reservation = request.toReservationWithoutId(findTime, findTheme);

        Long id = reservationRepository.saveAndReturnId(reservation);
        return ReservationResponse.from(reservation.withId(id));
    }

    private void validateDateAndTime(LocalDate date, LocalTime time){
        LocalDate now = LocalDate.now();
        if (date.isBefore(now)) {
            throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
        }
        if (date.equals(now)) {
            if (time.isBefore(LocalTime.now())) {
                throw new IllegalArgumentException("지난 시각은 예약할 수 없습니다.");
            }
        }
    }

    public void remove(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<AvailableTimeResponse> getAvailableTimes(LocalDate date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findAllByDateAndThemeId(date,
                themeId);

        List<ReservationTime> times = reservationTimeRepository.findAll();

        List<AvailableTimeResponse> timeResponses = new ArrayList<>();

        for (ReservationTime time : times) {
            timeResponses.add(
                    AvailableTimeResponse.from(time.getStartAt(), time.getId(), isAlreadyBooked(time, reservations)));
        }

        return timeResponses;
    }

    private Boolean isAlreadyBooked(ReservationTime time, List<Reservation> reservations) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTimeId().equals(time.getId()));
    }

    private void validateDuplicateReservation(LocalDate localDate, Long timeId, Long themeId) {
        if (reservationRepository.existReservationByDateAndTimeIdAndThemeId(localDate, timeId, themeId)) {
            throw new IllegalArgumentException("해당 날짜, 시간, 테마에 대한 동일한 예약이 존재합니다.");
        }
    }
}
