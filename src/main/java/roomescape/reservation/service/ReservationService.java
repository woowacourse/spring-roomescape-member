package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.custom.BusinessRuleViolationException;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.dto.CreateReservationServiceRequest;
import roomescape.reservationtime.dao.ReservationTimeDao;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;

@Service
public class ReservationService {

    private final ReservationDao reservationDAO;
    private final ReservationTimeDao reservationTimeDAO;
    private final ThemeDao themeDAO;
    private final MemberDao memberDao;

    public ReservationService(final ReservationDao reservationDAO,
                              final ReservationTimeDao reservationTimeDAO,
                              final ThemeDao themeDAO,
                              final MemberDao memberDao) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
        this.themeDAO = themeDAO;
        this.memberDao = memberDao;
    }

    public Reservation addReservation(final CreateReservationServiceRequest creation) {
        final ReservationTime reservationTime = getReservationTimeByTimeId(creation.timeId());
        final Theme theme = getThemeByThemeId(creation.themeId());
        final Member member = getMemberByMemberId(creation.memberId());
        final Reservation reservation = new Reservation(creation.date(), reservationTime, theme, member);

        validatePastDateAndTime(reservation);
        validateDuplicateReservation(reservation);

        final long savedId = reservationDAO.insert(reservation);
        return new Reservation(savedId, reservation);
    }

    private ReservationTime getReservationTimeByTimeId(final long timeId) {
        return reservationTimeDAO.findById(timeId)
                .orElseThrow(() -> new NotExistedValueException("존재하지 않는 예약 가능 시간입니다: timeId=%d".formatted(timeId)));
    }

    private Theme getThemeByThemeId(final long themeId) {
        return themeDAO.findById(themeId)
                .orElseThrow(() -> new NotExistedValueException("존재하지 않는 테마 입니다"));
    }

    private Member getMemberByMemberId(final long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(() -> new NotExistedValueException("존재하지 않는 멤버 입니다"));
    }

    private void validatePastDateAndTime(final Reservation reservation) {
        if (reservation.isPastDateAndTime()) {
            throw new BusinessRuleViolationException("과거 시점은 예약할 수 없습니다");
        }
    }

    private void validateDuplicateReservation(final Reservation reservation) {
        if (existsSameReservation(reservation)) {
            throw new ExistedDuplicateValueException("이미 예약이 존재하는 시간입니다: date=%s, time=%s"
                    .formatted(reservation.getDate(), reservation.getTime().getStartAt()));
        }
    }

    private boolean existsSameReservation(final Reservation reservation) {
        return reservationDAO.existSameReservation(
                reservation.getDate(), reservation.getTime().getId(), reservation.getTheme().getId());
    }

    public List<Reservation> findAllReservations() {
        return reservationDAO.findAll();
    }

    public List<Reservation> findByConditions(final Long memberId, final Long themeId,
                                              final LocalDate from, final LocalDate to) {
        return reservationDAO.findByConditions(memberId, themeId, from, to);
    }

    public void removeReservationById(final long id) {
        boolean deleted = reservationDAO.deleteById(id);

        if (!deleted) {
            throw new NotExistedValueException("존재하지 않는 예약입니다");
        }
    }
}
