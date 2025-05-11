package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.request.LoginMember;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.request.UserReservationRequest;
import roomescape.model.Member;
import roomescape.model.Reservation;
import roomescape.model.ReservationDateTime;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservedChecker;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservedChecker reservedChecker;
    private final ReservationTimeFinder reservationTimeFinder;
    private final ThemeFinder themeFinder;
    private final MemberFinder memberFinder;

    public ReservationService(final ReservationRepository reservationRepository, final ReservedChecker reservedChecker,
                              final ReservationTimeFinder reservationTimeFinder, final ThemeFinder themeFinder,
                              final MemberFinder memberFinder) {
        this.reservationRepository = reservationRepository;
        this.reservedChecker = reservedChecker;
        this.reservationTimeFinder = reservationTimeFinder;
        this.themeFinder = themeFinder;
        this.memberFinder = memberFinder;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.getAllReservations();
    }

    public Reservation addReservationByAdmin(ReservationRequest reservationRequest) {
        ReservationDateTime reservationDateTime = validateAndGetDateTime(reservationRequest.date(),
                reservationRequest.timeId(), reservationRequest.themeId());
        Theme theme = themeFinder.getThemeById(reservationRequest.themeId());
        Member member = memberFinder.getMemberById(reservationRequest.memberId());
        return reservationRepository.addReservation(Reservation.create(null, member, reservationDateTime, theme));
    }

    public Reservation addReservationByUser(final UserReservationRequest userReservationRequest,
                                            final LoginMember loginMember) {

        ReservationDateTime reservationDateTime = validateAndGetDateTime(userReservationRequest.date(),
                userReservationRequest.timeId(), userReservationRequest.themeId());
        Theme theme = themeFinder.getThemeById(userReservationRequest.themeId());
        Member member = loginMember.toEntity();
        return reservationRepository.addReservation(Reservation.create(null, member, reservationDateTime, theme));
    }

    private ReservationDateTime validateAndGetDateTime(LocalDate date,
                                                       Long timeId,
                                                       Long themeId) {
        ReservationTime reservationTime = reservationTimeFinder.getReservationTimeById(timeId);
        ReservationDateTime reservationDateTime = new ReservationDateTime(date, reservationTime);
        if (isAlreadyExist(reservationDateTime.getDate(), timeId, themeId)) {
            throw new IllegalArgumentException("Reservation already exists");
        }
        return reservationDateTime;
    }


    public void deleteReservation(long id) {
        int result = reservationRepository.deleteReservation(id);
        if (result == 0) {
            throw new IllegalArgumentException("삭제할 예약이 존재하지 않습니다. id: " + id);
        }
    }

    private boolean isAlreadyExist(LocalDate reservationDate, Long timeId, Long themeId) {
        return reservedChecker.contains(reservationDate, timeId, themeId);
    }


    public List<Reservation> getFilteredReservations(final Long memberId, final Long themeId, final LocalDate fromDate,
                                                     final LocalDate toDate) {
        return reservationRepository.findBy(memberId, themeId, fromDate, toDate);
    }
}
