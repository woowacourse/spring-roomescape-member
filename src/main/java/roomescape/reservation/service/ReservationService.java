package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.Dao;
import roomescape.common.exception.DuplicateException;
import roomescape.common.exception.InvalidIdException;
import roomescape.common.exception.InvalidTimeException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.admin.ReservationTimeResponse;
import roomescape.theme.domain.Theme;

@Service
public class ReservationService {
    private static final String INVALID_TIME_EXCEPTION_MESSAGE = "당일의 과거 시간대로는 예약할 수 없습니다.";
    private static final String DUPLICATE_RESERVATION_EXCEPTION_MESSAGE = "이미 동일한 예약이 존재합니다.";
    private static final String INVALID_TIME_ID_EXCEPTION_MESSAGE = "해당 시간 아이디는 존재하지 않습니다.";
    private static final String INVALID_THEME_ID_EXCEPTION_MESSAGE = "해당 테마 아이디는 존재하지 않습니다.";
    private static final String INVALID_ID_EXCEPTION_MESSAGE = "해당 예약 아이디는 존재하지 않습니다";

    private final Dao<Reservation> reservationDao;
    private final Dao<ReservationTime> reservationTimeDao;
    private final Dao<Theme> themeDao;

    public ReservationService(
            Dao<Reservation> reservationDao,
            Dao<ReservationTime> reservationTimeDao,
            Dao<Theme> themeDao
    ) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(reservation -> new ReservationResponse(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getTheme(),
                        reservation.getDate(),
                        ReservationTimeResponse.from(reservation.getTime()))
                )
                .toList();
    }

    public ReservationResponse add(final ReservationRequest reservationRequest) {
        ReservationTime reservationTimeResult = searchReservationTime(reservationRequest);
        validateTime(reservationRequest, reservationTimeResult);
        validateAvailability(reservationRequest, reservationTimeResult);
        Theme themeResult = searchTheme(reservationRequest);

        Reservation newReservation = new Reservation(
                reservationRequest.name(),
                reservationRequest.date(),
                reservationTimeResult,
                themeResult
        );
        Reservation savedReservation = reservationDao.add(newReservation);

        return new ReservationResponse(
                savedReservation.getId(),
                savedReservation.getName(),
                savedReservation.getTheme(),
                savedReservation.getDate(),
                ReservationTimeResponse.from(savedReservation.getTime())
        );
    }

    private void validateTime(
            final ReservationRequest reservationRequest,
            final ReservationTime reservationTimeResult
    ) {
        if (reservationRequest.date().isEqual(LocalDate.now())
                && reservationTimeResult.getStartAt().isBefore(LocalTime.now())) {
            throw new InvalidTimeException(INVALID_TIME_EXCEPTION_MESSAGE);
        }
    }

    private void validateAvailability(
            final ReservationRequest reservationRequest,
            final ReservationTime reservationTimeResult
    ) {
        List<Reservation> reservations = reservationDao.findAll();

        boolean isNotAvailable = reservations.stream()
                .anyMatch(reservation -> reservation.getDate().equals(reservationRequest.date())
                        && reservation.getTime().equals(reservationTimeResult));

        if (isNotAvailable) {
            throw new DuplicateException(DUPLICATE_RESERVATION_EXCEPTION_MESSAGE);
        }
    }

    public void deleteById(final Long id) {
        searchReservation(id);

        reservationDao.deleteById(id);
    }

    private Reservation searchReservation(final Long id) {
        return reservationDao.findById(id)
                .orElseThrow(() -> new InvalidIdException(INVALID_ID_EXCEPTION_MESSAGE));
    }

    private ReservationTime searchReservationTime(final ReservationRequest reservationRequest) {
        return reservationTimeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new InvalidIdException(INVALID_TIME_ID_EXCEPTION_MESSAGE));
    }

    private Theme searchTheme(final ReservationRequest reservationRequest) {
        return themeDao.findById(reservationRequest.themeId())
                .orElseThrow(() -> new InvalidIdException(INVALID_THEME_ID_EXCEPTION_MESSAGE));
    }
}
