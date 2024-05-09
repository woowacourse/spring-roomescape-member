package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.global.exception.error.ErrorType;
import roomescape.global.exception.model.DataDuplicateException;
import roomescape.global.exception.model.NotFoundException;
import roomescape.global.exception.model.ValidateException;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dao.TimeDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.dto.response.ReservationTimeInfosResponse;
import roomescape.reservation.dto.response.ReservationsResponse;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;
    private final MemberDao memberDao;

    public ReservationService(final ReservationDao reservationDao,
                              final TimeDao timeDao,
                              final ThemeDao themeDao,
                              final MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
        this.memberDao = memberDao;
    }

    public ReservationsResponse findAllReservations() {
        List<ReservationResponse> response = reservationDao.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();

        return new ReservationsResponse(response);
    }

    public ReservationTimeInfosResponse findReservationsByDateAndThemeId(final LocalDate date, final Long themeId) {
        return timeDao.findByDateAndThemeId(date, themeId);
    }

    public void removeReservationById(final Long id) {
        reservationDao.deleteById(id);
    }

    public ReservationResponse addReservation(final ReservationRequest request, final Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate requestDate = request.date();

        ReservationTime requestReservationTime = timeDao.findById(request.timeId());
        Theme theme = themeDao.findById(request.themeId());
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorType.MEMBER_NOT_FOUND,
                        String.format("회원(Member) 정보가 존재하지 않습니다. [values: %s]", request)));

        validateDateAndTime(requestDate, requestReservationTime, now);
        validateReservationDuplicate(request, theme);

        Reservation savedReservation = reservationDao.insert(request.toEntity(requestReservationTime, theme, member));
        return ReservationResponse.from(savedReservation);
    }

    private void validateDateAndTime(final LocalDate requestDate, final ReservationTime requestReservationTime, final LocalDateTime now) {
        if (isReservationInPast(requestDate, requestReservationTime, now)) {
            throw new ValidateException(ErrorType.RESERVATION_PERIOD_IN_PAST,
                    String.format("지난 날짜나 시간은 예약이 불가능합니다. [now: %s %s | request: %s %s]",
                            now.toLocalDate(), now.toLocalTime(), requestDate, requestReservationTime.getStartAt()));
        }
    }

    private boolean isReservationInPast(final LocalDate requestDate, final ReservationTime requestReservationTime, final LocalDateTime now) {
        LocalDate today = now.toLocalDate();
        LocalTime nowTime = now.toLocalTime();

        if (requestDate.isBefore(today)) {
            return true;
        }
        if (requestDate.isEqual(today) && requestReservationTime.getStartAt().isBefore(nowTime)) {
            return true;
        }
        return false;
    }

    private void validateReservationDuplicate(final ReservationRequest reservationRequest, final Theme theme) {
        List<Reservation> duplicateTimeReservations = reservationDao.findByTimeIdAndDateAndThemeId(
                reservationRequest.timeId(), reservationRequest.date(), theme.getId());

        if (duplicateTimeReservations.size() > 0) {
            throw new DataDuplicateException(ErrorType.RESERVATION_DUPLICATED,
                    String.format("이미 해당 날짜/시간/테마에 예약이 존재합니다. [values: %s]", reservationRequest));
        }
    }
}
