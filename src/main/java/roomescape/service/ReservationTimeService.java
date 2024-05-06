package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDAO;
import roomescape.dao.ReservationTimeDAO;
import roomescape.domain.ReservationTime;
import roomescape.dto.AvailableTimeResponse;
import roomescape.dto.ReservationTimeRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationTimeService {
    private final ReservationDAO reservationDAO;
    private final ReservationTimeDAO reservationTimeDAO;

    public ReservationTimeService(final ReservationDAO reservationDAO, final ReservationTimeDAO reservationTimeDAO) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
    }

    public ReservationTime save(final ReservationTimeRequest reservationTimeRequest) {
        final LocalTime requestReservationTime = reservationTimeRequest.startAt();
        validateDuplicatedTime(requestReservationTime);

        final ReservationTime reservationTime = new ReservationTime(requestReservationTime);
        return reservationTimeDAO.insert(reservationTime);
    }

    private void validateDuplicatedTime(final LocalTime requestReservationTime) {
        if (reservationTimeDAO.existReservationTimeOf(requestReservationTime)) {
            throw new IllegalArgumentException("중복된 시간을 예약할 수 없습니다.");
        }
    }

    public List<ReservationTime> findAll() {
        return reservationTimeDAO.selectAll();
    }

    public List<AvailableTimeResponse> findAvailableTimes(final LocalDate date, final Long themeId) {
        return reservationTimeDAO.findAvailableTimes(date, themeId);
    }

    public void delete(final Long id) {
        if (reservationDAO.existReservationTime(id)) {
            throw new IllegalArgumentException("해당 시간에 대한 예약이 존재하여 삭제할 수 없습니다.");
        }
        reservationTimeDAO.deleteById(id);
    }
}
