package roomescape.reservation.application;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.domain.MemberAuthInfo;
import roomescape.exception.auth.AuthorizationException;
import roomescape.exception.resource.AlreadyExistException;
import roomescape.exception.resource.ResourceNotFoundException;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberQueryRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationCommandRepository;
import roomescape.reservation.domain.ReservationQueryRepository;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.ReservationTimeQueryRepository;
import roomescape.reservation.ui.dto.request.AvailableReservationTimeRequest;
import roomescape.reservation.ui.dto.request.CreateReservationRequest;
import roomescape.reservation.ui.dto.request.ReservationsByfilterRequest;
import roomescape.reservation.ui.dto.response.AvailableReservationTimeResponse;
import roomescape.reservation.ui.dto.response.ReservationResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeQueryRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationCommandRepository reservationCommandRepository;
    private final ReservationQueryRepository reservationQueryRepository;
    private final ReservationTimeQueryRepository reservationTimeQueryRepository;
    private final ThemeQueryRepository themeQueryRepository;
    private final MemberQueryRepository memberQueryRepository;

    public ReservationResponse create(final CreateReservationRequest request, final MemberAuthInfo memberAuthInfo) {
        if (reservationQueryRepository.existsByDateAndTimeIdAndThemeId(
                request.date(), request.timeId(), request.themeId())
        ) {
            throw new AlreadyExistException("해당 날짜와 시간에 이미 예약된 테마입니다.");
        }

        final ReservationTime reservationTime = reservationTimeQueryRepository.findById(request.timeId())
                .orElseThrow(() -> new ResourceNotFoundException("해당 예약 시간 데이터가 존재하지 않습니다. id = " + request.timeId()));
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime reservationDateTime = LocalDateTime.of(request.date(), reservationTime.getStartAt());
        if (reservationDateTime.isBefore(now)) {
            throw new IllegalArgumentException("예약 시간은 현재 시간보다 이후여야 합니다.");
        }

        final Theme theme = themeQueryRepository.findById(request.themeId())
                .orElseThrow(() -> new ResourceNotFoundException("해당 테마 데이터가 존재하지 않습니다. id = " + request.themeId()));
        final Member member = memberQueryRepository.findById(memberAuthInfo.id())
                .orElseThrow(() -> new ResourceNotFoundException("해당 회원 데이터가 존재하지 않습니다. id = " + memberAuthInfo.id()));
        final Reservation reservation = new Reservation(request.date(), reservationTime, theme, member);

        final Long id = reservationCommandRepository.save(reservation);
        final Reservation found = reservationQueryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 예약 데이터가 존재하지 않습니다. id = " + id));

        return ReservationResponse.from(found);
    }

    public void deleteIfOwner(final Long reservationId, final MemberAuthInfo memberAuthInfo) {
        final Reservation reservation = reservationQueryRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 예약이 존재하지 않습니다. id = " + reservationId));
        final Member member = memberQueryRepository.findById(memberAuthInfo.id())
                .orElseThrow(() -> new ResourceNotFoundException("해당 회원이 존재하지 않습니다. id = " + memberAuthInfo.id()));

        if (member.isAdmin()) {
            reservationCommandRepository.deleteById(reservationId);
            return;
        }

        if (!Objects.equals(reservation.getMember(), member)) {
            throw new AuthorizationException("삭제할 권한이 없습니다.");
        }

        reservationCommandRepository.deleteById(reservationId);
    }

    public List<ReservationResponse> findAll() {
        return reservationQueryRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> findAllByFilter(final ReservationsByfilterRequest request) {
        if (request.dateFrom().isAfter(request.dateTo())) {
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 이전이어야 합니다.");
        }

        return reservationQueryRepository.findAllByThemeIdAndMemberIdAndDateRange(
                        request.themeId(), request.memberId(), request.dateFrom(), request.dateTo()
                )
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<AvailableReservationTimeResponse> findAvailableReservationTimes(
            final AvailableReservationTimeRequest request
    ) {
        final List<ReservationTime> reservationTimes = reservationTimeQueryRepository.findAll();
        final List<LocalTime> bookedTimes = reservationQueryRepository.findReservationsByDateAndThemeId(
                        request.date(),
                        request.themeId()
                ).stream()
                .map(reservation -> reservation.getTime().getStartAt())
                .toList();

        return reservationTimes.stream()
                .map(reservationTime ->
                        new AvailableReservationTimeResponse(
                                reservationTime.getId(),
                                reservationTime.getStartAt(),
                                bookedTimes.contains(reservationTime.getStartAt())
                        )
                )
                .toList();
    }
}
