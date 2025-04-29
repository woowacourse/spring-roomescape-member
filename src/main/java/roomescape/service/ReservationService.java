package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(final ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationResponse> getReservations() {
        return ReservationResponse.from(reservationRepository.findAll());
    }

    public ReservationResponse createReservation(final ReservationRequest reservationRequest) {
        String name = reservationRequest.name();
        LocalDate date = reservationRequest.date();
        Long timeId = reservationRequest.timeId();
        if (reservationRepository.isDuplicateDateAndTime(date, timeId)) {
            throw new IllegalArgumentException("해당 시간에는 예약이 존재합니다.");
        }

        Reservation reservation = new Reservation(name, date, new ReservationTime(timeId));

        final Reservation saved = reservationRepository.save(reservation);
        return ReservationResponse.from(saved);
    }

    public void delete(final Long id) {
        if (reservationRepository.findById(id) == null) {
            throw new NoSuchElementException("없는 예약번호 입니다.");
        }
        reservationRepository.deleteById(id);
    }
}
