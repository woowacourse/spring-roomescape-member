package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationTimeAvailabilityResponseDto;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;

@Service
public class ReservationTimeService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationTimeService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao,
                                  ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public ReservationTimeResponseDto create(ReservationTimeRequestDto requestDto) {
        ReservationTime reservationTime = reservationTimeDao.create(requestDto.toEntity());
        return ReservationTimeResponseDto.from(reservationTime);
    }

    public List<ReservationTimeResponseDto> readAll() {
        List<ReservationTime> reservationTimes = reservationTimeDao.readAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponseDto::from)
                .toList();
    }

    public List<ReservationTimeAvailabilityResponseDto> readAvailabilityByDateAndTheme(
            LocalDate date, Long themeId) {
        Theme theme = findTheme(themeId);

        List<ReservationTime> allReservationTimes = reservationTimeDao.readAll();
        List<Long> bookedTimeIdByDateAndTheme = reservationTimeDao.bookedTimeIdByDateAndTheme(date, theme.getId());

        return allReservationTimes.stream()
                .map(reservationTime -> {
                    if (bookedTimeIdByDateAndTheme.contains(reservationTime.getId())) {
                        return ReservationTimeAvailabilityResponseDto.from(reservationTime, false);
                    }
                    return ReservationTimeAvailabilityResponseDto.from(reservationTime, true);
                }).toList();
    }

    private Theme findTheme(Long id) {
        return themeDao.read(id)
                .orElseThrow(() -> new CustomException(ErrorCode.THEME_NOT_FOUND));
    }

    public void delete(Long id) {
        validateTimeNotUse(id);

        reservationTimeDao.delete(id);
    }

    private void validateTimeNotUse(Long id) {
        if (reservationDao.existsByTimeId(id)) {
            throw new CustomException(ErrorCode.RESERVATION_TIME_IN_USE);
        }
    }
}
