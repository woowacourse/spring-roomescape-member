package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.reservation.ReservationCreateRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.repository.ThemeRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private static final String RESERVATION_NOT_FOUND = "존재하지 않는 예약입니다.";
    private static final String RESERVATION_TIME_NOT_FOUND = "존재하지 않는 예약 시간입니다.";
    private static final String RESERVATION_DATETIME_IS_INVALID = "이미 지난 날짜는 예약할 수 없습니다.";
    public static final String THEME_NOT_FOUND = "존재하지 않는 테마입니다.";

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> readReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse readReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESERVATION_NOT_FOUND));
        return ReservationResponse.from(reservation);
    }

    public ReservationResponse createReservation(ReservationCreateRequest request) {
        ReservationTime reservationTime = findReservationTimeById(request.timeId());
        Theme theme = findThemeById(request.themeId());
        Reservation reservation = request.toReservation(reservationTime, theme);

        validateDuplicated(reservation);
        validateRequestedTime(reservation, reservationTime);

        Reservation newReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(newReservation);
    }

    private void validateDuplicated(Reservation reservation) {
        reservationRepository.findAll().stream()
                .filter(reservation::isDuplicated)
                .findFirst()
                .ifPresent(duplicatedReservation -> validateSameUser(duplicatedReservation, reservation));
    }

    private void validateSameUser(Reservation duplicatedReservation, Reservation reservation) {
        if (duplicatedReservation.isSameUser(reservation)) {
            throw new BadRequestException("중복된 예약입니다.");
        }

        throw new BadRequestException("이미 예약된 테마입니다.");
    }

    private Theme findThemeById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(THEME_NOT_FOUND));
    }

    private ReservationTime findReservationTimeById(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESERVATION_TIME_NOT_FOUND));
    }

    private void validateRequestedTime(Reservation reservation, ReservationTime reservationTime) {
        LocalDateTime requestedDateTime = LocalDateTime.of(reservation.getDate(), reservationTime.getStartAt());
        if (requestedDateTime.isBefore(LocalDateTime.now())) {
            throw new BadRequestException(RESERVATION_DATETIME_IS_INVALID);
        }
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
