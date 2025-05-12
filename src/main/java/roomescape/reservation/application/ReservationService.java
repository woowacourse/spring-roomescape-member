package roomescape.reservation.application;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.exception.auth.AuthorizationException;
import roomescape.exception.resource.AlreadyExistException;
import roomescape.exception.resource.ResourceNotFoundException;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.ReservationTimeRepository;
import roomescape.reservation.ui.dto.request.AvailableReservationTimeRequest;
import roomescape.reservation.ui.dto.request.CreateReservationRequest;
import roomescape.reservation.ui.dto.request.ReservationsByfilterRequest;
import roomescape.reservation.ui.dto.response.AvailableReservationTimeResponse;
import roomescape.reservation.ui.dto.response.ReservationResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationResponse create(final CreateReservationRequest request, final Member member) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(request.date(), request.timeId(),
                request.themeId())) {
            throw new AlreadyExistException("해당 날짜와 시간에 이미 예약된 테마입니다.");
        }

        final ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new ResourceNotFoundException("해당 예약 시간 데이터가 존재하지 않습니다. id = " + request.timeId()));
        final Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new ResourceNotFoundException("해당 테마 데이터가 존재하지 않습니다. id = " + request.themeId()));
        final Reservation reservation = new Reservation(request.date(), reservationTime, theme, member);

        final Long id = reservationRepository.save(reservation);
        final Reservation found = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 예약 데이터가 존재하지 않습니다. id = " + id));

        return ReservationResponse.from(found);
    }

    public void deleteIfOwner(final Long reservationId, final Member member) {
        final Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 예약 데이터가 존재하지 않습니다. id = " + reservationId));

        if (member.isAdmin()) {
            reservationRepository.deleteById(reservationId);
            return;
        }

        if (!Objects.equals(reservation.getMember(), member)) {
            throw new AuthorizationException("삭제할 권한이 없습니다.");
        }

        reservationRepository.deleteById(reservationId);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> findAllByFilter(final ReservationsByfilterRequest request) {
        if (request.dateFrom().isAfter(request.dateTo())) {
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 이전이어야 합니다.");
        }

        return reservationRepository.findAllByThemeIdAndMemberIdAndDateRange(
                        request.themeId(), request.memberId(), request.dateFrom(), request.dateTo()
                )
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<AvailableReservationTimeResponse> findAvailableReservationTimes(
            final AvailableReservationTimeRequest request
    ) {
        final List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        final List<LocalTime> bookedTimes = reservationRepository.findReservationsByDateAndThemeId(
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
