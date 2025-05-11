package roomescape.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.common.exception.NotAbleReservationException;
import roomescape.common.exception.NotFoundMemberException;
import roomescape.common.exception.NotFoundReservationTimeException;
import roomescape.common.exception.NotFoundThemeException;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository,
                              MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse createReservation(ReservationCreateRequest reservationCreateRequest, Long memberId) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationCreateRequest.timeId())
                .orElseThrow(() -> new NotFoundReservationTimeException("올바른 예약 시간을 찾을 수 없습니다. 나중에 다시 시도해주세요."));
        Theme theme = themeRepository.findById(reservationCreateRequest.themeId())
                .orElseThrow(() -> new NotFoundThemeException("올바른 방탈출 테마가 없습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("일치하는 예약자가 존재하지 않습니다."));
        if (LocalDateTime.of(reservationCreateRequest.date(), reservationTime.getStartAt()).isBefore(LocalDateTime.now())) {
            throw new NotAbleReservationException("과거 시점의 예약을 할 수 없습니다.");
        }

        List<ReservationTime> availableTimes = reservationTimeRepository.findByReservationDateAndThemeId(reservationCreateRequest.date(),
                reservationCreateRequest.themeId());

        if (!availableTimes.contains(reservationTime)) {
            throw new NotAbleReservationException("이미 해당 시간과 테마에 예약이 존재하여 예약할 수 없습니다.");
        }

        Reservation reservation = reservationRepository.save(reservationCreateRequest.toReservation(reservationTime, theme, member));

        return ReservationResponse.from(reservation);
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream().map(ReservationResponse::from).toList();
    }

    public List<ReservationResponse> findByCondition(Long themeId, Long memberId, String dateFrom, String dateTo) {
        List<Reservation> reservations = reservationRepository.findByMemberAndThemeAndVisitDateBetween(themeId, memberId, dateFrom, dateTo);
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteReservationById(Long id) {
        reservationRepository.deleteById(id);
    }
}
