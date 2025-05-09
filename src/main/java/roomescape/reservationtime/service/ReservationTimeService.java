package roomescape.reservationtime.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.InUseException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservationtime.dao.ReservationTimeDao;
import roomescape.reservationtime.domain.AvailableReservationTime;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.service.dto.CreateReservationTimeServiceRequest;

@Service
public class ReservationTimeService {

    private final ReservationDao reservationDAO;
    private final ReservationTimeDao reservationTimeDAO;

    public ReservationTimeService(final ReservationDao reservationDAO,
                                  final ReservationTimeDao reservationTimeDAO) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
    }

    public ReservationTime addReservationTime(final CreateReservationTimeServiceRequest creation) {
        validateTimeNotDuplicated(creation);
        final ReservationTime reservationTime = new ReservationTime(creation.startAt());

        final long savedId = reservationTimeDAO.insert(reservationTime);
        return new ReservationTime(savedId, reservationTime);
    }

    private void validateTimeNotDuplicated(final CreateReservationTimeServiceRequest creation) {
        if (reservationTimeDAO.existsByStartAt(creation.startAt())) {
            throw new ExistedDuplicateValueException("이미 존재하는 예약 가능 시간입니다: %s".formatted(creation.startAt()));
        }
    }

    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeDAO.findAll();
    }

    public List<AvailableReservationTime> findAllAvailableTime(final LocalDate date, final long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeDAO.findAll();
        List<ReservationTime> bookedTimes = reservationTimeDAO.findAllBookedTime(date, themeId);

        return reservationTimes.stream()
                .map(time -> new AvailableReservationTime(bookedTimes, time))
                .toList();
    }

    public void deleteById(final long id) {
        validateTimeNotInUse(id);

        boolean deleted = reservationTimeDAO.deleteById(id);

        if (!deleted) {
            throw new NotExistedValueException("존재하지 않는 예약 시간입니다");
        }
    }

    private void validateTimeNotInUse(final long id) {
        if (reservationDAO.existsByTimeId(id)) {
            throw new InUseException("사용 중인 예약 시간입니다");
        }
    }
}
