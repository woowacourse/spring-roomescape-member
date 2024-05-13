package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.TimeDao;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.TimeProvider.TimeProvider;
import roomescape.domain.exception.IllegalRequestArgumentException;
import roomescape.dto.request.ReservationAdminCreateRequest;
import roomescape.dto.request.ReservationDetailRequest;
import roomescape.dto.response.ReservationResponse;

@Service
public class ReservationService {
    private final TimeProvider timeProvider;
    private final ReservationDao reservationDao;
    private final MemberDao memberDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;

    public ReservationService(TimeProvider timeProvider, ReservationDao reservationDao, MemberDao memberDao, TimeDao timeDao, ThemeDao themeDao) {
        this.timeProvider = timeProvider;
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponse> readReservations() {
        return reservationDao.readReservations()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> readReservations(ReservationDetailRequest request) {
        return reservationDao.readReservationsByDetails(request)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse createReservation(ReservationAdminCreateRequest dto) {
        Reservation reservation = createReservationFromDto(dto);

        Reservation createdReservation = reservationDao.createReservation(reservation);
        return ReservationResponse.from(createdReservation);
    }

    private Reservation createReservationFromDto(ReservationAdminCreateRequest dto) {
        ReservationTime time = timeDao.readTimeById(dto.timeId())
                .orElseThrow(() -> new IllegalRequestArgumentException("해당 예약 시간이 존재하지 않습니다."));
        Theme theme = themeDao.readThemeById(dto.themeId())
                .orElseThrow(() -> new IllegalRequestArgumentException("해당 테마가 존재하지 않습니다."));
        Member member = memberDao.readMemberById(dto.memberId())
                .orElseThrow(() -> new IllegalRequestArgumentException("해당 사용자는 존재하지 않습니다."));

        Reservation reservation = dto.createReservation(member, time, theme);
        validateAvailableReservation(reservation);
        return reservation;
    }

    private void validateAvailableReservation(Reservation reservation) {
        if (reservation.isBefore(timeProvider.getCurrentDateTime())) {
            throw new IllegalRequestArgumentException("예약은 현재 시간 이후여야 합니다.");
        }

        if (reservationDao.existsReservationByDateAndTimeIdAndThemeId(
                reservation.date(), reservation.getTimeId(), reservation.getThemeId())) {
            throw new IllegalRequestArgumentException("해당 시간대 해당 테마 예약은 이미 존재합니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteReservation(id);
    }
}
