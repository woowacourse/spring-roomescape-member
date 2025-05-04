package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.AvailableReservationTime;
import roomescape.domain.ReservationTime;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.exception.custom.PharmaceuticalViolationException;
import roomescape.service.dto.ReservationTimeCreation;

@Service
public class ReservationTimeService {

    private final ReservationDao reservationDAO;
    private final ReservationTimeDao reservationTimeDAO;

    public ReservationTimeService(final ReservationDao reservationDAO,
                                  final ReservationTimeDao reservationTimeDAO) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
    }

    public ReservationTime addReservationTime(final ReservationTimeCreation creation) {
        if (reservationTimeDAO.existsByStartAt(creation.startAt())) {
            throw new ExistedDuplicateValueException("이미 존재하는 예약 가능 시간입니다: %s".formatted(creation.startAt()));
        }
        final ReservationTime reservationTime = new ReservationTime(creation.startAt());
        final long id = reservationTimeDAO.insert(reservationTime);

        return findById(id);
    }

    private ReservationTime findById(final long id) {
        return reservationTimeDAO.findById(id)
                .orElseThrow(() -> new NotExistedValueException("존재하지 않는 예약 시간입니다"));
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
        if (reservationDAO.existsByTimeId(id)) {
            throw new PharmaceuticalViolationException("사용 중인 예약 시간입니다");
        }

        boolean deleted = reservationTimeDAO.deleteById(id);

        if (!deleted) {
            throw new NotExistedValueException("존재하지 않는 예약 시간입니다");
        }
    }
}
