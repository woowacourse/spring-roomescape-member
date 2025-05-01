package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.Dao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.ReservationTimeResponse;
import roomescape.theme.domain.Theme;

@Service
public class ReservationService {
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
        validateDate(reservationRequest, reservationTimeResult);
        validateAvailability(reservationRequest, reservationTimeResult);
        Theme themeResult = searchTheme(reservationRequest);

        Reservation newReservation = new Reservation(
                null,
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

    private void validateDate(
            final ReservationRequest reservationRequest,
            final ReservationTime reservationTimeResult
    ) {
        if (reservationRequest.date().isEqual(LocalDate.now())
                && reservationTimeResult.getStartAt().isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("[ERROR] 이미 지난 시간으로는 예약할 수 없습니다.");
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
            throw new IllegalArgumentException("[ERROR] 이미 예약이 존재합니다.");
        }
    }

    public void deleteById(final Long id) {
        searchReservation(id);

        reservationDao.deleteById(id);
    }

    private Reservation searchReservation(final Long id) {
        return reservationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] id에 해당하는 예약이 존재하지 않습니다"));
    }

    private ReservationTime searchReservationTime(final ReservationRequest reservationRequest) {
        return reservationTimeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 시간 id가 존재하지 않습니다."));
    }

    private Theme searchTheme(final ReservationRequest reservationRequest) {
        return themeDao.findById(reservationRequest.themeId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 테마 id가 존재하지 않습니다."));
    }
}
