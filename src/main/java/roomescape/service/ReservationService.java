package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
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
        ReservationTime reservationTime = findReservationTime(request.timeId());
        validateNotPast(request.date(), reservationTime);
        Theme theme = findTheme(request.themeId());

        validateDuplicate(request, reservationTime, theme);

        Reservation reservationWithoutId = request.toEntity(reservationTime, theme);
        Reservation reservation = reservationDao.create(reservationWithoutId);

        return ReservationResponseDto.from(reservation);
    }

    private void validateDuplicate(ReservationRequestDto request, ReservationTime reservationTime, Theme theme) {
        boolean isDuplicated = reservationDao.existsBy(
                request.date(),
                reservationTime.getId(),
                theme.getId()
        );
        if (isDuplicated) {
            throw new CustomException(ErrorCode.RESERVATION_DUPLICATED);
        }
    }

    private void validateNotPast(LocalDate date, ReservationTime time) {
        if (date.isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.RESERVATION_DATE_PASSED);
        }
        if (date.isEqual(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            throw new CustomException(ErrorCode.RESERVATION_TIME_PASSED);
        }
    }

    private ReservationTime findReservationTime(Long id) {
        return reservationTimeDao.read(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_TIME_NOT_FOUND));
    }

    private Theme findTheme(Long id) {
        return themeDao.read(id)
                .orElseThrow(() -> new CustomException(ErrorCode.THEME_NOT_FOUND));
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
