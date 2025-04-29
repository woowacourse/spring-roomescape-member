package roomescape.domain.reservation.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.reservation.dto.ReservationRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.repository.EntityRepository;

@Service
public class ReservationService {

    private final Clock clock;
    private final EntityRepository<Reservation> reservationRepository;
    private final EntityRepository<ReservationTime> reservationTimeRepository;

    public ReservationService(Clock clock, EntityRepository<Reservation> reservationRepository,
                              EntityRepository<ReservationTime> reservationTimeRepository) {
        this.clock = clock;
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponse> getAll() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse create(ReservationRequest request) {
        Long timeId = request.timeId();
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException("reservationsTime not found id =" + timeId));

        Reservation reservation = Reservation.withoutId(request.name(), request.date(), reservationTime);

        validateDateTime(now(), reservation.getReservationDate(), reservation.getReservationStratTime());

        Reservation saved = reservationRepository.save(reservation);

        return ReservationResponse.from(saved);
    }

    private void validateDateTime(LocalDateTime now, LocalDate date, LocalTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        if (now.isAfter(dateTime)) {
            throw new IllegalArgumentException("이미 지난 예약 시간입니다.");
        }
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    private LocalDateTime now(){
        return LocalDateTime.now(clock);
    }
}
