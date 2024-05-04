package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.service.dto.ReservationRequest;
import roomescape.service.dto.ReservationResponse;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository,
            Clock clock
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse addReservation(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeRepository.getById(reservationRequest.timeId());
        Theme theme = themeRepository.getById(reservationRequest.themeId());
        Reservation reservation = reservationRequest.toReservation(reservationTime, theme);

        validateDateTimeNotPassed(reservation.getDate(), reservationTime.getStartAt());
        validateDuplicatedReservation(reservation);

        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    private void validateDateTimeNotPassed(LocalDate date, LocalTime startAt) {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime reservationDateTime = LocalDateTime.of(date, startAt);

        if (reservationDateTime.isBefore(now)) {
            throw new IllegalArgumentException("지나간 날짜/시간에 대한 예약은 불가능합니다.");
        }
    }

    private void validateDuplicatedReservation(Reservation reservation) {
        if (reservationRepository.existsByReservation(
                reservation.getDate(),
                reservation.getTimeId(),
                reservation.getThemeId()
        )) {
            throw new IllegalArgumentException("해당 날짜/시간에 이미 예약이 존재합니다.");
        }
    }

    @Transactional
    public void deleteReservationById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new NoSuchElementException("해당 id의 예약이 존재하지 않습니다.");
        }

        reservationRepository.deleteById(id);
    }
}
