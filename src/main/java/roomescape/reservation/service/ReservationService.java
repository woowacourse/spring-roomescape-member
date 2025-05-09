package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.CurrentDateTime;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;
import roomescape.reservation.controller.dto.CreateReservationInfo;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservation.repository.ReservationTimeDao;
import roomescape.reservation.repository.ThemeDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final MemberDao memberDao;
    private final CurrentDateTime currentDateTime;

    public ReservationService(final ReservationDao reservationDao, final ReservationTimeDao reservationTimeDao,
                              final ThemeDao themeDao, final MemberDao memberDao,
                              final CurrentDateTime dateTimeGenerator) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.memberDao = memberDao;
        this.currentDateTime = dateTimeGenerator;
    }

    public ReservationResponse createReservation(final CreateReservationInfo request) {
        final Reservation reservation = makeReservation(request);
        if (reservation.isBefore(currentDateTime.getDateTime())) {
            throw new IllegalArgumentException("지나간 날짜와 시간은 예약 불가합니다.");
        }
        if (reservationDao.isExistsByDateAndTimeIdAndThemeId(request.date(), request.timeId(), request.themeId())) {
            throw new IllegalArgumentException("해당 시간에 이미 예약이 존재합니다.");
        }
        final Reservation savedReservation = reservationDao.save(reservation);
        return new ReservationResponse(savedReservation);
    }

    public List<ReservationResponse> getReservations() {
        return reservationDao.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public void cancelReservationById(final long id) {
        reservationDao.deleteById(id);
    }

    private Reservation makeReservation(final CreateReservationInfo request) {
        final Member member = findMember(request.memberId());
        final ReservationTime reservationTime = findReservationTime(request.timeId());
        final Theme theme = findTheme(request.themeId());
        return request.convertToReservation(member, reservationTime, theme);
    }

    private Member findMember(final long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("예약 시간이 존재하지 않습니다."));
    }

    private ReservationTime findReservationTime(final long timeId) {
        return reservationTimeDao.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("예약 시간이 존재하지 않습니다."));
    }

    private Theme findTheme(final long themeId) {
        return themeDao.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("테마가 존재하지 않습니다."));
    }
}
