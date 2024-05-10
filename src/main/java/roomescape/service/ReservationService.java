package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationResponse;
import roomescape.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ReservationService {

    private static final int MAX_RESERVATIONS_PER_TIME = 1;

    private final ReservationDao reservationDao;

    public ReservationService(final ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse create(final Reservation reservation) {
        final List<Reservation> reservationsInSameDateTime = reservationDao.findAllByDateAndTimeAndThemeId(
                reservation.getDate(), reservation.getTime(), reservation.getThemeId());
        validateDuplicatedReservation(reservationsInSameDateTime);
        return ReservationResponse.from(reservationDao.save(reservation));
    }

    private void validateDuplicatedReservation(final List<Reservation> reservationsInSameDateTime) {
        if (reservationsInSameDateTime.size() >= MAX_RESERVATIONS_PER_TIME) {
            throw new IllegalArgumentException("해당 시간대에 예약이 모두 찼습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAll() {
        final List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllByThemeAndMemberAndPeriod(final Long themeId, final Long memberId, final LocalDate dateFrom, final LocalDate dateTo) {
        final List<Reservation> reservations = reservationDao.findAllByThemeAndMemberAndPeriod(themeId, memberId, dateFrom, dateTo);
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void delete(final Long id) {
        final boolean isExist = reservationDao.existById(id);
        if (!isExist) {
            throw new NotFoundException("해당 ID의 예약이 없습니다.");
        }
        reservationDao.deleteById(id);
    }
}
