package roomescape.service.reservation;

import org.springframework.stereotype.Service;
import roomescape.controller.reservation.ReservationRequest;
import roomescape.controller.reservation.ReservationResponse;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
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

    public ReservationService(final ReservationRepository reservationRepository,
                              final MemberRepository memberRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(this::assignTime)
                .map(this::assignTheme)
                .map(ReservationResponse::from)
                .toList();
    }

    private Reservation assignTime(final Reservation reservation) {
        final ReservationTime time = reservationTimeRepository.findById(reservation.getTime().getId())
                .orElse(reservation.getTime());

        return reservation.assignTime(time);
    }

    private Reservation assignTheme(final Reservation reservation) {
        final Theme theme = themeRepository.findById(reservation.getTheme().getId())
                .orElse(reservation.getTheme());

        return reservation.assignTheme(theme);
    }

    public ReservationResponse addReservation(final ReservationRequest reservationRequest) {
        final ReservationTime time = findTimeOrElseThrow(reservationRequest);
        final Theme theme = findThemeOrElseThrow(reservationRequest);
        final Member member = findMemberOrElseThrow(reservationRequest);
        final Reservation parsedReservation = reservationRequest.toDomain()
                .assignTime(time)
                .assignTheme(theme)
                .assignMember(member);

        validateReservationDuplicated(parsedReservation.getDate(), time.getId(), theme.getId());
        validateDateTimeFuture(parsedReservation.getDate(), parsedReservation.getTime());

        final Reservation savedReservation = reservationRepository.save(parsedReservation);
        return ReservationResponse.from(savedReservation);
    }

    private ReservationTime findTimeOrElseThrow(ReservationRequest reservationRequest) {
        return reservationTimeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new TimeNotFoundException("존재 하지 않는 시간 입니다."));
    }

    private Theme findThemeOrElseThrow(ReservationRequest reservationRequest) {
        return themeRepository.findById(reservationRequest.themeId())
                .orElseThrow(() -> new ThemeNotFoundException("존재 하지 않는 테마 입니다."));
    }

    private Member findMemberOrElseThrow(ReservationRequest reservationRequest) {
        return memberRepository.findById(reservationRequest.memberId())
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

    public int deleteReservation(final Long id) {
        final int deletedCount = reservationRepository.delete(id);
        validateNotFound(deletedCount);

        return deletedCount;
    }

    private void validateNotFound(final int deletedCount) {
        if (deletedCount == 0) {
            throw new ReservationNotFoundException("존재하지 않는 예약 입니다.");
        }
    }
}
