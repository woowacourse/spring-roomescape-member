package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.member.LoginMember;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.request.LoginMemberRequest;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.exceptions.DuplicationException;
import roomescape.exceptions.ValidationException;
import roomescape.repository.reservation.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;
    private final MemberService memberService;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeService reservationTimeService,
            ThemeService themeService,
            MemberService memberService
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
        this.memberService = memberService;
    }

    public ReservationResponse addReservation(
            ReservationRequest reservationRequest,
            LoginMemberRequest loginMemberRequest
    ) {
        ReservationTimeResponse timeResponse = reservationTimeService.getTime(reservationRequest.timeId());
        ThemeResponse themeResponse = themeService.getTheme(reservationRequest.themeId());

        Reservation reservation = new Reservation(
                reservationRequest.date(),
                timeResponse.toReservationTime(),
                themeResponse.toTheme(),
                loginMemberRequest.toLoginMember()
        );
        validateIsBeforeNow(reservation);
        validateIsDuplicated(reservation);

        return new ReservationResponse(reservationRepository.save(reservation));
    }

    public ReservationResponse addReservation(AdminReservationRequest adminReservationRequest) {
        LoginMember loginMember = memberService.getLoginMemberById(adminReservationRequest.memberId());
        ReservationTimeResponse timeResponse = reservationTimeService.getTime(adminReservationRequest.timeId());
        ThemeResponse themeResponse = themeService.getTheme(adminReservationRequest.themeId());

        Reservation reservation = new Reservation(
                adminReservationRequest.date(),
                timeResponse.toReservationTime(),
                themeResponse.toTheme(),
                loginMember
        );
        validateIsBeforeNow(reservation);
        validateIsDuplicated(reservation);

        return new ReservationResponse(reservationRepository.save(reservation));
    }

    private void validateIsBeforeNow(Reservation reservation) {
        if (reservation.isBeforeNow()) {
            throw new ValidationException("과거 시간은 예약할 수 없습니다.");
        }
    }

    private void validateIsDuplicated(Reservation reservation) {
        if (reservationRepository.isAlreadyBooked(reservation)) {
            throw new DuplicationException("이미 예약이 존재합니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }

    public List<ReservationResponse> findReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
