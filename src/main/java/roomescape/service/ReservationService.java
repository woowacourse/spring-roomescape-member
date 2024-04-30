package roomescape.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTimeRepository;
import roomescape.dto.app.ReservationAppRequest;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
        ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public Reservation save(ReservationAppRequest request) {
        try {
            LocalDate parsed = LocalDate.parse(request.date());
            Reservation newReservation = new Reservation(request.name(), parsed,
                reservationTimeRepository.findById(request.timeId()));
            return reservationRepository.save(newReservation);
        } catch (EmptyResultDataAccessException | DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("잘못된 예약 포맷을 입력했습니다.");
        }
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }
}
