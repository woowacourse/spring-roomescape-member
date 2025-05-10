package roomescape.application.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.AdminReservationCreateRequest;
import roomescape.application.dto.ReservationRequest;
import roomescape.application.dto.ReservationResponse;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Service
public class ReservationService {

    private static final String ERROR_DUPLICATE_RESERVATION = "중복된 예약은 생성이 불가능합니다.";

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao,
                              ThemeDao themeDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.memberDao = memberDao;
    }

    public List<ReservationResponse> findAllReservations() {
        return reservationDao.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public List<ReservationResponse> findFilter(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        List<Reservation> filteredReservations = reservationDao.findFilterByThemeIdOrMemberIdOrDate(
                themeId,
                memberId,
                dateFrom,
                dateTo
        );

        return filteredReservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public ReservationResponse createReservation(Member member, ReservationRequest request) {
        Reservation reservationWithoutId = toReservation(member, request);
        validateForCreation(reservationWithoutId);

        Reservation savedReservation = saveReservation(reservationWithoutId);
        return new ReservationResponse(savedReservation);
    }

    public ReservationResponse createReservation(AdminReservationCreateRequest request) {
        Member member = memberDao.findById(request.memberId())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 사용자가 없습니다."));

        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId());
        Theme theme = themeDao.findById(request.themeId());

        Reservation reservationWithoutId = request.toReservationWith(member, reservationTime, theme);
        validateForCreation(reservationWithoutId);

        Reservation savedReservation = saveReservation(reservationWithoutId);
        return new ReservationResponse(savedReservation);
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }

    private Reservation toReservation(Member member, ReservationRequest request) {
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId());
        Theme theme = themeDao.findById(request.themeId());
        return request.toReservationWith(member, reservationTime, theme);
    }

    private void validateForCreation(Reservation reservationWithoutId) {
        reservationWithoutId.validatePastDateTime();
        if (reservationDao.existsByDateAndTimeId(reservationWithoutId.getDate(),
                reservationWithoutId.getTheme().getId())) {
            throw new IllegalArgumentException(ERROR_DUPLICATE_RESERVATION);
        }
    }

    private Reservation saveReservation(Reservation reservationWithoutId) {
        Long reservationId = reservationDao.save(reservationWithoutId);
        return reservationWithoutId.copyWithId(reservationId);
    }
}
