package roomescape.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;
import roomescape.service.dto.ServiceReservationRequest;
import roomescape.service.dto.ServiceReservationResponse;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    @Transactional
    public ServiceReservationResponse create(ServiceReservationRequest requestDto) {
        Optional<ReservationTime> reservationTime = reservationTimeDao.read(requestDto.timeId());
        if (reservationTime.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_RESERVATION_TIME);
        }

        Optional<Theme> theme = themeDao.read(requestDto.themeId());
        if (theme.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_THEME);
        }

        boolean existReservation = reservationDao.existByDateAndTimeIdAndThemeId(requestDto.date(), requestDto.timeId(),
                requestDto.themeId());
        if (existReservation) {
            throw new CustomException(ErrorCode.DUPLICATED_RESERVATION);
        }

        Reservation reservationWithoutId = requestDto.toEntity(reservationTime.get(), theme.get());
        Reservation reservation = reservationDao.create(reservationWithoutId);

        return ServiceReservationResponse.from(reservation);
    }

    public List<ServiceReservationResponse> readAll() {
        List<Reservation> reservations = reservationDao.readAll();
        return reservations.stream()
                .map(ServiceReservationResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        reservationDao.delete(id);
    }
}
