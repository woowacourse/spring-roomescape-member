package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.SearchReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationThemeRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;
    private final MemberRepository memberRepository;

    public ReservationResponse create(Long memberId, LocalDate date, Long timeId, Long themeId) {
        Member member = memberRepository.getById(memberId);
        ReservationTime reservationTime = reservationTimeRepository.getById(timeId);
        validateReservationTime(date, timeId, themeId, reservationTime);
        ReservationTheme reservationTheme = reservationThemeRepository.getById(themeId);
        Reservation reservation = new Reservation(member, date, reservationTime, reservationTheme);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    private void validateReservationTime(LocalDate date, Long timeId, Long themeId, ReservationTime reservationTime) {
        LocalDateTime requestedDateTime = LocalDateTime.of(date, reservationTime.startAt());
        if (requestedDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이미 지나간 시간으로 예약할 수 없습니다.");
        }
        if (reservationRepository.existDuplicatedDateTime(date, timeId, themeId)) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
    }

    public List<ReservationResponse> getAll() {
        List<Reservation> reservations = reservationRepository.getAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void delete(Long id) {
        reservationRepository.findById(id)
                .ifPresent(reservationRepository::remove);
    }

    public List<ReservationResponse> search(SearchReservationRequest request) {
        ReservationTheme theme = getNullableTheme(request.themeId());
        Member member = getNullableMember(request.memberId());
        List<Reservation> searchResult = reservationRepository.findAllByThemeAndMemberInDateRange(
                theme, member, request.dateFrom(), request.dateTo());
        return searchResult.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private ReservationTheme getNullableTheme(Long themeId) {
        if (themeId == null) {
            return null;
        }
        return reservationThemeRepository.getById(themeId);
    }

    private Member getNullableMember(Long memberId) {
        if (memberId == null) {
            return null;
        }
        return memberRepository.getById(memberId);
    }
}
