package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.auth.entity.Member;
import roomescape.auth.repository.MemberRepository;
import roomescape.global.exception.badRequest.BadRequestException;
import roomescape.global.exception.conflict.ReservationConflictException;
import roomescape.global.exception.notFound.MemberNotFoundException;
import roomescape.global.exception.notFound.ReservationNotFoundException;
import roomescape.global.exception.notFound.ReservationTimeNotFoundException;
import roomescape.global.exception.notFound.ThemeNotFoundException;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.dto.ReservationWithFilterRequest;
import roomescape.reservation.service.dto.request.CreateReservationRequest;
import roomescape.reservation.service.dto.response.ReservationResponse;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.entity.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository timeRepository,
            ThemeRepository themeRepository,
            MemberRepository memberRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public List<ReservationResponse> getAllReservation(ReservationWithFilterRequest request) {
        return reservationRepository.findAllByFilter(request)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private ReservationResponse convertToResponse(Reservation reservation) {
        final Long themeId = reservation.getThemeId();
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException(themeId));
        final Long memberId = reservation.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        return ReservationResponse.of(reservation, theme, member);
    }

    public ReservationResponse createReservation(CreateReservationRequest request) {
        ReservationTime timeEntity = timeRepository.findById(request.timeId())
                .orElseThrow(() -> new ReservationTimeNotFoundException(request.timeId()));
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new ThemeNotFoundException(request.themeId()));

        Reservation newReservation = request.toEntity(timeEntity);
        validateReservationDate(newReservation);
        validateDuplicated(newReservation);

        Reservation saved = reservationRepository.save(newReservation);
        return ReservationResponse.of(saved, theme, request.loginMember());
    }

    private void validateDuplicated(Reservation newReservation) {
        if (isExistDuplicatedWith(newReservation)) {
            throw new ReservationConflictException();
        }
    }

    private boolean isExistDuplicatedWith(Reservation target) {
        return reservationRepository.findDuplicatedWith(target).isPresent();
    }

    private void validateReservationDate(Reservation newReservation) {
        LocalDateTime now = LocalDateTime.now();
        if (newReservation.isBefore(now)) {
            throw new BadRequestException("과거 날짜/시간의 예약은 생성할 수 없습니다.");
        }
    }

    public void deleteReservation(final Long id) {
        final boolean deleted = reservationRepository.deleteById(id);
        if (!deleted) {
            throw new ReservationNotFoundException(id);
        }
    }
}
