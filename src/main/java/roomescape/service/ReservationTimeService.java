package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDAO;
import roomescape.dao.ReservationTimeDAO;
import roomescape.domain.ReservationTime;
import roomescape.dto.AvailableTimeResponse;
import roomescape.dto.ReservationTimeRequest;
import roomescape.service.exception.DeleteException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationDAO reservationDAO;
    private final ReservationTimeDAO reservationTimeDAO;

    public ReservationTimeService(ReservationDAO reservationDAO, ReservationTimeDAO reservationTimeDAO) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
    }

    public ReservationTime save(ReservationTimeRequest reservationTimeRequest) {
        validateTime(reservationTimeRequest);
        ReservationTime reservationTime = new ReservationTime(reservationTimeRequest.startAt());
        return reservationTimeDAO.insert(reservationTime);
    }

    private void validateTime(ReservationTimeRequest reservationTimeRequest) {
        List<ReservationTime> reservationTimes = reservationTimeDAO.selectAll();
        reservationTimes.forEach(reservationTime -> reservationTime.validateNotDuplicated(reservationTimeRequest.startAt()));
    }

    public List<ReservationTime> findAll() {
        return reservationTimeDAO.selectAll();
    }

    public List<AvailableTimeResponse> findAvailableTimes(LocalDate date, Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeDAO.selectAll();
        List<Long> reservedTimeIds = reservationDAO.findReservedTimeIds(date, themeId);

        return reservationTimes.stream()
                .map(reservationTime -> AvailableTimeResponse.from(reservationTime, isReservedTime(reservationTime, reservedTimeIds)))
                .toList();
    }

    private boolean isReservedTime(ReservationTime reservationTime, List<Long> reservedTimeIds) {
        return reservedTimeIds.contains(reservationTime.getId());
    }

    public void delete(Long id) {
        if (reservationDAO.hasReservationTime(id)) {
            throw new DeleteException("해당 시간에 대한 예약이 존재하여 삭제할 수 없습니다.");
        }
        reservationTimeDAO.deleteById(id);
    }
}
