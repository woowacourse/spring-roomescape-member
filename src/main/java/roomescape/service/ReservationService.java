package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.AddReservationDto;
import roomescape.exception.InvalidReservationException;
import roomescape.exception.InvalidReservationTimeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional
    public long addReservation(AddReservationDto newReservation) {
        ReservationTime reservationTime = reservationTimeRepository.findById(newReservation.timeId())
                .orElseThrow(() -> new InvalidReservationTimeException("존재하지 않는 id입니다."));

        Reservation reservation = newReservation.toReservation(reservationTime);

        validateSameReservation(reservation);
        LocalDateTime currentDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        validateAddReservationDateTime(reservation, currentDateTime);
        return reservationRepository.add(reservation);
    }


    private void validateSameReservation(Reservation reservation) {
        if (reservationRepository.existByDateAndTimeId(reservation)) {
            throw new InvalidReservationException("중복된 예약신청입니다");
        }
    }

    private void validateAddReservationDateTime(Reservation newReservation, LocalDateTime currentDateTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(newReservation.getDate(), newReservation.getStartAt());
        if (reservationDateTime.isBefore(currentDateTime)) {
            throw new InvalidReservationException("과거 시간에 예약할 수 없습니다.");
        }
    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }
}
