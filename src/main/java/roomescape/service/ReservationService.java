package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.dto.reservation.ReservationAvailableTimeResponse;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.global.exception.ApplicationException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              TimeRepository timeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> findAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationAvailableTimeResponse> findReservationByDateAndThemeId(LocalDate date, Long themeId) {
        List<Time> allTimes = timeRepository.findAll();
        Set<Long> reservedTimes = reservationRepository.findByDateAndThemeId(date, themeId).stream()
                .map(Reservation::getTime)
                .map(Time::getId)
                .collect(Collectors.toSet());

        List<ReservationAvailableTimeResponse> response = new ArrayList<>();
        for (Time time : allTimes) {
            response.add(new ReservationAvailableTimeResponse(time.getId(), time.getStartAt(),
                    reservedTimes.contains(time.getId())));
        }

        return response;
    }

    public ReservationResponse createReservation(ReservationRequest reservationRequest) {
        LocalDate today = LocalDate.now();
        LocalDate requestDate = reservationRequest.date();
        Time time = timeRepository.findById(reservationRequest.timeId());
        Theme theme = themeRepository.findById(reservationRequest.themeId());

        validateDateAndTime(requestDate, today, time);
        validateReservationDuplicate(reservationRequest, theme);

        Reservation savedReservation = reservationRepository.save(reservationRequest.toReservation(time, theme));

        return ReservationResponse.from(savedReservation);
    }

    private void validateDateAndTime(LocalDate requestDate, LocalDate today, Time time) {
        LocalTime currentTime = LocalTime.now();
        if (requestDate.isBefore(today) || (requestDate.isEqual(today) && time.getStartAt().isBefore(currentTime))) {
            throw new ApplicationException("지난 날짜나 시간은 예약이 불가능합니다.");
        }
    }

    private void validateReservationDuplicate(ReservationRequest reservationRequest, Theme theme) {
        List<Reservation> duplicateTimeReservation = reservationRepository.findByTimeIdAndDateThemeId(
                reservationRequest.timeId(), reservationRequest.date(), theme.getId());

        if (duplicateTimeReservation.size() > 0) {
            throw new ApplicationException("이미 해당 날짜/시간/테마에 예약이 존재합니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }
}
