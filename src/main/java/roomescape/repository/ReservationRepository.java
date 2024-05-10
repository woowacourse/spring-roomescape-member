package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Member;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.CustomException2;

@Repository
public class ReservationRepository {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final MemberDao memberDao;

    public ReservationRepository(final ReservationDao reservationDao,
                                 final ReservationTimeDao reservationTimeDao,
                                 final ThemeDao themeDao,
                                 final MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.memberDao = memberDao;
    }

    public ReservationTime getReservationTimeById(final Long timeId) {
        return reservationTimeDao.findById(timeId)
                .orElseThrow(() -> new CustomException2("timeId가 존재하지 않습니다."));
    }

    public Theme getThemeById(final Long themeId) {
        return themeDao.findById(themeId)
                .orElseThrow(() -> new CustomException2("themeId가 존재하지 않습니다."));
    }

    public Member getMemberById(final Long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(() -> new CustomException2("memberId가 존재하지 않습니다."));
    }
}
