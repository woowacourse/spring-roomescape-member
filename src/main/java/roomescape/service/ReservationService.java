package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.dto.AdminReservationRequest;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.repository.JdbcMemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final JdbcMemberRepository memberRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository timeRepository, final ThemeRepository themeRepository,
                              final JdbcMemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public Long addReservation(Member member, ReservationRequest reservationRequest) {
        ReservationTime reservationTime = findReservationTime(reservationRequest);
        Theme theme = findTheme(reservationRequest);
        validateReservationNotDuplicate(reservationRequest);
        validateUnPassedDate(reservationRequest.date(), reservationTime.getStartAt());
        Reservation reservationToSave = reservationRequest.toEntity(member, reservationTime, theme);
        return reservationRepository.save(reservationToSave);
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream().map(ReservationResponse::from).toList();
    }

    public ReservationResponse getReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id가 존재하지 않습니다 : " + id));
        return ReservationResponse.from(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id가 존재하지 않습니다 : " + id));
        reservationRepository.delete(id);
    }

    private ReservationTime findReservationTime(ReservationRequest reservationRequest) {
        Long timeId = reservationRequest.timeId();
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("time_id가 존재하지 않습니다 : " + timeId));
    }

    private Theme findTheme(ReservationRequest reservationRequest) {
        Long themeId = reservationRequest.themeId();
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("theme_id가 존재하지 않습니다 : " + themeId));
    }

    private void validateReservationNotDuplicate(ReservationRequest reservationRequest) {
        if (reservationRepository.existsByDateTimeAndTheme(reservationRequest.date(), reservationRequest.timeId(),
                reservationRequest.themeId())) {
            throw new IllegalArgumentException("이미 해당 시간에 동일한 테마의 예약이 존재합니다.");
        }
    }

    private void validateUnPassedDate(LocalDate date, LocalTime time) {
        if ((date.isBefore(LocalDate.now()) && time.isBefore(LocalTime.now()))
            || date.isEqual(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다. : " + date + " " + time);
        }
    }

    public void addReservation(final AdminReservationRequest adminReservationRequest, final Member member) {
        if (!member.getRole().equals("admin")) {
            throw new IllegalArgumentException("관리자가 아닙니다.");
        }
        Member user = memberRepository.findById(adminReservationRequest.memberId()).orElseThrow();
        Theme theme = themeRepository.findById(adminReservationRequest.themeId()).orElseThrow();
        LocalDate date = adminReservationRequest.date();
        ReservationTime time = timeRepository.findById(adminReservationRequest.timeId()).orElseThrow();
        reservationRepository.save(new Reservation(user, date, time, theme));
    }

    public List<ReservationResponse> getFilteredReservations(final Long themeId, final Long memberId,
                                                             final LocalDate dateFrom, final LocalDate dateTo) {
        return reservationRepository.findByConditions(themeId, memberId, dateFrom, dateTo)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
