package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.LoginMember;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.request.ReservationFilterRequest;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.ResourceNotExistException;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final MemberDao memberDao;

    public ReservationService(
        ReservationDao reservationDao,
        ReservationTimeDao reservationTimeDao,
        ThemeDao themeDao, MemberDao memberDao
    ) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.memberDao = memberDao;
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll()
            .stream()
            .map(ReservationResponse::from)
            .toList();
    }

    public List<ReservationResponse> findFiltered(ReservationFilterRequest request) {
        List<Reservation> reservations = reservationDao.findByFilters(request.themeId(),
            request.memberId(), request.dateFrom(), request.dateTo());
        return reservations.stream()
            .map(ReservationResponse::from)
            .toList();
    }

    public void deleteReservation(Long id) {
        int deleteCount = reservationDao.deleteById(id);
        if (deleteCount == 0) {
            throw new ResourceNotExistException();
        }
    }

    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {
        return saveReservation(request.date(), request.timeId(), request.themeId(),
            loginMember.id());
    }

    public ReservationResponse save(AdminReservationRequest request) {
        return saveReservation(request.date(), request.timeId(), request.themeId(),
            request.memberId());
    }

    private ReservationResponse saveReservation(LocalDate date, Long timeId, Long themeId,
        Long memberId) {
        ReservationTime reservationTime = reservationTimeDao.findById(timeId)
            .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당하는 시간이 없습니다"));
        Theme theme = themeDao.findById(themeId)
            .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당하는 테마가 없습니다"));
        Member member = memberDao.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당하는 맴버가 없습니다"));
        Reservation reservation = new Reservation(
            date,
            reservationTime,
            theme,
            member
        );
        validateSaveReservation(reservation);
        return getReservationResponse(reservation);
    }

    private void validateSaveReservation(Reservation reservation) {
        if (reservationDao.existsByTimeIdAndThemeIdAndDate(reservation.getTime().getId(),
            reservation.getTheme().getId(), reservation.getDate())) {
            throw new IllegalArgumentException("[ERROR] 해당 날짜와 시간에 대한 예약이 이미 존재합니다.");
        }
        validateNotPast(reservation.getDate(), reservation.getTime());
    }

    private void validateNotPast(LocalDate date, ReservationTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());
        if (dateTime.isBefore(LocalDateTime.now())) {
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
