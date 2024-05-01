package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.time.Time;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.global.exception.model.ConflictException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.TimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
    }

    public List<ReservationResponse> findAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    // TODO: 메서드 분리
    public ReservationResponse createReservation(ReservationRequest reservationRequest) {
        LocalDate today = LocalDate.now();
        LocalDate requestDate = reservationRequest.date();
        Time time = timeRepository.findById(reservationRequest.timeId());

        if (requestDate.isBefore(today) || (requestDate.isEqual(today) && time.getStartAt().isBefore(LocalTime.now()))) {
            throw new ConflictException("지난 날짜나 시간은 예약이 불가능합니다.");
        }

        List<Reservation> duplicateTimeReservation = reservationRepository.findByTimeIdAndDate(
                reservationRequest.timeId(), reservationRequest.date());
        if (duplicateTimeReservation.size() > 0) {
            throw new ConflictException("이미 해당 날짜와 시간에 예약이 존재합니다.");
        }

        Reservation savedReservation = reservationRepository.save(reservationRequest.toReservation(time));

        return ReservationResponse.from(savedReservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }
}
