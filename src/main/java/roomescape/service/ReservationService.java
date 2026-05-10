package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;

@Service

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
    public ReservationResponseDto create(ReservationRequestDto request) {
        ReservationTime reservationTime = reservationTimeDao.read(request.timeId());
        Theme theme = themeDao.read(request.themeId());

        boolean existReservation = reservationDao.existByDateAndTimeIdAndThemeId(request.date(),
                reservationTime.getId(),
                theme.getId());
        if (existReservation) {
            throw new CustomException(ErrorCode.DUPLICATED_RESERVATION);
        }

        Reservation reservationWithoutId = request.toEntity(reservationTime, theme);
        Reservation reservation = reservationDao.create(reservationWithoutId, reservationTime, theme);

        return ReservationResponseDto.from(reservation);
    }

    public List<ReservationResponseDto> readAll() {
        List<Reservation> reservations = reservationDao.readAll();
        return reservations.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    public void delete(Long id) {
        reservationDao.delete(id);
    }
}
