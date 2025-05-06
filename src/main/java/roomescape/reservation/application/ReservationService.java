package roomescape.reservation.application;

import java.time.LocalDate;
import org.springframework.stereotype.Service;
import roomescape.member.application.MemberService;
import roomescape.member.application.dto.MemberDto;
import roomescape.reservation.application.dto.ReservationDto;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRegistrationPolicy;
import roomescape.time.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.presentation.dto.request.AdminReservationRequest;
import roomescape.reservation.presentation.dto.request.ReservationRequest;
import roomescape.theme.application.ThemeService;
import roomescape.time.application.TimeService;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeService timeService;
    private final ThemeService themeService;
    private final MemberService memberService;
    private final ReservationRegistrationPolicy reservationRegistrationPolicy;

    public ReservationService(ReservationRepository reservationRepository, TimeService timeService,
                              ThemeService themeService, MemberService memberService,
                              ReservationRegistrationPolicy reservationRegistrationPolicy) {
        this.reservationRepository = reservationRepository;
        this.timeService = timeService;
        this.themeService = themeService;
        this.memberService = memberService;
        this.reservationRegistrationPolicy = reservationRegistrationPolicy;
    }

    public ReservationDto registerReservationForAdmin(AdminReservationRequest request) {
        Long memberId = request.memberId();
        MemberDto memberDto = memberService.getMemberById(memberId);
        return doRegisterReservation(request.date(), request.timeId(), request.themeId(), memberDto.id());
    }

    public ReservationDto registerReservationForUser(ReservationRequest request, Long memberId) {
        return doRegisterReservation(request.date(), request.timeId(), request.themeId(), memberId);
    }

    private ReservationDto doRegisterReservation(LocalDate date, Long timeId, Long themeId, Long memberId) {
        Theme theme = themeService.getThemeById(themeId);
        ReservationTime reservationTime = timeService.getTimeById(timeId);
        Reservation reservation = Reservation.createNew(memberId, theme, date, reservationTime);
        validateCanRegister(date, timeId, themeId, reservation);
        Long id = reservationRepository.save(reservation);

        return ReservationDto.from(Reservation.assignId(id, reservation));
    }

    private void validateCanRegister(LocalDate date, Long timeId, Long themeId, Reservation reservation) {
        boolean existsDuplicatedReservation = reservationRepository.existsDuplicatedReservation(date, timeId, themeId);
        reservationRegistrationPolicy.validate(reservation, existsDuplicatedReservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
