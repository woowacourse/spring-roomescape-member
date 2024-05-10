package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.RoomThemeDao;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.request.MemberReservationRequest;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.InvalidInputException;
import roomescape.exception.TargetNotExistException;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final MemberDao memberDao;
    private final ReservationTimeDao reservationTimeDao;
    private final RoomThemeDao roomThemeDao;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao,
                              ReservationTimeDao reservationTimeDao, RoomThemeDao roomThemeDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.reservationTimeDao = reservationTimeDao;
        this.roomThemeDao = roomThemeDao;
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll()
                .stream()
                .map(ReservationResponse::fromReservation)
                .toList();
    }

    public List<ReservationResponse> findAllMatching(
            Long themeId, Long memberId,
            LocalDate dateFrom, LocalDate dateTo) {
        return reservationDao.findAllMatching(themeId, memberId, dateFrom, dateTo)
                .stream()
                .map(ReservationResponse::fromReservation)
                .toList();
    }

    public ReservationResponse save(MemberReservationRequest memberReservationRequest,
                                    Member member) {
        Reservation reservation = getValidatedReservation(memberReservationRequest, member);
        Reservation savedReservation = reservationDao.save(reservation);
        return ReservationResponse.fromReservation(savedReservation);
    }

    public ReservationResponse save(AdminReservationRequest adminReservationRequest) {
        Member member = memberDao.findMemberById(adminReservationRequest.memberId());
        Reservation reservation = getValidatedReservation(adminReservationRequest, member);
        Reservation savedReservation = reservationDao.save(reservation);
        return ReservationResponse.fromReservation(savedReservation);
    }

    private Reservation getValidatedReservation(ReservationRequest reservationRequest,
                                                Member member) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequest.timeId());
        validateOutdatedDateTime(reservationRequest.date(), reservationTime.getStartAt());
        RoomTheme roomTheme = roomThemeDao.findById(reservationRequest.themeId());
        validateDuplicatedReservation(
                reservationRequest.date(), reservationTime.getId(), roomTheme.getId());
        return reservationRequest.toReservation(member, reservationTime, roomTheme);
    }

    public void deleteById(Long id) {
        boolean deleted = reservationDao.deleteById(id);
        if (!deleted) {
            throw new TargetNotExistException("삭제할 예약이 존재하지 않습니다.");
        }
    }

    private void validateOutdatedDateTime(LocalDate date, LocalTime time) {
        LocalDateTime now = LocalDateTime.now(Clock.systemDefaultZone());
        if (LocalDateTime.of(date, time).isBefore(now)) {
            throw new InvalidInputException("지난 날짜에는 예약할 수 없습니다.");
        }
    }

    private void validateDuplicatedReservation(LocalDate date, Long timeId, Long themeId) {
        boolean exists = reservationDao.exists(date, timeId, themeId);
        if (exists) {
            throw new InvalidInputException("예약이 이미 존재합니다.");
        }
    }
}
