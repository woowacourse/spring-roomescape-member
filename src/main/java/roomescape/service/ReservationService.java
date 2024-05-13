package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.request.UserReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.MemberRepository;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.ThemeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository,
            MemberRepository memberRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse saveByUser(Long memberId, UserReservationRequest userReservationRequest) {
        Reservation savedReservation = save(
                userReservationRequest.date(),
                memberId,
                userReservationRequest.timeId(),
                userReservationRequest.themeId()
        );
        return ReservationResponse.from(savedReservation);
    }

    public ReservationResponse saveByAdmin(AdminReservationRequest adminReservationRequest) {
        Reservation savedReservation = save(
                adminReservationRequest.date(),
                adminReservationRequest.memberId(),
                adminReservationRequest.timeId(),
                adminReservationRequest.themeId());
        return ReservationResponse.from(savedReservation);
    }

    private Reservation save(LocalDate date, Long memberId, Long timeId, Long themeId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("가입되어 있지 않은 유저입니다. memberId: " + memberId));
        ReservationTime requestedReservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("예약할 수 없는 시간입니다. timeId: " + timeId));
        Theme requestedTheme = themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("예약할 수 없는 테마입니다. themeId: " + themeId));
        Reservation requestedReservation = new Reservation(date, member, requestedReservationTime, requestedTheme);

        rejectPastTimeReservation(requestedReservation);
        rejectDuplicateReservation(requestedReservation);

        return reservationRepository.save(requestedReservation);
    }

    private void rejectPastTimeReservation(Reservation reservation) {
        if (reservation.isBeforeNow()) {
            LocalDateTime reservationDataTime = reservation.getDateTime();
            throw new IllegalArgumentException("이미 지난 시간입니다. 입력한 시간: " + reservationDataTime.toLocalDate()
                    + " " + reservationDataTime.toLocalTime());
        }
    }

    private void rejectDuplicateReservation(Reservation reservation) {
        if (reservationRepository.existsByDateTimeAndTheme(reservation)) {
            throw new IllegalArgumentException("중복된 예약이 존재합니다.");
        }
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> searchReservations(
            Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo
    ) {
        List<Reservation> reservations = reservationRepository.searchReservations(
                themeId, memberId, dateFrom, dateTo
        );

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
