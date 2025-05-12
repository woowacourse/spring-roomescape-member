package roomescape.reservation.application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.MemberReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.infrastructure.ReservationRepository;
import roomescape.time.domain.Time;
import roomescape.theme.infrastructure.ThemeRepository;
import roomescape.theme.domain.Theme;
import roomescape.time.infrastructure.TimeRepository;
import roomescape.member.application.MemberNotFoundException;
import roomescape.member.domain.Member;
import roomescape.member.infrastructure.MemberRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              TimeRepository timeRepository,
                              ThemeRepository themeRepository, MemberRepository memberRepository) {

        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse addByUser(MemberReservationRequest request, Member member) {
        Time time = findReservationTimeOrThrow(request.timeId());

        LocalDate date = request.date();
        validateReservationTimeNotInPast(date, time);
        validateNoDuplicateReservation(date, request.timeId(), request.themeId());

        Theme theme = findThemeOrThrow(request.themeId());

        Reservation reservation = Reservation.createBeforeSaved(member, date, time, theme);
        return ReservationResponse.from(reservationRepository.add(reservation));
    }

    public ReservationResponse addByAdmin(AdminReservationRequest request) {
        Member user = memberRepository.findById(request.memberId())
                .orElseThrow(MemberNotFoundException::new);

        Time time = findReservationTimeOrThrow(request.timeId());
        Theme theme = findThemeOrThrow(request.themeId());

        Reservation reservation = Reservation.createBeforeSaved(user, request.date(), time, theme);
        return ReservationResponse.from(reservationRepository.add(reservation));
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> findBySearchFilter(Long themeId,
                                                        Long memberId,
                                                        LocalDate dateFrom,
                                                        LocalDate dateTo) {
        return reservationRepository.findBySearchFilter(themeId, memberId, dateFrom, dateTo).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteById(Long id) {
        ensureReservationExists(id);
        reservationRepository.deleteById(id);
    }

    private void validateReservationTimeNotInPast(LocalDate date, Time time) {
        if (date.isEqual(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("[ERROR] 지난 시간으로는 예약할 수 없습니다.");
        }
    }

    private void validateNoDuplicateReservation(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new IllegalArgumentException("[ERROR] 이미 예약이 존재합니다.");
        }
    }

    private void ensureReservationExists(Long id) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당 id의 예약이 존재하지 않습니다."));
    }

    private Time findReservationTimeOrThrow(Long timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 시간 id가 존재하지 않습니다."));
    }

    private Theme findThemeOrThrow(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 테마 id가 존재하지 않습니다."));
    }
}
