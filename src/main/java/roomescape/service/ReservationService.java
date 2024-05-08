package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.request.AdminReservationRequest;
import roomescape.controller.response.ReservationResponse;
import roomescape.controller.response.UserResponse;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
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

    public ReservationResponse save(AdminReservationRequest adminReservationRequest) {
        ReservationTime requestedReservationTime = reservationTimeRepository.findById(adminReservationRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("예약할 수 없는 시간입니다. timeId: " + adminReservationRequest.timeId()));
        Theme requestedTheme = themeRepository.findById(adminReservationRequest.themeId())
                .orElseThrow(() -> new IllegalArgumentException("예약할 수 없는 테마입니다. themeId: " + adminReservationRequest.themeId()));
        Member member = memberRepository.findById(adminReservationRequest.memberId()).orElseThrow();
        Reservation requestedReservation = new Reservation(adminReservationRequest.date(), member, requestedReservationTime, requestedTheme);

        rejectPastTimeReservation(requestedReservation);
        rejectDuplicateReservation(requestedReservation);

        Reservation savedReservation = reservationRepository.save(requestedReservation);
        return ReservationResponse.from(savedReservation);
    }

    private void rejectPastTimeReservation(Reservation reservation) {
        if (reservation.isBeforeNow()) {
            LocalDateTime reservationDataTime = reservation.getDateTime();
            throw new IllegalArgumentException("이미 지난 시간입니다. 입력한 시간: " + reservationDataTime.toLocalDate() + " "
                    + reservationDataTime.toLocalTime());
        }
    }

    private void rejectDuplicateReservation(Reservation reservation) {
        List<Reservation> savedReservations = reservationRepository.findAll();
        boolean isDuplicateReservationPresent = savedReservations.stream()
                .filter(reservation::hasSameTheme)
                .anyMatch(reservation::hasSameDateTime);

        if (isDuplicateReservationPresent) {
            throw new IllegalArgumentException("중복된 예약이 존재합니다.");
        }
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public ReservationResponse findMember(AdminReservationRequest adminReservationRequest) {
        ReservationTime requestedReservationTime = reservationTimeRepository.findById(adminReservationRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("예약할 수 없는 시간입니다. timeId: " + adminReservationRequest.timeId()));
        Theme requestedTheme = themeRepository.findById(adminReservationRequest.themeId())
                .orElseThrow(() -> new IllegalArgumentException("예약할 수 없는 테마입니다. themeId: " + adminReservationRequest.themeId()));
        Member member = memberRepository.findById(adminReservationRequest.memberId()).orElseThrow();
        Reservation requestedReservation = reservationRepository.save(new Reservation(adminReservationRequest.date(), member, requestedReservationTime, requestedTheme));

        return ReservationResponse.from(requestedReservation);
    }

    public UserResponse findByEmail(String email) {
        Member byEmail = memberRepository.findByEmail(email);
        return new UserResponse(byEmail.getId(), byEmail.getName(), byEmail.getEmail(), byEmail.getPassword(), byEmail.getRole());
    }

    public List<ReservationResponse> findSearchReservation(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        List<Reservation> reservations = reservationRepository.findSearchReservation(themeId, memberId, dateFrom, dateTo);

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
