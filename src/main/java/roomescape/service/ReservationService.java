package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.response.ReservationResponse;
import roomescape.dao.ReservationDAO;
import roomescape.dao.ReservationTimeDAO;
import roomescape.dao.ThemeDAO;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.service.dto.ReservationCreation;

@Service
public class ReservationService {

    private final ReservationDAO reservationDAO;
    private final ReservationTimeDAO reservationTimeDAO;
    private final ThemeDAO themeDAO;

    public ReservationService(final ReservationDAO reservationDAO,
                              final ReservationTimeDAO reservationTimeDAO,
                              final ThemeDAO themeDAO) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
        this.themeDAO = themeDAO;
    }

    public ReservationResponse addReservation(final ReservationCreation creation) {
        ReservationTime reservationTime = findReservationTimeByTimeId(creation.timeId());
        Theme theme = themeDAO.findById(creation.themeId())
                .orElseThrow(NotExistedValueException::new);
        Reservation reservation = new Reservation(creation.name(), creation.date(), reservationTime, theme);

        validateDuplicateReservation(reservation);

        long savedId = reservationDAO.insert(reservation);
        Reservation savedReservation = reservationDAO.findById(savedId)
                .orElseThrow(NotExistedValueException::new);

        return ReservationResponse.from(savedReservation);
    }

    private void validateDuplicateReservation(final Reservation reservation) {
        if (existsSameDateTimeAndTheme(reservation)) {
            throw new ExistedDuplicateValueException("이미 예약이 존재하는 시간입니다: date=%s, time=%s"
                    .formatted(reservation.getDate(), reservation.getTime().getStartAt()));
        }
    }

    private boolean existsSameDateTimeAndTheme(final Reservation reservation) {
        return reservationDAO.existsByDateAndTimeIdAndThemeId(reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getTheme().getId());
    }

    public List<Reservation> findAll() {
        return reservationDAO.findAll();
    }

    private ReservationTime findReservationTimeByTimeId(final long timeId) {
        return reservationTimeDAO.findById(timeId)
                .orElseThrow(() -> new NotExistedValueException("존재하지 않는 예약 가능 시간입니다: timeId=%d"
                        .formatted(timeId)));
    }

    public void removeReservationById(final long id) {
        boolean deleted = reservationDAO.deleteById(id);

        if (!deleted) {
            throw new NotExistedValueException("존재하지 않는 예약입니다");
        }
    }
}
