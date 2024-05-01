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
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final Clock clock;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              Clock clock) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
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
        Reservation reservation = reservationRequest.toReservation(reservationTime);

        validateDateTimeNotPassed(reservation.getDate(), reservationTime.getStartAt());
        validateDuplicatedReservation(reservation);

        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    private void validateDateTimeNotPassed(LocalDate date, LocalTime startAt) {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime reservationDateTime = LocalDateTime.of(date, startAt);

        if (reservationDateTime.isBefore(now)) {
            throw new IllegalArgumentException("현재 시간 이후의 시간만 예약 가능합니다.");
        }
    }

    private void validateDuplicatedReservation(Reservation reservation) {
        if (reservationRepository.existsByDateAndTimeId(reservation.getDate(), reservation.getTimeId())) {
            throw new IllegalArgumentException("해당 날짜/시간에 이미 예약이 존재합니다.");
        }
    }

    @Transactional
    public void deleteReservationById(Long id) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 예약이 존재하지 않습니다."));

        reservationRepository.deleteById(id);
    }
}
