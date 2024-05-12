package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;
import roomescape.reservation.response.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservation.request.ReservationRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeDao;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeDao;

@Service
public class ReservationService {
    private static final int DELETE_SUCCESS = 1;
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.memberDao = memberDao;
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll();
    }

    public Reservation save(ReservationRequest request) {
        validateDuplicate(request);
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId());
        Theme theme = themeDao.findById(request.themeId());
        Member member = memberDao.findMemberById(request.memberId());
        return reservationDao.save(new Reservation(member, request.date(), reservationTime, theme));
    }

    public Reservation validatePastAndSave(ReservationRequest request, Member member) {
        validateDuplicate(request);
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId());
        new ReservationDateTime(request, reservationTime).validatePast(LocalDateTime.now());
        Theme theme = themeDao.findById(request.themeId());
        return reservationDao.save(new Reservation(member, request.date(), reservationTime, theme));

    }

    private void validateDuplicate(ReservationRequest request) {
        if (reservationDao.isDuplicate(request.date(), request.timeId(), request.themeId())) {
            throw new IllegalArgumentException("Reservation already exists");
        }
    }

    public void delete(long id) {
        if (reservationDao.deleteById(id) != DELETE_SUCCESS) {
            throw new IllegalArgumentException("Cannot delete a reservation by given id");
        }
    }

    public List<ReservationResponse> filter(long themeId, long memberId, String dateFrom, String dateTo) {
        LocalDate from = LocalDate.parse(dateFrom);
        LocalDate to = LocalDate.parse(dateTo);
        return reservationDao.filter(themeId, memberId, from, to);
    }
}
