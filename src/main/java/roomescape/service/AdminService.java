package roomescape.service;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.repository.MemberRepository;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.repository.ThemeRepository;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.request.AdminReservationSearchRequest;
import roomescape.dto.response.AdminReservationResponse;
import roomescape.dto.response.ReservationResponse;

@Service
@Transactional(readOnly = true)
public class AdminService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public AdminService(ReservationRepository reservationRepository,
                        ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository,
                        MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public List<ReservationResponse> getReservations(
            @Valid AdminReservationSearchRequest adminReservationSearchRequest) {

        List<Reservation> reservations = reservationRepository.findByMemberIdAndThemeId(
                adminReservationSearchRequest.memberId(),
                adminReservationSearchRequest.themeId(),
                adminReservationSearchRequest.dateFrom(),
                adminReservationSearchRequest.dateTo()
        );

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public AdminReservationResponse addReservation(AdminReservationRequest adminReservationRequest) {
        ReservationTime reservationTime = reservationTimeRepository.getById(adminReservationRequest.timeId());
        Theme theme = themeRepository.getById(adminReservationRequest.themeId());
        Member member = memberRepository.getById(adminReservationRequest.memberId());
        Reservation reservation = adminReservationRequest.toReservation(member, reservationTime, theme);

        validateDateTimeNotPassed(reservation.getDate(), reservationTime.getStartAt());
        validateDuplicatedReservation(reservation);

        Reservation savedReservation = reservationRepository.save(reservation);

        return AdminReservationResponse.from(savedReservation);
    }

    private void validateDateTimeNotPassed(LocalDate date, LocalTime startAt) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(date, startAt);

        if (reservationDateTime.isBefore(now)) {
            throw new IllegalArgumentException("지나간 날짜/시간에 대한 예약은 불가능합니다.");
        }
    }

    private void validateDuplicatedReservation(Reservation reservation) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(
                reservation.getDate(),
                reservation.getTimeId(),
                reservation.getThemeId())) {
            throw new IllegalArgumentException("해당 날짜/시간에 이미 예약이 존재합니다.");
        }
    }
}
