package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.member.Member;
import roomescape.exception.IllegalUserRequestException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.AdminReservationSaveRequest;
import roomescape.service.dto.ReservationSaveRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              MemberRepository memberRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public Reservation createReservation(AdminReservationSaveRequest request) {
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new IllegalUserRequestException("존재하지 않는 사용자입니다."));

        return createReservation(request.toReservationSaveRequest(), member);
    }

    public Reservation createReservation(ReservationSaveRequest request, Member member) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new IllegalUserRequestException("존재하지 않는 예약 시간 입니다."));

        validateDateIsFuture(toLocalDateTime(request.date(), reservationTime));

        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new IllegalUserRequestException("존재하지 않는 테마 입니다."));

        if (reservationRepository.existsByDateAndTimeIdAndThemeId(request.date(), request.timeId(), request.themeId())) {
            throw new IllegalUserRequestException("해당 시간에 이미 예약된 테마입니다.");
        }

        Reservation reservation = request.toEntity(member, reservationTime, theme);
        return reservationRepository.save(reservation);
    }

    private void validateDateIsFuture(LocalDateTime localDateTime) {
        if (localDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalUserRequestException("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
        }
    }

    private LocalDateTime toLocalDateTime(LocalDate date, ReservationTime reservationTime) {
        return LocalDateTime.of(date, reservationTime.getStartAt());
    }

    public List<Reservation> findReservations() {
        return reservationRepository.findAll();
    }

    public void deleteReservation(Long id) {
        int deletedCount = reservationRepository.deleteById(id);
        if (deletedCount == 0) {
            throw new IllegalUserRequestException("존재하지 않는 예약입니다.");
        }
    }
}
