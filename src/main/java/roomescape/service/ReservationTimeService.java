package roomescape.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDAO;
import roomescape.domain.ReservationTime;

@Service
public class ReservationTimeService {

    private final ReservationTimeDAO reservationTimeDAO;

    public ReservationTimeService(final ReservationTimeDAO reservationTimeDAO) {
        this.reservationTimeDAO = reservationTimeDAO;
    }

    public long addReservationTime(final ReservationTime reservationTime) {
        if (reservationTimeDAO.existsByStartAt(reservationTime.getStartAt())) {
            throw new IllegalArgumentException("이미 존재하는 예약 가능 시간입니다: %s".formatted(reservationTime.getStartAt()));
        }
        return reservationTimeDAO.insert(reservationTime);
    }

    public List<ReservationTime> findAll() {
        return reservationTimeDAO.findAll();
    }

    public Optional<ReservationTime> findById(long id) {
        return reservationTimeDAO.findById(id);
    }

    public boolean deleteById(final long id) {
        return reservationTimeDAO.deleteById(id);
    }
}
