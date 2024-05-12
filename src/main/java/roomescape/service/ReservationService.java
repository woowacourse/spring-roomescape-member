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
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.service.dto.request.ReservationCreateRequest;
import roomescape.service.dto.response.ReservationResponse;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final RoomThemeDao roomThemeDao;
    private final MemberDao memberDao;

    public ReservationService(
            ReservationDao reservationDao,
            ReservationTimeDao reservationTimeDao,
            RoomThemeDao roomThemeDao,
            MemberDao memberDao)
    {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.roomThemeDao = roomThemeDao;
        this.memberDao = memberDao;
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> findBy(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        validateDateCondition(dateFrom, dateTo);

        return reservationDao.findBy(themeId, memberId, dateFrom, dateTo)
                .stream().map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse save(ReservationCreateRequest reservationCreateRequest) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationCreateRequest.timeId())
                .orElseThrow(() -> new NotFoundException("예약시간을 찾을 수 없습니다."));
        validateOutdatedDateTime(
                reservationCreateRequest.date(),
                reservationTime.getStartAt());
        validateDuplicatedDateTime(
                reservationCreateRequest.date(),
                reservationCreateRequest.timeId(),
                reservationCreateRequest.themeId());

        Member member = memberDao.findById(reservationCreateRequest.memberId())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        RoomTheme roomTheme = roomThemeDao.findById(reservationCreateRequest.themeId())
                .orElseThrow(() -> new NotFoundException("테마를 찾을 수 없습니다."));

        Reservation reservation = reservationCreateRequest.toReservation(member, reservationTime, roomTheme);
        Reservation savedReservation = reservationDao.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    public boolean deleteById(Long id) {
        return reservationDao.deleteById(id);
    }

    private void validateDateCondition(LocalDate dateFrom, LocalDate dateTo) {
        if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)) {
            throw new BadRequestException("날짜를 잘못 입력하셨습니다.");
        }
    }

    private void validateOutdatedDateTime(LocalDate date, LocalTime time) {
        LocalDateTime now = LocalDateTime.now(Clock.systemDefaultZone());
        if (LocalDateTime.of(date, time).isBefore(now)) {
            throw new BadRequestException("지나간 날짜와 시간에 대한 예약을 생성할 수 없습니다.");
        }
    }

    private void validateDuplicatedDateTime(LocalDate date, Long timeId, Long themeId) {
        boolean exists = reservationDao.existsByDateTime(date, timeId, themeId);
        if (exists) {
            throw new BadRequestException("중복된 시간과 날짜에 대한 예약을 생성할 수 없습니다.");
        }
    }
}
