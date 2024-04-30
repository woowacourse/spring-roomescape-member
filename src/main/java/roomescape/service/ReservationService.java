package roomescape.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.SelectableTimeResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(
            final ReservationDao reservationDao,
            final ReservationTimeDao reservationTimeDao,
            final ThemeDao themeDao
    ) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public ReservationResponse save(final ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 예약 가능 시간 번호를 입력하였습니다."));
        Theme theme = themeDao.findById(reservationRequest.themeId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 테마 번호를 입력하였습니다."));

        if (hasDuplicateReservation(reservationRequest.date(), reservationRequest.timeId())) {
            throw new IllegalArgumentException("[ERROR] 중복된 예약이 존재합니다.");
        }

        Reservation reservation = reservationRequest.toEntity(reservationTime, theme);
        return ReservationResponse.from(reservationDao.save(reservation));
    }

    public List<ReservationResponse> findAll() {
        List<ReservationResponse> reservations = getAll();
        if (reservations.isEmpty()) {
            throw new IllegalStateException("[ERROR] 방탈출 예약 내역이 없습니다.");
        }
        return reservations;
    }

    public List<ReservationResponse> getAll() {
        return reservationDao.getAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteById(final long id) {
        reservationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 삭제할 예약 데이터가 없습니다."));
        reservationDao.delete(id);
    }

    public List<SelectableTimeResponse> findSelectableTime(final LocalDate date, final long themeId) {
        List<SelectableTimeResponse> result = new ArrayList<>();
        List<Long> usedTimeId = reservationDao.findTimeIdByDateAndThemeId(date, themeId);
        List<ReservationTime> reservationTimes = reservationTimeDao.getAll();
        for (ReservationTime reservationTime : reservationTimes) {
            if (usedTimeId.contains(reservationTime.getId())) {
                result.add(new SelectableTimeResponse(reservationTime.getId(), reservationTime.getStartAt(), true));
                continue;
            }
            result.add(new SelectableTimeResponse(reservationTime.getId(), reservationTime.getStartAt(), false));
        }
        return result;
    }

    private boolean hasDuplicateReservation(final LocalDate date, final long timeId) {
        return !reservationDao.findByDateAndTimeId(date, timeId).isEmpty();
    }
}
