package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.domain.Password;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.CustomBadRequest;

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

    public Reservation getReservationById(final Long reservationId) {
        return reservationDao.findById(reservationId)
                .orElseThrow(() -> new CustomBadRequest(String.format("reservationId(%s)가 존재하지 않습니다.", reservationId)));
    }

    public ReservationTime getReservationTimeById(final Long timeId) {
        return reservationTimeDao.findById(timeId)
                .orElseThrow(() -> new CustomBadRequest(String.format("timeId(%s)가 존재하지 않습니다.", timeId)));
    }

    public Theme getThemeById(final Long themeId) {
        return themeDao.findById(themeId)
                .orElseThrow(() -> new CustomBadRequest(String.format("themeId(%s)가 존재하지 않습니다.", themeId)));
    }

    public Member getMemberById(final Long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(() -> new CustomBadRequest(String.format("memberId(%s)가 존재하지 않습니다.", memberId)));
    }

    public Member getMember(final Email email, final Password password) {
        return memberDao.find(email, password)
                .orElseThrow(() -> new CustomBadRequest("없는 이메일이거나 잘못된 비밀번호입니다."));
    }

    public void checkReservationNotExists(final ReservationTime time) {
        if (reservationDao.exists(time)) {
            throw new CustomBadRequest(String.format("예약(timeId=%s)이 존재합니다.", time.id()));
        }
    }

    public void checkReservationNotExists(final Theme theme) {
        if (reservationDao.exists(theme)) {
            throw new CustomBadRequest(String.format("예약(themeId=%s)이 존재합니다.", theme.id()));
        }
    }

    public void checkReservationTimeNotExists(final ReservationTime time) {
        if (reservationTimeDao.existsByStartAt(time.getStartAtAsString())) {
            throw new CustomBadRequest(String.format("예약 시간(startAt=%s)이 존재합니다.", time.getStartAtAsString()));
        }
    }
}
