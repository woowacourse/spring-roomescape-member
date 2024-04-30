package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDAO;
import roomescape.dao.ReservationTimeDAO;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;

import java.util.List;

@Service
public class ReservationTimeService {
    private final ReservationDAO reservationDAO;
    private final ReservationTimeDAO reservationTimeDAO;

    public ReservationTimeService(ReservationDAO reservationDAO, final ReservationTimeDAO reservationTimeDAO) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
    }

    public ReservationTime save(final ReservationTimeRequest reservationTimeRequest) {
        validateTime(reservationTimeRequest);

        final ReservationTime reservationTime = new ReservationTime(reservationTimeRequest.startAt());
        return reservationTimeDAO.insert(reservationTime);
    }

    private void validateTime(ReservationTimeRequest reservationTimeRequest) {
        if (hasDuplicatedTime(reservationTimeRequest)) {
            throw new IllegalArgumentException("중복된 시간을 예약할 수 없습니다.");
        }
    }

    private boolean hasDuplicatedTime(ReservationTimeRequest reservationTimeRequest) {
        List<ReservationTime> reservationTimes = reservationTimeDAO.selectAll();

        return reservationTimes.stream()
                .anyMatch(reservationTime -> reservationTime.isMatch(reservationTimeRequest.startAt()));
    }

    public List<ReservationTime> findAll() {
        return reservationTimeDAO.selectAll();
    }

    public void delete(final Long id) {
        if (reservationDAO.hasReservationTime(id)) {
            throw new IllegalArgumentException("해당 시간에 대한 예약이 존재하여 삭제할 수 없습니다.");
        }
        reservationTimeDAO.deleteById(id);
    }
}
