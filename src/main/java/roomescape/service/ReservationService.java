package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationResponse;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationDao;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private static final int MAX_RESERVATIONS_PER_TIME = 1;

    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Transactional
    public ReservationResponse create(Reservation reservation) {
        List<Reservation> reservationsInSameDateTime = reservationDao.findAllByDateAndTimeAndThemeId(
                reservation.getDate(), reservation.getTime(), reservation.getThemeId());

        validateDuplicatedReservation(reservationsInSameDateTime);

        Reservation savedReservation = reservationDao.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    private void validateDuplicatedReservation(List<Reservation> reservationsInSameDateTime) {
        if (reservationsInSameDateTime.size() >= MAX_RESERVATIONS_PER_TIME) {
            throw new IllegalArgumentException("해당 시간대에 예약이 모두 찼습니다.");
        }
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        boolean isExist = reservationDao.existById(id);
        if (!isExist) {
            throw new NotFoundException("해당 ID의 예약이 없습니다.");
        }
        reservationDao.deleteById(id);
    }
}
