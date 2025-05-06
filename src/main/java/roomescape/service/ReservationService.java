package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.*;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.ResourceNotExistException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(final ReservationRepository reservationRepository, final ReservationTimeRepository reservationTimeRepository, final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll();
    }

    public void deleteReservation(Long id) {
        int count = reservationRepository.deleteById(id);
        if (count == 0) {
            throw new ResourceNotExistException();
        }
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
        return reservationTimeRepository.findById(timeId);
    }

    private Theme getTheme(final Long themeId) {
        return themeRepository.findById(themeId);
    }

    private void validateSaveReservation(
            ReservationRequest request,
            ReservationTime reservationTime
    ) {
        validateIsDuplicate(request);
        validateNotPast(request.date(), reservationTime);
    }

    private void validateIsDuplicate(ReservationRequest request) {
        boolean isReservationExist = reservationRepository.existByTimeIdAndThemeIdAndDate(request.timeId(), request.themeId(), request.date());
        if (isReservationExist) {
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
        return ReservationResponse.from(reservationRepository.save(reservation));
    }
}
