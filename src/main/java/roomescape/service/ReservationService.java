package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationSlots;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRanking;
import roomescape.dto.request.CreateReservationRequest;
import roomescape.dto.request.AdminCreateReservationRequest;
import roomescape.dto.request.AvailableTimeRequest;
import roomescape.exception.InvalidMemberException;
import roomescape.exception.InvalidReservationException;
import roomescape.exception.InvalidReservationTimeException;
import roomescape.exception.InvalidThemeException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private static final int THEME_RANKING_END_RANGE = 7;
    private static final int THEME_RANKING_START_RANGE = 1;

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

    public Reservation addReservation(CreateReservationRequest request, LoginMember loginMember) {
        return createReservation(loginMember.getId(), request.themeId(), request.date(), request.timeId());
    }

    public Reservation addReservationByAdmin(AdminCreateReservationRequest request) {
        return createReservation(request.memberId(), request.themeId(), request.date(), request.timeId());
    }

    private Reservation createReservation(long memberId, long themeId, LocalDate date, long timeId) {
        Member member = getMemberById(memberId);
        ReservationTime reservationTime = getReservationTimeById(timeId);
        Theme theme = getThemeById(themeId);

        Reservation reservation = new Reservation(member, date, reservationTime, theme);

        validateDuplicateReservation(reservation);
        validateAddReservationDateTime(reservation);
        return reservationRepository.add(reservation);
    }

    private void validateDuplicateReservation(Reservation reservation) {
        if (reservationRepository.existsByDateAndTimeIdAndTheme(reservation)) {
            throw new InvalidReservationException("중복된 예약신청입니다");
        }
    }

    private void validateAddReservationDateTime(Reservation reservation) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (reservation.isBefore(currentDateTime)) {
            throw new InvalidReservationException("과거 시간에 예약할 수 없습니다.");
        }
    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findAllByFilter(Long memberId, Long themeId, LocalDate dateFrom, LocalDate dateTo) {
        return reservationRepository.findAllByFilter(memberId, themeId, dateFrom, dateTo);
    }

    public Reservation getReservationById(long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new InvalidReservationException("존재하지 않는 예약입니다."));
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public ReservationSlots getReservationSlots(AvailableTimeRequest request) {
        List<ReservationTime> times = reservationTimeRepository.findAll();

        List<Reservation> alreadyReservedReservations = reservationRepository.findAllByDateAndThemeId(
                request.date(), request.themeId());

        return new ReservationSlots(times, alreadyReservedReservations);
    }

    public List<Theme> getRankingThemes(LocalDate originDate) {
        LocalDate end = originDate.minusDays(THEME_RANKING_START_RANGE);
        LocalDate start = end.minusDays(THEME_RANKING_END_RANGE);
        List<Reservation> inRangeReservations = reservationRepository.findAllByDateInRange(start, end);

        ThemeRanking themeRanking = new ThemeRanking(inRangeReservations);
        return themeRanking.getAscendingRanking();
    }

    private Theme getThemeById(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new InvalidThemeException("존재하지 않는 테마 id입니다."));
    }

    private ReservationTime getReservationTimeById(long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new InvalidReservationTimeException("존재하지 않는 예약 시간 id입니다."));
    }

    private Member getMemberById(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new InvalidMemberException("존재하지 않는 멤버 id입니다."));
    }
}
