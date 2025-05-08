package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
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
import roomescape.dto.request.CreateReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse saveReservation(CreateReservationRequest request) {
        /*
         * 1. 멤버 ID가 존재하는 지 확인해야 한다
         * 2. 예약 시간 ID가 존재하는 지 확인해야 한다
         * 3. 방탈출 테마 ID가 존재하는 지 확인해야 한다
         * 4. 과거 시점 검증을 해야한다.
         * 5. 중복 예약이 아닌지 확인해야 한다.
         * 이 복잡한 검증 로직을 어떻게 처리할 것인가?
         */
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new NotFoundMemberException("올바른 멤버가 아닙니다."));

        ReservationTime reservationTime = reservationTimeRepository.read(request.timeId())
                .orElseThrow(() -> new NotFoundReservationTimeException("올바른 예약 시간을 찾을 수 없습니다. 나중에 다시 시도해주세요."));

        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new NotFoundThemeException("올바른 방탈출 테마가 없습니다."));

        if (LocalDateTime.of(request.date(), reservationTime.getStartAt()).isBefore(LocalDateTime.now())) {
            throw new NotAbleReservationException("과거 시점의 예약을 할 수 없습니다.");
        }

        List<ReservationTime> availableTimes = reservationTimeRepository.findAvailableTimesBy(
                request.date(),
                request.themeId());

        if (!availableTimes.contains(reservationTime)) {
            throw new NotAbleReservationException("이미 해당 시간과 테마에 예약이 존재하여 예약할 수 없습니다.");
        }

        Reservation createdReservation = reservationRepository.save(
                new Reservation(
                        null,
                        member,
                        request.date(),
                        reservationTime,
                        theme
                )
        );

        return ReservationResponse.of(
                createdReservation,
                ReservationTimeResponse.from(reservationTime),
                ThemeResponse.from(theme)
        );
    }

    public List<ReservationResponse> readReservations() {
        List<Reservation> reservations = reservationRepository.readAll();
        return reservations.stream().map(ReservationResponse::from).toList();
    }

    public void delete(Long id) {
        reservationRepository.delete(id);
    }
}
