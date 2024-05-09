package roomescape.service.serviceimpl;

import org.springframework.stereotype.Service;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;
import roomescape.dto.request.MemberRequest;
import roomescape.dto.request.UserReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.SelectableTimeResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;
import roomescape.service.ReservationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
class ReservationServiceImpl implements ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationServiceImpl(
            final ReservationDao reservationDao,
            final ReservationTimeDao reservationTimeDao,
            final ThemeDao themeDao
    ) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    @Override
    public ReservationResponse save(
            final UserReservationRequest userReservationRequest,
            final MemberRequest memberRequest) {
        Reservation reservation = userReservationRequest.toEntity(
                findReservationTimeById(userReservationRequest),
                findThemeById(userReservationRequest),
                memberRequest.toEntity()
        );

        validateCreatedReservation(reservation);
        return new ReservationResponse(reservationDao.save(reservation));
    }

    @Override
    public List<ReservationResponse> findAll() {
        return ReservationResponse.listOf(reservationDao.getAll());
    }

    @Override
    public void delete(final long id) {
        checkReservationExist(id);
        reservationDao.delete(id);
    }

    @Override
    public List<SelectableTimeResponse> findSelectableTime(final LocalDate date, final long themeId) {
        List<Long> usedTimeId = reservationDao.findTimeIdByDateAndThemeId(date, themeId);
        List<ReservationTime> reservationTimes = reservationTimeDao.getAll();

        return SelectableTimeResponse.listOf(reservationTimes, usedTimeId);
    }

    private ReservationTime findReservationTimeById(UserReservationRequest userReservationRequest) {
        return reservationTimeDao.findById(userReservationRequest.timeId())
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 잘못된 예약 가능 시간 번호를 입력하였습니다."));
    }

    private Theme findThemeById(UserReservationRequest userReservationRequest) {
        return themeDao.findById(userReservationRequest.themeId())
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 잘못된 테마 번호를 입력하였습니다."));
    }

    private void validateCreatedReservation(Reservation reservation) {
        if (hasDuplicateReservation(reservation.getDate(), reservation.getTimeId(), reservation.getThemeId())) {
            throw new IllegalArgumentException("[ERROR] 중복된 예약이 존재합니다.");
        }

        if (reservation.isBeforeThan(LocalDateTime.now())) {
            throw new IllegalArgumentException("[ERROR] 이전 날짜의 예약을 생성할 수 없습니다.");
        }
    }

    private boolean hasDuplicateReservation(final LocalDate date, final long timeId, final long themeId) {
        return !reservationDao.findByDateAndTimeIdAndThemeId(date, timeId, themeId).isEmpty();
    }

    private void checkReservationExist(long id) {
        reservationDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 삭제할 예약 데이터가 없습니다."));
    }
}
