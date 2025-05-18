package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.entity.Member;
import roomescape.domain.entity.Reservation;
import roomescape.domain.entity.ReservationTime;
import roomescape.domain.entity.Theme;
import roomescape.domain.repository.MemberRepository;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.repository.ThemeRepository;
import roomescape.dto.LoginInfo;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.request.UserReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.error.NotFoundException;
import roomescape.error.ReservationException;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public List<ReservationResponse> findAllReservation() {
        final List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public ReservationResponse saveReservation(final UserReservationRequest request, final LoginInfo loginInfo) {
        final Member member = memberRepository.findById(loginInfo.id())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
        return save(request.date(), request.timeId(), request.themeId(), member);
    }

    public ReservationResponse saveReservation(final AdminReservationRequest request) {
        final Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
        return save(request.date(), request.timeId(), request.themeId(), member);
    }

    private ReservationResponse save(final LocalDate date, final Long timeId, final Long themeId, final Member member) {
        final ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 시간입니다."));
        final Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));

        if (reservationRepository.existsByDateAndTimeAndTheme(date, reservationTime.getStartAt(), themeId)) {
            throw new ReservationException("해당 시간은 이미 예약되어 있습니다.");
        }

        final Reservation reservation = new Reservation(member, date, reservationTime, theme);
        validateFutureOrPresent(reservation.getDate(), reservation.extractTime());

        final Reservation saved = reservationRepository.save(reservation);
        return new ReservationResponse(saved);
    }

    private void validateFutureOrPresent(final LocalDate date, final LocalTime time) {
        final LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
        final LocalDateTime currentDateTime = LocalDateTime.now();
        if (reservationDateTime.isBefore(currentDateTime)) {
            throw new ReservationException("예약은 현재 시간 이후로 가능합니다.");
        }
    }

    public void deleteReservation(final Long id) {
        final Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));
        reservationRepository.deleteById(reservation.getId());
    }

    public List<ReservationResponse> searchReservations(final Long themeId, final Long memberId, final LocalDate dateFrom,
                                                        final LocalDate dateTo) {
        final List<Reservation> results = reservationRepository.search(themeId, memberId, dateFrom, dateTo);
        return results.stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
