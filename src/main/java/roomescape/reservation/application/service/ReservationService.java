package roomescape.reservation.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import roomescape.admin.presentation.dto.AdminReservationRequest;
import roomescape.auth.application.exception.InvalidMemberException;
import roomescape.member.application.repository.MemberRepository;
import roomescape.member.domain.Member;
import roomescape.reservation.application.dto.CreateReservationRequest;
import roomescape.reservation.application.exception.DuplicateReservationException;
import roomescape.reservation.application.exception.GetThemeException;
import roomescape.reservation.application.exception.GetTimeException;
import roomescape.reservation.application.exception.PastTimeException;
import roomescape.reservation.application.repository.ReservationRepository;
import roomescape.reservation.application.repository.ReservationTimeRepository;
import roomescape.reservation.application.repository.ThemeRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.presentation.dto.MemberReservationRequest;
import roomescape.reservation.presentation.dto.ReservationResponse;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              final ThemeRepository themeRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse createReservation(final Member member,
                                                 final MemberReservationRequest memberReservationRequest) {
        CreateReservationRequest request = mapToCreateReservationRequest(
                member,
                memberReservationRequest.date(),
                memberReservationRequest.themeId(),
                memberReservationRequest.timeId());
        return new ReservationResponse(reservationRepository.insert(request));
    }

    public ReservationResponse createReservation(final AdminReservationRequest adminReservationRequest) {
        Member member = memberRepository.findById(adminReservationRequest.memberId())
                .orElseThrow(() -> new InvalidMemberException("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND));

        CreateReservationRequest request = mapToCreateReservationRequest(
                member,
                adminReservationRequest.date(),
                adminReservationRequest.themeId(),
                adminReservationRequest.timeId());
        return new ReservationResponse(reservationRepository.insert(request));
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAllReservations().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public void deleteReservation(final Long id) {
        reservationRepository.delete(id);
    }

    private CreateReservationRequest mapToCreateReservationRequest(Member member, LocalDate date, Long themeId,
                                                                   Long timeId) {
        ReservationDate reservationDate = new ReservationDate(date);
        ReservationTime reservationTime = getReservationTime(timeId);
        Theme theme = getTheme(themeId);
        validateReservationDateTime(reservationDate, reservationTime);
        return new CreateReservationRequest(
                member,
                theme,
                reservationDate,
                reservationTime
        );
    }

    private ReservationTime getReservationTime(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new GetTimeException("[ERROR] 예약 시간 정보를 찾을 수 없습니다."));
    }

    private Theme getTheme(final Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new GetThemeException("[ERROR] 테마 정보를 찾을 수 없습니다."));
    }

    private void validateReservationDateTime(ReservationDate reservationDate, ReservationTime reservationTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate.getReservationDate(),
                reservationTime.getStartAt());

        validateIsPast(reservationDateTime);
        validateIsDuplicate(reservationDateTime);
    }

    private void validateIsPast(LocalDateTime reservationDateTime) {
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new PastTimeException("[ERROR] 지난 일시에 대한 예약 생성은 불가능합니다.");
        }
    }

    private void validateIsDuplicate(LocalDateTime reservationDateTime) {
        if (reservationRepository.existsByDateTime(reservationDateTime)) {
            throw new DuplicateReservationException("[ERROR] 중복된 일시의 예약은 불가능합니다.");
        }
    }

    public List<ReservationResponse> getFilteredReservations(Long themeId, Long memberId, LocalDate dateFrom,
                                                             LocalDate dateTo) {
        List<Reservation> reservations = reservationRepository.findAllByFilters(themeId, memberId, dateFrom, dateTo);
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
