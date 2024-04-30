package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse addReservation(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationRequest.timeId());
        validateNotPast(reservationRequest.date(), reservationTime.getStartAt());
        validateNotDuplicatedTime(reservationRequest.date(), reservationRequest.timeId());

        Reservation reservation = new Reservation(reservationRequest.name(), reservationRequest.date(), reservationTime,
                new Theme(1L, "테마", "내용", "썸네일"));
        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    private void validateNotDuplicatedTime(LocalDate date, Long id) {
        if (reservationRepository.existByDateAndTimeId(date, id)) {
            throw new IllegalArgumentException("중복 예약은 불가능하다.");
        }
    }

    private void validateNotPast(LocalDate date, LocalTime time) {
        LocalDateTime reservationDateTime = date.atTime(time);
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("지나간 시간에 대한 예약 생성은 불가능하다.");
        }
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
