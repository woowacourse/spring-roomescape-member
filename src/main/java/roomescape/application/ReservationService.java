package roomescape.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.application.dto.ReservationCreateDto;
import roomescape.application.dto.ReservationDto;
import roomescape.application.dto.UserReservationCreateDto;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.dto.ReservationSearchFilter;
import roomescape.exception.NotFoundException;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeService timeService;
    private final ThemeService themeService;
    private final MemberService memberService;

    public ReservationService(
            ReservationRepository reservationRepository,
            TimeService timeService,
            ThemeService themeService,
            MemberService memberService
    ) {
        this.reservationRepository = reservationRepository;
        this.timeService = timeService;
        this.themeService = themeService;
        this.memberService = memberService;
    }

    @Transactional
    public ReservationDto registerReservationByUser(UserReservationCreateDto request, Long memberId) {
        return registerReservation(ReservationCreateDto.of(request, memberId));
    }

    @Transactional
    public ReservationDto registerReservation(ReservationCreateDto request) {
        Theme theme = themeService.getThemeById(request.themeId()).toEntity();
        ReservationTime reservationTime = timeService.getTimeById(request.timeId()).toEntity();
        Member member = memberService.getMemberById(request.memberId()).toEntity();
        Reservation reservation = Reservation.withoutId(member, theme, request.date(), reservationTime);
        validateNotPast(reservation);
        validateNotDuplicate(reservation);
        Long id = reservationRepository.save(reservation);

        return ReservationDto.from(Reservation.assignId(id, reservation));
    }

    private void validateNotDuplicate(Reservation reservation) {
        List<Reservation> allReservations = reservationRepository.findAll();
        boolean duplicated = allReservations.stream()
                .anyMatch(r -> r.isDuplicated(reservation));
        if (duplicated) {
            throw new IllegalArgumentException("이미 예약된 일시입니다");
        }
    }

    private void validateNotPast(Reservation reservation) {
        if (reservation.isPast()) {
            throw new IllegalArgumentException("과거 일시로 예약할 수 없습니다.");
        }
    }

    public List<ReservationDto> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return ReservationDto.from(reservations);
    }

    public List<ReservationDto> searchReservationsWith(ReservationSearchFilter reservationSearchFilter) {
        List<Reservation> reservations = reservationRepository.searchWith(reservationSearchFilter);
        return ReservationDto.from(reservations);
    }

    public void deleteReservation(Long id) {
        boolean deleted = reservationRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("삭제하려는 예약 id가 존재하지 않습니다. id: " + id);
        }
    }
}
