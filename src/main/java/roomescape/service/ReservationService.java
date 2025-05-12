package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.AdminCreateReservationRequest;
import roomescape.dto.request.CreateReservationRequest;
import roomescape.exception.InvalidReservationException;
import roomescape.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;
    private final MemberService memberService;

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationTimeService reservationTimeService, ThemeService themeService,
                              MemberService memberService, ReservationRepository reservationRepository) {
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
        this.memberService = memberService;
        this.reservationRepository = reservationRepository;
    }

    public Reservation addReservation(CreateReservationRequest request, LoginMember loginMember) {
        return createReservation(loginMember.getId(), request.themeId(), request.date(), request.timeId());
    }

    public Reservation addReservationByAdmin(AdminCreateReservationRequest request) {
        return createReservation(request.memberId(), request.themeId(), request.date(), request.timeId());
    }

    private Reservation createReservation(long memberId, long themeId, LocalDate date, long timeId) {
        Member member = memberService.getMemberById(memberId);
        ReservationTime reservationTime = reservationTimeService.getReservationTimeById(timeId);
        Theme theme = themeService.getThemeById(themeId);

        Reservation reservation = new Reservation(member, date, reservationTime, theme);

        validateDuplicateReservation(reservation);
        validateAddReservationDateTime(reservation);
        return reservationRepository.add(reservation);
    }

    private void validateDuplicateReservation(Reservation reservation) {
        if (reservationRepository.existsByDateAndTimeIdAndTheme(reservation)) {
            throw new InvalidReservationException("중복된 예약신청입니다");
        }
    }

    private void validateAddReservationDateTime(Reservation reservation) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (reservation.isBefore(currentDateTime)) {
            throw new InvalidReservationException("과거 시간에 예약할 수 없습니다.");
        }
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findAllByFilter(Long memberId, Long themeId, LocalDate dateFrom, LocalDate dateTo) {
        return reservationRepository.findAllByFilter(memberId, themeId, dateFrom, dateTo);
    }

    public Reservation getReservationById(long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new InvalidReservationException("존재하지 않는 예약입니다."));
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId) {
        return reservationRepository.findAllByDateAndThemeId(date, themeId);
    }

    public List<Reservation> findAllByDateInRange(LocalDate start, LocalDate end) {
        return reservationRepository.findAllByDateInRange(start, end);
    }
}
