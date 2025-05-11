package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.LoginMember;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.UserReservationRequestDto;
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
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;
    private final MemberFinder memberFinder;

    public ReservationService(ReservationRepository reservationRepository, ReservedChecker reservedChecker,
                              ReservationTimeService reservationTimeService, ThemeService themeService,
                              final MemberFinder memberFinder) {
        this.reservationRepository = reservationRepository;
        this.reservedChecker = reservedChecker;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
        this.memberFinder = memberFinder;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.getAllReservations();
    }

    public Reservation addReservation(ReservationRequestDto reservationRequestDto) {
        ReservationTime reservationTime = reservationTimeService.getReservationTimeById(
                reservationRequestDto.timeId());

        ReservationDateTime reservationDateTime = new ReservationDateTime(
                reservationRequestDto.date(), reservationTime);

        validateFutureDateTime(reservationDateTime);

        if (isAlreadyExist(reservationDateTime.getDate(), reservationRequestDto.timeId(),
                reservationRequestDto.themeId())) {
            throw new IllegalArgumentException("Reservation already exists");
        }

        Theme theme = themeService.getThemeById(reservationRequestDto.themeId());
        System.out.println("___________________");
        System.out.println("reservationRequestDto.memberId() = " + reservationRequestDto.memberId());
        System.out.println("___________________");
        System.out.println("___________________");
        System.out.println("___________________");

        Member member = memberFinder.getMemberById(reservationRequestDto.memberId());

        return reservationRepository.addReservation(
                reservationRequestDto.toEntity(null, member, reservationTime, theme));
    }

    public Reservation addReservation(final UserReservationRequestDto userReservationRequestDto,
                                      final LoginMember loginMember) {
        ReservationTime reservationTime = reservationTimeService.getReservationTimeById(
                userReservationRequestDto.timeId());
        ReservationDateTime reservationDateTime = new ReservationDateTime(
                userReservationRequestDto.date(), reservationTime);

        validateFutureDateTime(reservationDateTime);
        if (isAlreadyExist(reservationDateTime.getDate(), userReservationRequestDto.timeId(),
                userReservationRequestDto.themeId())) {
            throw new IllegalArgumentException("Reservation already exists");
        }
        Theme theme = themeService.getThemeById(userReservationRequestDto.themeId());
        Member member = loginMember.toEntity();
        return reservationRepository.addReservation(new Reservation(null, member, reservationDateTime, theme));
    }

    public void deleteReservation(long id) {
        int result = reservationRepository.deleteReservation(id);
        if (result == 0) {
            throw new IllegalArgumentException("삭제할 예약이 존재하지 않습니다. id: " + id);
        }
    }

    private void validateFutureDateTime(ReservationDateTime reservationDateTime) {
        LocalDateTime dateTime = LocalDateTime.of(reservationDateTime.getDate(),
                reservationDateTime.getTime().getStartAt());
        LocalDateTime now = LocalDateTime.now();
        if (dateTime.isBefore(now)) {
            throw new IllegalArgumentException("과거 예약은 불가능합니다.");
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
