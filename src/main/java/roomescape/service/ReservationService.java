package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain_entity.Reservation;
import roomescape.domain_entity.ReservationTime;
import roomescape.domain_entity.Theme;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;

@Component
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponseDto> findAllReservations() {
        return reservationDao.findAll().stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    public ReservationResponseDto createReservation(ReservationRequestDto reservationRequest) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequest.timeId());
        Theme theme = themeDao.findById(reservationRequest.themeId());
        Reservation reservationWithoutId = reservationRequest.toReservationWith(reservationTime, theme);
        reservationWithoutId.validatePastDateTime();

        if (reservationDao.existBySameDateTime(reservationWithoutId)) {
            throw new IllegalArgumentException("중복된 예약은 생성이 불가능합니다.");
        }

        long reservationId = reservationDao.create(reservationWithoutId);

        Reservation reservation = reservationWithoutId.copyWithId(reservationId);
        return ReservationResponseDto.from(reservation);
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }
}
