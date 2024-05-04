package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.reservation.ReservationTimeInfoResponse;
import roomescape.dto.reservation.ReservationTimeInfosResponse;
import roomescape.dto.reservation.ReservationsResponse;
import roomescape.global.exception.model.ConflictException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public ReservationsResponse findAllReservations() {
        List<ReservationResponse> response = reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();

        return new ReservationsResponse(response);
    }

    public ReservationTimeInfosResponse findReservationByDateAndThemeId(LocalDate date, Long themeId) {
        List<Time> allTimes = timeRepository.findAll();
        Set<Long> reservedTimes = reservationRepository.findByDateAndThemeId(date, themeId).stream()
                .map(Reservation::getTime)
                .map(Time::getId)
                .collect(Collectors.toSet());

        List<ReservationTimeInfoResponse> response = new ArrayList<>();
        for (Time time : allTimes) {
            response.add(new ReservationTimeInfoResponse(time.getId(), time.getStartAt(),
                    reservedTimes.contains(time.getId())));
        }

        return new ReservationTimeInfosResponse(response);
    }

    public ReservationResponse createReservation(ReservationRequest reservationRequest) {
        LocalDate today = LocalDate.now();
        LocalDate requestDate = reservationRequest.date();
        Time time = timeRepository.findById(reservationRequest.timeId());
        Theme theme = themeRepository.findById(reservationRequest.themeId());

        validateDateAndTime(requestDate, today, time);
        validateReservationDuplicate(reservationRequest, theme);

        Reservation savedReservation = reservationRepository.insert(reservationRequest.toReservation(time, theme));

        return ReservationResponse.from(savedReservation);
    }

    private void validateDateAndTime(LocalDate requestDate, LocalDate today, Time time) {
        if (requestDate.isBefore(today) || (requestDate.isEqual(today) && time.getStartAt()
                .isBefore(LocalTime.now()))) {
            throw new ConflictException("지난 날짜나 시간은 예약이 불가능합니다.");
        }
    }

    private void validateReservationDuplicate(ReservationRequest reservationRequest, Theme theme) {
        List<Reservation> duplicateTimeReservation = reservationRepository.findByTimeIdAndDateAndThemeId(
                reservationRequest.timeId(), reservationRequest.date(), theme.getId());

        if (duplicateTimeReservation.size() > 0) {
            throw new ConflictException("이미 해당 날짜/시간/테마에 예약이 존재합니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
