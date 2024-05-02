package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDAO;
import roomescape.dao.ReservationTimeDAO;
import roomescape.dao.ThemeDAO;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationDAO reservationDAO;
    private final ReservationTimeDAO reservationTimeDAO;
    private final ThemeDAO themeDAO;

    public ReservationService(final ReservationDAO reservationDAO, final ReservationTimeDAO reservationTimeDAO, ThemeDAO themeDAO) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
        this.themeDAO = themeDAO;
    }

    public Reservation save(final ReservationRequest reservationRequest) {
        validateReservation(reservationRequest);
        final ReservationTime reservationTime = reservationTimeDAO.findById(reservationRequest.timeId());
        final Theme theme = themeDAO.findById(reservationRequest.themeId());
        final Reservation reservation = reservationRequest.toEntity(reservationTime, theme);

        return reservationDAO.insert(reservation);
    }

    private void validateReservation(final ReservationRequest reservationRequest) {
        validateDuplicatedReservation(reservationRequest);
        validatePast(reservationRequest);
    }

    private void validateDuplicatedReservation(final ReservationRequest reservationRequest) {
        final LocalTime reservationTime = findRequestTime(reservationRequest);
        final List<Reservation> reservations = reservationDAO.selectAll();

        if (hasDuplicatedReservation(reservations, reservationTime, reservationRequest)) {
            throw new IllegalArgumentException("예약 날짜와 예약 시간이 중복될 수 없습니다.");
        }
    }

    private boolean hasDuplicatedReservation(final List<Reservation> reservations, final LocalTime reservationTime, final ReservationRequest reservationRequest) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.isDuplicatedReservation(reservationTime, reservationRequest));
    }

    private void validatePast(final ReservationRequest reservationRequest) {
        final LocalDate date = reservationRequest.date();
        final LocalTime reservationTime = findRequestTime(reservationRequest);

        final LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            throw new IllegalArgumentException("지나간 날짜에 예약을 등록할 수 없습니다.");
        }
        if (date.isEqual(today) && reservationTime.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("지나간 시간에 예약을 등록할 수 없습니다.");
        }
    }

    private LocalTime findRequestTime(final ReservationRequest reservationRequest) {
        final Long timeId = reservationRequest.timeId();
        final ReservationTime time = reservationTimeDAO.findById(timeId);
        return time.getStartAt();
    }

    public List<Reservation> findAll() {
        return reservationDAO.selectAll();
    }

    public void delete(final long id) {
        reservationDAO.deleteById(id);
    }
}
