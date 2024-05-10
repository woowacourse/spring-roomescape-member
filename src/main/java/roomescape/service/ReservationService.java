package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.dto.reservation.AvailableReservationResponse;
import roomescape.dto.reservation.AdminReservationCreateRequest;
import roomescape.dto.reservation.MemberReservationCreateRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.service.exception.InvalidRequestException;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final MemberDao memberDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final Clock clock;

    public ReservationService(
            ReservationDao reservationDao,
            MemberDao memberDao,
            ReservationTimeDao reservationTimeDao,
            ThemeDao themeDao,
            Clock clock) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.clock = clock;
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationDao.readAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<AvailableReservationResponse> findTimeByDateAndThemeID(String date, Long themeId) {
        ReservationDate reservationDate = ReservationDate.from(date);
        List<ReservationTime> reservationTimes = reservationTimeDao.readAll();
        List<Long> ids = reservationDao.readTimeIdsByDateAndThemeId(reservationDate, themeId);
        return reservationTimes.stream()
                .map(time -> AvailableReservationResponse.of(time, ids.contains(time.getId())))
                .toList();
    }

    public ReservationResponse add(AdminReservationCreateRequest request) {
        Reservation reservation =
                request.toDomain(findMember(request.memberId()), findReservationTime(request.timeId()), findTheme(request.themeId()));
        return add(reservation);
    }

    public ReservationResponse add(MemberReservationCreateRequest request, Member member) {
        Reservation reservation =
                request.toDomain(member, findReservationTime(request.timeId()), findTheme(request.themeId()));
        return add(reservation);
    }

    private ReservationResponse add(Reservation reservation) {
        validateDate(reservation, LocalDate.now(clock));
        validateDuplicate(reservation);
        validatePastTimeWhenToday(reservation, LocalDate.now(clock), LocalTime.now(clock));
        return ReservationResponse.from(reservationDao.create(reservation));
    }

    public void delete(Long id) {
        validateNotExistReservation(id);
        reservationDao.delete(id);
    }

    private Member findMember(Long memberId) {
        return memberDao.readById(memberId)
                .orElseThrow(() -> new InvalidRequestException("존재하지 않는 회원입니다."));
    }

    private ReservationTime findReservationTime(Long timeId) {
        return reservationTimeDao.readById(timeId)
                .orElseThrow(() -> new InvalidRequestException("예약 시간 아이디에 해당하는 예약 시간이 존재하지 않습니다."));
    }

    private Theme findTheme(Long themeId) {
        return themeDao.readById(themeId)
                .orElseThrow(() -> new InvalidRequestException("테마 아이디에 해당하는 테마가 존재하지 않습니다."));
    }

    private void validateDate(Reservation reservation, LocalDate today) {
        if (reservation.isBeforeDate(today)) {
            throw new InvalidRequestException("예약일은 오늘보다 과거일 수 없습니다.");
        }
    }

    private void validateDuplicate(Reservation reservation) {
        if (reservationDao.exist(reservation.getDate(), reservation.getReservationTime(), reservation.getTheme())) {
            throw new InvalidRequestException("중복된 예약을 생성할 수 없습니다.");
        }
    }

    private void validatePastTimeWhenToday(Reservation reservation, LocalDate today, LocalTime now) {
        if (reservation.isSameDate(today) && reservation.isBeforeTime(now)) {
            throw new InvalidRequestException("현재보다 이전 시간을 예약할 수 없습니다.");
        }
    }

    private void validateNotExistReservation(Long id) {
        if (!reservationDao.exist(id)) {
            throw new InvalidRequestException("해당 아이디를 가진 예약이 존재하지 않습니다.");
        }
    }
}
