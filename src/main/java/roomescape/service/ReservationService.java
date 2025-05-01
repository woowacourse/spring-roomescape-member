package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.response.ReservationResponse;
import roomescape.dao.ReservationDAO;
import roomescape.dao.ReservationTimeDAO;
import roomescape.dao.RoomThemeDAO;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.exception.custom.PharmaceuticalViolationException;
import roomescape.service.dto.ReservationCreation;

@Service
public class ReservationService {

    private final ReservationDAO reservationDAO;
    private final ReservationTimeDAO reservationTimeDAO;
    private final RoomThemeDAO themeDAO;

    public ReservationService(final ReservationDAO reservationDAO,
                              final ReservationTimeDAO reservationTimeDAO,
                              final RoomThemeDAO themeDAO) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
        this.themeDAO = themeDAO;
    }

    public ReservationResponse addReservation(final ReservationCreation creation) {
        final ReservationTime reservationTime = findReservationTimeByTimeId(creation.timeId());
        final RoomTheme theme = findThemeByThemeId(creation.themeId());
        final Reservation reservation = new Reservation(creation.name(), creation.date(), reservationTime, theme);

        validatePastDateAndTime(reservation.getDate(), reservation.getTime());
        validateDuplicateReservation(reservation);

        final long savedId = reservationDAO.insert(reservation);
        final Reservation savedReservation = reservationDAO.findById(savedId)
                .orElseThrow(NotExistedValueException::new);

        return ReservationResponse.from(savedReservation);
    }

    private ReservationTime findReservationTimeByTimeId(final long timeId) {
        return reservationTimeDAO.findById(timeId)
                .orElseThrow(() -> new NotExistedValueException("존재하지 않는 예약 가능 시간입니다: timeId=%d"
                        .formatted(timeId)));
    }

    private RoomTheme findThemeByThemeId(final long themeId) {
        return themeDAO.findById(themeId)
                .orElseThrow(() -> new NotExistedValueException("존재하지 않는 테마 입니다"));
    }

    private void validatePastDateAndTime(final LocalDate date, final ReservationTime time) {
        final LocalDate currentDate = LocalDate.now();

        final boolean isPastDate = date.isBefore(currentDate);
        final boolean isPastTime = date.isEqual(currentDate) && time.getStartAt().isBefore(LocalTime.now());

        if (isPastDate || isPastTime) {
            throw new PharmaceuticalViolationException("과거 시점은 예약할 수 없습니다");
        }
    }

    private void validateDuplicateReservation(final Reservation reservation) {
        if (existsSameReservation(reservation)) {
            throw new ExistedDuplicateValueException("이미 예약이 존재하는 시간입니다: date=%s, time=%s"
                    .formatted(reservation.getDate(), reservation.getTime().getStartAt()));
        }
    }

    private boolean existsSameReservation(final Reservation reservation) {
        return reservationDAO.existSameReservation(reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getTheme().getId());
    }

    public List<ReservationResponse> findAll() {
        return reservationDAO.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void removeReservationById(final long id) {
        boolean deleted = reservationDAO.deleteById(id);

        if (!deleted) {
            throw new NotExistedValueException("존재하지 않는 예약입니다");
        }
    }
}
