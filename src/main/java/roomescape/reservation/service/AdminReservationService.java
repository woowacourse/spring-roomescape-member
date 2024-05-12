package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;
import roomescape.reservation.controller.dto.request.AdminReservationSaveRequest;
import roomescape.reservation.controller.dto.response.AdminReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservation.repository.ReservationTimeDao;
import roomescape.reservation.repository.ThemeDao;

@Service
public class AdminReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final MemberDao memberDao;

    public AdminReservationService(
            final ReservationDao reservationDao,
            final ReservationTimeDao reservationTimeDao,
            final ThemeDao themeDao,
            final MemberDao memberDao
    ) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.memberDao = memberDao;
    }

    public AdminReservationResponse save(final AdminReservationSaveRequest reservationSaveRequest) {
        Member member = findMemberById(reservationSaveRequest);
        ReservationTime reservationTime = findReservationTimeById(reservationSaveRequest);
        Theme theme = findThemeById(reservationSaveRequest);

        Reservation reservation = reservationDao.save(
                reservationSaveRequest.toEntity(member, reservationTime, theme)
        );
        return AdminReservationResponse.from(reservation);
    }

    private ReservationTime findReservationTimeById(final AdminReservationSaveRequest reservationSaveRequest) {
        return reservationTimeDao.findById(reservationSaveRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 예약 가능 시간 번호를 입력하였습니다."));
    }

    private Theme findThemeById(final AdminReservationSaveRequest reservationSaveRequest) {
        return themeDao.findById(reservationSaveRequest.themeId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 테마 번호를 입력하였습니다."));
    }

    private Member findMemberById(final AdminReservationSaveRequest reservationSaveRequest) {
        return memberDao.findById(reservationSaveRequest.memberId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 회원 번호를 입력하였습니다."));
    }

    public List<AdminReservationResponse> getByFilter(
            final Long memberId, final Long themeId,
            final LocalDate dateFrom, final LocalDate dateTo
    ) {
        List<Reservation> reservations = reservationDao
                .findByMemberIdAndThemeIdAndDateFromAndDateTo(memberId, themeId, dateFrom, dateTo);
        return reservations.stream()
                .map(AdminReservationResponse::from)
                .toList();
    }
}
