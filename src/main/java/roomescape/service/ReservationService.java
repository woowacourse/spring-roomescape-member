package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.repository.JdbcReservationTimeRepository;
import roomescape.repository.ReservationRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final JdbcReservationTimeRepository reservationTimeRepository;

    public ReservationService(
            final ReservationRepository reservationRepository,
            final JdbcReservationTimeRepository reservationTimeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<Reservation> read() {
        return reservationRepository.read();
    }

    public Reservation create(final String name, final LocalDate date, final Long timeId) {
        validateTimeId(timeId);
        Reservation newReservation = reservationRepository.create(name, date, timeId);
        return reservationRepository.findById(newReservation.getId());
    }

    public void delete(final long id) {
        reservationRepository.delete(id);
    }

    private void validateTimeId(final Long timeId) {
        if (timeId == null) {
            throw new IllegalArgumentException("[ERROR] 예약 시간 ID는 필수입니다.");
        }
        if (!reservationTimeRepository.existsById(timeId)) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 예약 시간입니다.");
        }
    }
}
