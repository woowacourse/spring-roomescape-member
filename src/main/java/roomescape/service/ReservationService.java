package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.request.MemberRequest;
import roomescape.dto.request.UserReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.SelectableTimeResponse;
import roomescape.repository.MemberDao;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final MemberDao memberDao;

    public ReservationService(
            final ReservationDao reservationDao,
            final ReservationTimeDao reservationTimeDao,
            final ThemeDao themeDao,
            final MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.memberDao = memberDao;
    }

    public ReservationResponse save(
            final UserReservationRequest userReservationRequest,
            final MemberRequest memberRequest) {
        Reservation reservation = userReservationRequest.toEntity(
                findReservationTimeById(userReservationRequest.timeId()),
                findThemeById(userReservationRequest.themeId()),
                memberRequest.toEntity()
        );

        validateCreatedReservation(reservation);
        return new ReservationResponse(reservationDao.save(reservation));
    }

    // TODO 중복코드 줄일 수 있는지 고려해보기
    public ReservationResponse save(AdminReservationRequest reservationRequest) {
        ReservationTime reservationTime = findReservationTimeById(reservationRequest.timeId());
        Theme theme = findThemeById(reservationRequest.themeId());
        Member member = findMemberById(reservationRequest.memberId());
        Reservation reservation = reservationRequest.toEntity(reservationTime, theme, member);
        validateCreatedReservation(reservation);
        return new ReservationResponse(reservationDao.save(reservation));
    }

    public List<ReservationResponse> findAll() {
        return mapToReservationTimeResponses(reservationDao.getAll());
    }

    public void delete(final long id) {
        checkReservationExist(id);
        reservationDao.delete(id);
    }

    public List<SelectableTimeResponse> findSelectableTime(final LocalDate date, final long themeId) {
        List<Long> usedTimeId = findUsedTimeId(date, themeId);
        List<ReservationTime> reservationTimes = reservationTimeDao.getAll();
        return mapToSelectableTimeResponses(reservationTimes, usedTimeId);
    }

    public List<ReservationResponse> findReservationBy(long themeId, long memberId, LocalDate start, LocalDate end) {
        List<Reservation> filteredReservations = reservationDao.findByThemeIdAndMemberIdInDuration(themeId, memberId, start, end);
        return mapToReservationTimeResponses(filteredReservations);
    }

    private List<Long> findUsedTimeId(LocalDate date, long themeId) {
        return reservationDao.findTimeIdByDateAndThemeId(date, themeId)
                .stream()
                .map(Reservation::getId)
                .toList();
    }

    private Member findMemberById(Long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] " + memberId + "에 해당하는 회원이 존재하지 않습니다."));

    }

    private ReservationTime findReservationTimeById(Long timeId) {
        return reservationTimeDao.findById(timeId)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 잘못된 예약 가능 시간 번호를 입력하였습니다."));
    }

    private Theme findThemeById(Long themeId) {
        return themeDao.findById(themeId)
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

    private List<ReservationResponse> mapToReservationTimeResponses(List<Reservation> reservations) {
        return reservations
                .stream()
                .map(ReservationResponse::new)
                .toList();
    }

    private List<SelectableTimeResponse> mapToSelectableTimeResponses(List<ReservationTime> reservationTimes, List<Long> usedTimeIds) {
        return reservationTimes.stream()
                .map(time -> SelectableTimeResponse.from(time, usedTimeIds))
                .toList();
    }
}
