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
import roomescape.dto.request.FilteredReservationRequest;
import roomescape.dto.request.MemberRequest;
import roomescape.dto.request.MemberReservationRequest;
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
            FilteredReservationRequest filteredReservationRequest) {
        return reservationDao.findAllMatching(
                        filteredReservationRequest.themeId(),
                        filteredReservationRequest.memberId(),
                        filteredReservationRequest.dateFrom(),
                        filteredReservationRequest.dateTo())
                .stream()
                .map(ReservationResponse::fromReservation)
                .toList();
    }

    public ReservationResponse save(MemberReservationRequest memberReservationRequest,
                                    MemberRequest memberRequest) {
        Reservation reservation = getValidatedReservation(
                memberReservationRequest.date(),
                memberReservationRequest.timeId(),
                memberReservationRequest.themeId(),
                memberRequest.id());
        return savedReservationResponse(reservation);
    }

    public ReservationResponse save(AdminReservationRequest adminReservationRequest) {
        Reservation reservation = getValidatedReservation(
                adminReservationRequest.date(),
                adminReservationRequest.timeId(),
                adminReservationRequest.themeId(),
                adminReservationRequest.memberId());
        return savedReservationResponse(reservation);
    }

    private Reservation getValidatedReservation(LocalDate date, Long timeId,
                                                Long themeId, Long memberId) {
        validateDuplicatedReservation(date, timeId, themeId);
        ReservationTime reservationTime = getTimeById(timeId);
        validateOutdatedDateTime(date, reservationTime.getStartAt());
        RoomTheme roomTheme = getThemeById(themeId);
        Member member = getMemberById(memberId);
        return new Reservation(date, member, reservationTime, roomTheme);
    }

    private ReservationResponse savedReservationResponse(Reservation reservation) {
        Reservation savedReservation = reservationDao.save(reservation);
        return ReservationResponse.fromReservation(savedReservation);
    }

    public void deleteById(Long id) {
        boolean deleted = reservationDao.deleteById(id);
        if (!deleted) {
            throw new TargetNotExistException("삭제할 예약이 존재하지 않습니다.");
        }
    }

    private ReservationTime getTimeById(Long timeId) {
        return reservationTimeDao.findById(timeId)
                .orElseThrow(() -> new InvalidInputException("해당 예약 시간이 존재하지 않습니다."));
    }

    private RoomTheme getThemeById(Long themeId) {
        return roomThemeDao.findById(themeId)
                .orElseThrow(() -> new InvalidInputException("해당 테마가 존재하지 않습니다."));
    }

    private Member getMemberById(Long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(() -> new InvalidInputException("해당 계정이 존재하지 않습니다."));
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
