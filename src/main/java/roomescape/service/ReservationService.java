package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.ResourceNotExistException;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(
        ReservationDao reservationDao,
        ReservationTimeDao reservationTimeDao,
        ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll()
            .stream()
            .map(ReservationResponse::from)
            .toList();
    }

    public void deleteReservation(Long id) {
        int deleteCount = reservationDao.deleteById(id);
        if(deleteCount == 0) {
            throw new ResourceNotExistException();
        }
    }

    public ReservationResponse save(ReservationRequest request) {
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId())
            .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당하는 시간이 없습니다"));
        Theme theme = themeDao.findById(request.themeId())
            .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당하는 테마가 없습니다"));
        validateSaveReservation(request, reservationTime);
        Reservation reservation = new Reservation(
            request.name(),
            request.date(),
            reservationTime,
            theme
        );
        return getReservationResponse(reservation);
    }

    private void validateSaveReservation(
        ReservationRequest request,
        ReservationTime reservationTime
    ) {
        validateIsDuplicate(request);
        validateNotPast(request.date(), reservationTime);
    }

    private void validateIsDuplicate(ReservationRequest request) {
        if (reservationDao.existsByTimeIdAndThemeIdAndDate(request.timeId(), request.themeId(), request.date())) {
            throw new IllegalArgumentException("[ERROR] 해당 날짜와 시간에 대한 예약이 이미 존재합니다.");
        }
    }

    private void validateNotPast(LocalDate date, ReservationTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());
        if(dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("[ERROR] 현재보다 과거 시간에는 예약이 불가능합니다.");
        }
    }

    private ReservationResponse getReservationResponse(Reservation reservation) {
        try {
            return ReservationResponse.from(reservationDao.save(reservation));
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("[ERROR] 해당 날짜와 시간에 대한 예약이 이미 존재합니다.");
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 예약 생성에 실패하였습니다");
        }
    }
}
