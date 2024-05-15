package roomescape.service.reservation;

import org.springframework.stereotype.Service;
import roomescape.controller.login.LoginMember;
import roomescape.controller.reservation.CreateReservationRequest;
import roomescape.controller.reservation.ReservationResponse;
import roomescape.controller.reservation.SearchReservationRequest;
import roomescape.domain.*;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.exception.ThemeNotFoundException;
import roomescape.repository.exception.TimeNotFoundException;
import roomescape.service.auth.exception.MemberNotFoundException;
import roomescape.service.reservation.exception.PreviousTimeException;
import roomescape.service.reservation.exception.ReservationDuplicatedException;
import roomescape.service.reservation.exception.ReservationNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              MemberRepository memberRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> getReservations(SearchReservationRequest request) {
        List<Reservation> reservations = reservationRepository.findAllByThemeIdAndMemberIdAndDateRange(
                request.themeId(),
                request.memberId(),
                request.dateFrom(),
                request.dateTo()
        );

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse addReservation(CreateReservationRequest createReservationRequest) {
        Reservation reservation = createReservationRequest.toDomain()
                .assignTime(findTimeOrElseThrow(createReservationRequest.timeId()))
                .assignTheme(findThemeOrElseThrow(createReservationRequest.themeId()))
                .assignMember(findMemberOrElseThrow(createReservationRequest.memberId()));

        validateReservationDuplicated(
                reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getTheme().getId()
        );
        validateDateTimeFuture(reservation.getDate(), reservation.getTime());

        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    private ReservationTime findTimeOrElseThrow(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new TimeNotFoundException("존재 하지 않는 시간 입니다."));
    }

    private Theme findThemeOrElseThrow(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException("존재 하지 않는 테마 입니다."));
    }

    private Member findMemberOrElseThrow(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재 하지 않는 멤버 입니다."));
    }

    private void validateReservationDuplicated(LocalDate date, long timeId, long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new ReservationDuplicatedException("중복된 시간으로 예약이 불가합니다.");
        }
    }

    private void validateDateTimeFuture(LocalDate date, ReservationTime time) {
        if (date.atTime(time.getStartAt()).isBefore(LocalDateTime.now())) {
            throw new PreviousTimeException("지난 시간으로 예약할 수 없습니다.");
        }
    }

    public int deleteReservation(Long reservationsId, LoginMember member) {
        if (Role.ADMIN.name().equals(member.role())) {
            int deletedCount = reservationRepository.delete(reservationsId);
            validateNotFound(deletedCount);

            return deletedCount;
        }
        int deletedCount = reservationRepository.deleteByMemberId(reservationsId, member.id());
        validateNotFound(deletedCount);

        return deletedCount;
    }

    private void validateNotFound(int deletedCount) {
        if (deletedCount == 0) {
            throw new ReservationNotFoundException("존재하지 않는 예약 입니다.");
        }
    }
}
