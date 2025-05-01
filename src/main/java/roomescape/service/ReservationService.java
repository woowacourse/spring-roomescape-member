package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    public ReservationResponse save(ReservationRequest request) {
        int count = reservationDao.getCountByTimeIdAndThemeIdAndDate(request.timeId(), request.themeId(), request.date());
        if (count != 0) {
            throw new IllegalArgumentException("[ERROR] 해당 날짜와 시간에 대한 예약이 이미 존재합니다.");
        }

        Optional<ReservationTime> reservationTime = reservationTimeDao.findById(request.timeId());
        Optional<Theme> theme = themeDao.findById(request.themeId());
        if (reservationTime.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 해당하는 시간이 없습니다");
        }
        if (theme.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 해당하는 테마가 없습니다");
        }
        validateDateTime(request.date(), reservationTime.get());
        Reservation reservation = new Reservation(
            request.name(),
            request.date(),
            reservationTime.get(),
            theme.get()
        );
        try {
            return ReservationResponse.from(reservationDao.save(reservation));
            // TODO: 이 예외는 어디서 catch하는게 맞을가? service vs dao
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("[ERROR] 해당 날짜와 시간에 대한 예약이 이미 존재합니다.");
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 예약 생성에 실패하였습니다");
        }
    }

    public boolean deleteReservation(Long id) {
        int deleteCount = reservationDao.deleteById(id);
        return deleteCount != 0;
    }

    private void validateDateTime(LocalDate date, ReservationTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());
        if(dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("[ERROR] 현재보다 과거 시간에는 예약이 불가능합니다.");
        }
    }
}
