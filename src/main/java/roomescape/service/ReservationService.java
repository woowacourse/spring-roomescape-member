package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.RoomThemeDao;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.roomtheme.RoomTheme;
import roomescape.exception.custom.BusinessRuleViolationException;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.service.dto.ReservationCreation;

@Service
public class ReservationService {

    private final ReservationDao reservationDAO;
    private final ReservationTimeDao reservationTimeDAO;
    private final RoomThemeDao themeDAO;

    public ReservationService(final ReservationDao reservationDAO,
                              final ReservationTimeDao reservationTimeDAO,
                              final RoomThemeDao themeDAO) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
        this.themeDAO = themeDAO;
    }

    public Reservation addReservation(final ReservationCreation creation) {
        final ReservationTime reservationTime = findReservationTimeByTimeId(creation.timeId());
        final RoomTheme theme = findThemeByThemeId(creation.themeId());
        final Reservation reservation = new Reservation(creation.name(), creation.date(), reservationTime, theme);

        validatePastDateAndTime(reservation);
        validateDuplicateReservation(reservation);

        final long savedId = reservationDAO.insert(reservation);
        return findById(savedId);
    }

    private ReservationTime findReservationTimeByTimeId(final long timeId) {
        return reservationTimeDAO.findById(timeId)
                .orElseThrow(() -> new NotExistedValueException("존재하지 않는 예약 가능 시간입니다: timeId=%d".formatted(timeId)));
    }

    private RoomTheme findThemeByThemeId(final long themeId) {
        return themeDAO.findById(themeId)
                .orElseThrow(() -> new NotExistedValueException("존재하지 않는 테마 입니다"));
    }

    private void validatePastDateAndTime(final Reservation reservation) {
        if (reservation.validatePastDateAndTime()) {
            throw new BusinessRuleViolationException("과거 시점은 예약할 수 없습니다");
        }
    }

    private void validateDuplicateReservation(final Reservation reservation) {
        if (existsSameReservation(reservation)) {
            throw new ExistedDuplicateValueException("이미 예약이 존재하는 시간입니다: date=%s, time=%s"
                    .formatted(reservation.getDate(), reservation.getTime().getStartAt()));
        }
    }

    private boolean existsSameReservation(final Reservation reservation) {
        return reservationDAO.existSameReservation(
                reservation.getDate(), reservation.getTime().getId(), reservation.getTheme().getId());
    }

    private Reservation findById(final long savedId) {
        return reservationDAO.findById(savedId)
                .orElseThrow(() -> new NotExistedValueException("존재하지 않는 예약입니다"));
    }

    public List<Reservation> findAllReservations() {
        return reservationDAO.findAll();
    }

    public void removeReservationById(final long id) {
        boolean deleted = reservationDAO.deleteById(id);

        if (!deleted) {
            throw new NotExistedValueException("존재하지 않는 예약입니다");
        }
    }
}
