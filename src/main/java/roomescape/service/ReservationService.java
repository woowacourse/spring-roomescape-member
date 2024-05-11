package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDAO;
import roomescape.dao.ReservationTimeDAO;
import roomescape.dao.ThemeDAO;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.AdminReservationRequest;
import roomescape.dto.ReservationRequest;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationDAO reservationDAO;
    private final ReservationTimeDAO reservationTimeDAO;
    private final ThemeDAO themeDAO;
    private final MemberDao memberDao;

    public ReservationService(ReservationDAO reservationDAO, ReservationTimeDAO reservationTimeDAO, ThemeDAO themeDAO, MemberDao memberDao) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
        this.themeDAO = themeDAO;
        this.memberDao = memberDao;
    }

    public Reservation save(Member member, ReservationRequest reservationRequest) {
        validateReservation(reservationRequest);
        ReservationTime reservationTime = reservationTimeDAO.findById(reservationRequest.timeId());
        Theme theme = themeDAO.findById(reservationRequest.themeId());

        Reservation reservation = reservationRequest.toEntity(member, reservationTime, theme);
        return reservationDAO.insert(reservation);
    }

    public Reservation save(Member member, AdminReservationRequest adminReservationRequest) {
        LocalDate date = adminReservationRequest.getDate();
        Long themeId = adminReservationRequest.getThemeId();
        Long timeId = adminReservationRequest.getTimeId();
        Long memberId = adminReservationRequest.getMemberId();

        Member requestMember = memberDao.findMemberById(memberId);
        Theme requestTheme = themeDAO.findById(themeId);
        ReservationTime requestTime = reservationTimeDAO.findById(timeId);

        Reservation reservation = new Reservation(requestMember, date, requestTime, requestTheme);
        return reservationDAO.insert(reservation);
    }

    private void validateReservation(ReservationRequest reservationRequest) {
        ReservationTime requestTime = findRequestTime(reservationRequest);
        LocalDate requestDate = reservationRequest.date();
        List<Reservation> reservations = reservationDAO.selectAll();

        requestTime.validateNotPast(requestDate);
        validateNotDuplicated(reservations, requestDate, requestTime);
    }

    private ReservationTime findRequestTime(ReservationRequest reservationRequest) {
        Long timeId = reservationRequest.timeId();
        return reservationTimeDAO.findById(timeId);
    }

    private void validateNotDuplicated(List<Reservation> reservations, LocalDate requestDate, ReservationTime requestTime) {
        reservations.forEach(reservation -> reservation.validateDifferentDateTime(requestDate, requestTime));
    }

    public List<Reservation> findAll() {
        return reservationDAO.selectAll();
    }

    public void delete(long id) {
        reservationDAO.deleteById(id);
    }
}
