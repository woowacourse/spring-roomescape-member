package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.dto.ReservationTimeAvailability;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.AvailableReservationTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.code.ReservationTimeErrorCode;
import roomescape.exception.code.ThemeErrorCode;
import roomescape.exception.domain.ReservationTimeException;
import roomescape.exception.domain.ThemeException;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ThemeDao themeDao,
                                  ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    //todo: 이미 동일한 시간이 존재한다면 예외처리 (중복 생성 방지)
    //todo: 예약 가능한 시간 10:00 ~ 22:00 & 1시간 단위
    public ReservationTimeResponse create(ReservationTimeRequest request) {
        ReservationTime reservationTime = request.toReservationTime();
        ReservationTime newReservationTime = reservationTimeDao.save(reservationTime);
        return ReservationTimeResponse.from(newReservationTime);
    }

    //todo: 오늘 이전의 date 예외 처리
    //todo: 관리자페이지에서는 예약 시간 조회를 어떻게 보여줄지 정하고, 정한 내용에 따라 api 추가 및 서비스 메서드 추가 등이 필요할 듯 (관리자는 테마별 시간만 조회하면 될 수도 있으니)
    public List<AvailableReservationTimeResponse> getReservationTimes(long themeId, LocalDate date) {
        validateTheme(themeId);
        List<ReservationTimeAvailability> timeAvailabilities = reservationTimeDao.findAvailabilitiesByThemeIdAndDate(themeId, date);
        return timeAvailabilities.stream()
                .map(AvailableReservationTimeResponse::from)
                .toList();
    }

    private void validateTheme(long themeId) {
        boolean exists = themeDao.existsById(themeId);
        if (!exists) {
            throw new ThemeException(ThemeErrorCode.THEME_NOT_FOUND);
        }
    }

    public void delete(long reservationTimeId) {
        validateReservationNotExistsBy(reservationTimeId);
        int affectedRows = reservationTimeDao.delete(reservationTimeId);

        if (affectedRows == 0) {
            throw new ReservationTimeException(ReservationTimeErrorCode.RESERVATION_TIME_NOT_FOUND);
        }
    }

    private void validateReservationNotExistsBy(long reservationTimeId) {
        if (reservationDao.existsByReservationTime(reservationTimeId)) {
            throw new ReservationTimeException(ReservationTimeErrorCode.RESERVATION_TIME_HAS_RESERVATION);
        }
    }
}
