package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.*;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.exception.CustomException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static roomescape.exception.CustomExceptionCode.RESERVATION_ALREADY_EXIST;
import static roomescape.exception.CustomExceptionCode.RESERVATION_FOR_PAST_NOT_ALLOWED;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse addReservation(ReservationRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId());
        validateNotPast(request.date(), reservationTime.getStartAt());
        validateNotDuplicatedTime(request.date(), request.timeId());
        Theme theme = themeRepository.findById(request.themeId());

        Reservation reservation = new Reservation(request.name(), request.date(), reservationTime, theme);
        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    private void validateNotDuplicatedTime(LocalDate date, Long id) {
        if (reservationRepository.existByDateAndTimeId(date, id)) {
            throw new CustomException(RESERVATION_ALREADY_EXIST);
        }
    }

    private void validateNotPast(LocalDate date, LocalTime time) {
        LocalDateTime reservationDateTime = date.atTime(time);
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new CustomException(RESERVATION_FOR_PAST_NOT_ALLOWED);
        }
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
