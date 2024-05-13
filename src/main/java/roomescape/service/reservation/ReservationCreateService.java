package roomescape.service.reservation;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.request.ReservationAdminSaveRequest;
import roomescape.service.dto.request.ReservationSaveRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ReservationCreateService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationCreateService(ReservationRepository reservationRepository,
                                    ReservationTimeRepository reservationTimeRepository,
                                    ThemeRepository themeRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public Reservation createReservationByAdmin(ReservationAdminSaveRequest request) {
        ReservationTime reservationTime = getValidReservationTime(request.timeId());
        validateDateIsFuture(toLocalDateTime(request.date(), reservationTime));
        Theme theme = getValidTheme(request.themeId());
        validateAlreadyBooked(request.date(), request.timeId(), request.themeId());

        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Reservation reservation = request.toEntity(request, reservationTime, theme, member);
        return reservationRepository.save(reservation);
    }

    public Reservation createReservationByUser(ReservationSaveRequest request, Member member) {
        ReservationTime reservationTime = getValidReservationTime(request.timeId());
        validateDateIsFuture(toLocalDateTime(request.date(), reservationTime));
        Theme theme = getValidTheme(request.themeId());
        validateAlreadyBooked(request.date(), request.timeId(), request.themeId());

        Reservation reservation = request.toEntity(request, reservationTime, theme, member);
        return reservationRepository.save(reservation);
    }

    private ReservationTime getValidReservationTime(long request) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간 입니다."));
        return reservationTime;
    }

    private Theme getValidTheme(long request) {
        Theme theme = themeRepository.findById(request)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마 입니다."));
        return theme;
    }

    private void validateAlreadyBooked(LocalDate date, long timeId, long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new IllegalArgumentException("해당 시간에 이미 예약된 테마입니다.");
        }
    }

    private void validateDateIsFuture(LocalDateTime localDateTime) {
        if (localDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
        }
    }

    private LocalDateTime toLocalDateTime(LocalDate date, ReservationTime reservationTime) {
        return LocalDateTime.of(date, reservationTime.getStartAt());
    }
}
