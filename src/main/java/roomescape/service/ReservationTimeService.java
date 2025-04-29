package roomescape.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.response.ReservationTimeResponse;
import roomescape.dao.ReservationTimeDAO;
import roomescape.domain.ReservationTime;
import roomescape.service.dto.ReservationTimeCreation;

@Service
public class ReservationTimeService {

    private final ReservationTimeDAO reservationTimeDAO;

    public ReservationTimeService(final ReservationTimeDAO reservationTimeDAO) {
        this.reservationTimeDAO = reservationTimeDAO;
    }

    public ReservationTimeResponse addReservationTime(final ReservationTimeCreation creation) {
        if (reservationTimeDAO.existsByStartAt(creation.startAt())) {
            throw new IllegalArgumentException("이미 존재하는 예약 가능 시간입니다: %s".formatted(creation.startAt()));
        }
        ReservationTime reservationTime = new ReservationTime(creation.startAt());
        long id = reservationTimeDAO.insert(reservationTime);

        ReservationTime savedReservationTime = reservationTimeDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR]"));

        return ReservationTimeResponse.from(savedReservationTime);

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
