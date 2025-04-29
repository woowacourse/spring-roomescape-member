package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponse> getAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse add(ReservationRequest request) {
        ReservationTime findTime = reservationTimeRepository.findById(request.timeId());
        validateDateAndTime(request.date(),findTime.getStartAt());
        validateDuplicateReservation(request.date(), request.timeId());
        Reservation reservation = request.toReservationWithoutId(findTime);

        Long id = reservationRepository.saveAndReturnId(reservation);
        return ReservationResponse.from(reservation.withId(id));
    }

    private void validateDateAndTime(LocalDate date, LocalTime time){
        LocalDate now = LocalDate.now();
        if(date.isBefore(now)){
            throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
        }
        if(date.equals(now)){
            if(time.isBefore(LocalTime.now())){
                throw new IllegalArgumentException("지난 시각은 예약할 수 없습니다.");
            }
        }
    }

    private void validateDuplicateReservation(LocalDate localDate, Long timeId){
        if(reservationRepository.existByDateAndTimeId(localDate, timeId)){
            throw new IllegalArgumentException("해당 시간에 대한 예약이 존재합니다.");
        }
    }

    public void remove(Long id) {
        reservationRepository.deleteById(id);
    }

}
