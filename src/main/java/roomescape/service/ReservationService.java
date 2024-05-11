package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.AdminReservationRequest;
import roomescape.dto.MemberReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.repository.JdbcMemberRepository;
import roomescape.repository.JdbcReservationRepository;
import roomescape.repository.JdbcReservationTimeRepository;
import roomescape.repository.JdbcThemeRepository;

@Service
public class ReservationService {

    private final JdbcReservationRepository reservationRepository;
    private final JdbcReservationTimeRepository reservationTimeRepository;
    private final JdbcThemeRepository themeRepository;
    private final JdbcMemberRepository memberRepository;

    public ReservationService(JdbcReservationRepository reservationRepository,
                              JdbcReservationTimeRepository reservationTimeRepository,
                              JdbcThemeRepository themeRepository,
                              JdbcMemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse addReservation(MemberReservationRequest request, Member member) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId());
        validateNotDuplicatedTime(request.date(), request.timeId(), request.themeId());
        Theme theme = themeRepository.findById(request.themeId());

        Reservation reservation = new Reservation(member, request.date(), reservationTime,
                theme, LocalDateTime.now());
        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    public ReservationResponse addAdminReservation(AdminReservationRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId());
        validateNotDuplicatedTime(request.date(), request.timeId(), request.themeId());
        Theme theme = themeRepository.findById(request.themeId());
        Member member = memberRepository.findById(request.memberId());

        Reservation reservation = new Reservation(member, request.date(), reservationTime, theme, LocalDateTime.now());
        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    private void validateNotDuplicatedTime(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new IllegalArgumentException("중복 예약은 불가능하다.");
        }
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
