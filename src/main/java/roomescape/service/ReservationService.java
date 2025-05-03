package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository repository;

    public ReservationService(final ReservationRepository repository) {
        this.repository = repository;
    }

    public List<ReservationResponse> findAll() {
        return repository.findAllReservations();
    }

    public void deleteReservation(Long id) {
        repository.deleteReservationById(id);
    }

    public ReservationResponse save(ReservationRequest request) {
        ReservationTime reservationTime = getReservationTime(request.timeId());
        Theme theme = getTheme(request.themeId());

        validateSaveReservation(request, reservationTime);
        Reservation reservation = new Reservation(
                request.name(),
                request.date(),
                reservationTime,
                theme
        );
        return getReservationResponse(reservation);
    }

    private ReservationTime getReservationTime(final Long timeId) {
        return repository.findReservationTimeById(timeId);
    }

    private Theme getTheme(final Long themeId) {
        return repository.findThemeById(themeId);
    }

    private void validateSaveReservation(
            ReservationRequest request,
            ReservationTime reservationTime
    ) {
        validateIsDuplicate(request);
        validateNotPast(request.date(), reservationTime);
    }

    private void validateIsDuplicate(ReservationRequest request) {
        int count = repository.getCountByTimeIdAndThemeIdAndDate(request.timeId(), request.themeId(), request.date());
        if (count != 0) {
            throw new IllegalArgumentException("[ERROR] 해당 날짜와 시간에 대한 예약이 이미 존재합니다.");
        }
    }

    private void validateNotPast(LocalDate date, ReservationTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("[ERROR] 현재보다 과거 시간에는 예약이 불가능합니다.");
        }
    }

    private ReservationResponse getReservationResponse(Reservation reservation) {
        return ReservationResponse.from(repository.saveReservation(reservation));
    }
}
