package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.admin.ReserveByAdminRequest;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;
import roomescape.reservation.controller.request.ReserveByUserRequest;
import roomescape.reservation.controller.response.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.ReservationTimeService;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;
    private final MemberService memberService;

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        return ReservationResponse.from(reservations);
    }

    public ReservationResponse reserve(ReserveByUserRequest request, Long memberId) {
        Long timeId = request.timeId();
        LocalDate date = request.date();
        if (reservationRepository.existsSameDateTime(date, timeId)) {
            throw new IllegalArgumentException("[ERROR] 이미 예약이 존재합니다.");
        }
        ReservationDate reservationDate = new ReservationDate(date);
        ReservationTime reservationTime = reservationTimeService.getReservationTime(timeId);
        ReservationDateTime reservationDateTime = new ReservationDateTime(reservationDate, reservationTime);

        Theme theme = themeService.getTheme(request.themeId());

        Member reserver = memberService.getMember(memberId);

        Reservation reserved = Reservation.reserve(reserver, reservationDateTime, theme);
        Reservation saved = reservationRepository.save(reserved);

        return ReservationResponse.from(saved);
    }

    public ReservationResponse reserve(ReserveByAdminRequest request) {
        Long timeId = request.timeId();
        LocalDate date = request.date();
        if (reservationRepository.existsSameDateTime(date, timeId)) {
            throw new IllegalArgumentException("[ERROR] 이미 예약이 존재합니다.");
        }
        ReservationDate reservationDate = new ReservationDate(date);
        ReservationTime reservationTime = reservationTimeService.getReservationTime(timeId);
        ReservationDateTime reservationDateTime = new ReservationDateTime(reservationDate, reservationTime);

        Theme theme = themeService.getTheme(request.themeId());

        Member reserver = memberService.getMember(request.memberId());

        Reservation reserved = Reservation.reserve(reserver, reservationDateTime, theme);
        Reservation saved = reservationRepository.save(reserved);

        return ReservationResponse.from(saved);
    }

    public void deleteById(Long id) {
        Reservation reservation = getReservation(id);
        reservationRepository.deleteById(reservation.getId());
    }

    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 예약을 찾을 수 없습니다."));
    }
}
