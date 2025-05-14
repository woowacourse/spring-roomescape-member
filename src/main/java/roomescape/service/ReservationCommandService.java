package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.request.ReservationPostRequestByAdmin;
import roomescape.dto.request.ReservationPostRequestByUser;
import roomescape.dto.response.ReservationPostResponse;
import roomescape.entity.Member;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.web.LoginMember;

@Service
public class ReservationCommandService {

    private final ReservationDao reservationDao;
    private final MemberDao memberDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationCommandService(ReservationDao reservationDao, MemberDao memberDao,
                                     ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public ReservationPostResponse createReservationOfLoginMember(
            ReservationPostRequestByUser reservationPostRequest,
            LoginMember loginMember
    ) {
        Member member = memberDao.findById(loginMember.id());
        ReservationTime reservationTime = reservationTimeDao.findById(reservationPostRequest.timeId());
        Theme theme = themeDao.findById(reservationPostRequest.themeId());
        Reservation reservationWithoutId = reservationPostRequest.toReservationWith(member, reservationTime,
                theme);
        validateReservationCreation(reservationWithoutId);
        return getReservationPostResponse(reservationWithoutId);
    }

    public ReservationPostResponse createReservationOfRequestMember(
            ReservationPostRequestByAdmin reservationPostRequest
    ) {
        Member member = memberDao.findById(reservationPostRequest.memberId());
        ReservationTime reservationTime = reservationTimeDao.findById(reservationPostRequest.timeId());
        Theme theme = themeDao.findById(reservationPostRequest.themeId());
        Reservation reservationWithoutId = reservationPostRequest.toReservationWith(member, reservationTime, theme);
        validateReservationCreation(reservationWithoutId);
        return getReservationPostResponse(reservationWithoutId);
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }

    private void validateReservationCreation(Reservation reservationWithoutId) {
        reservationWithoutId.validatePastDateTime();
        if (reservationDao.existBySameDateTime(reservationWithoutId)) {
            throw new IllegalArgumentException("중복된 예약은 생성이 불가능합니다.");
        }
    }

    private ReservationPostResponse getReservationPostResponse(
            Reservation reservationWithoutId
    ) {
        Reservation reservation = reservationDao.create(reservationWithoutId);
        return new ReservationPostResponse(reservation);
    }
}
