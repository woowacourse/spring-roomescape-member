package roomescape.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.response.ReservationTimeResponse;
import roomescape.dao.ReservationTimeDAO;
import roomescape.domain.ReservationTime;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.service.dto.ReservationTimeCreation;

@Service
public class ReservationTimeService {

    private final ReservationTimeDAO reservationTimeDAO;

    public ReservationTimeService(final ReservationTimeDAO reservationTimeDAO) {
        this.reservationTimeDAO = reservationTimeDAO;
    }

    public ReservationTimeResponse addReservationTime(final ReservationTimeCreation creation) {
        if (reservationTimeDAO.existsByStartAt(creation.startAt())) {
            throw new ExistedDuplicateValueException("이미 존재하는 예약 가능 시간입니다: %s".formatted(creation.startAt()));
        }
        ReservationTime reservationTime = new ReservationTime(creation.startAt());
        long id = reservationTimeDAO.insert(reservationTime);

        ReservationTime savedReservationTime = reservationTimeDAO.findById(id)
                .orElseThrow(NotExistedValueException::new);

        return ReservationTimeResponse.from(savedReservationTime);

    }

    public List<ReservationTime> findAll() {
        return reservationTimeDAO.findAll();
    }

    public Optional<ReservationTime> findById(long id) {
        return reservationTimeDAO.findById(id);
    }

    public void deleteById(final long id) {
        boolean deleted = reservationTimeDAO.deleteById(id);

        if (!deleted) {
            throw new NotExistedValueException("존재하지 않는 예약 시간입니다");
        }
    }
}
