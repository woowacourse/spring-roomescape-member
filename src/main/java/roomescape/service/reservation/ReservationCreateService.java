package roomescape.service.reservation;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.request.ReservationSaveRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ReservationCreateService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationCreateService(ReservationRepository reservationRepository,
                                    ReservationTimeRepository reservationTimeRepository,
                                    ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public Reservation createReservation(ReservationSaveRequest request, Member member) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간 입니다."));

        validateDateIsFuture(toLocalDateTime(request.date(), reservationTime));

        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마 입니다."));

        if (reservationRepository.existsByDateAndTimeIdAndThemeId(request.date(), request.timeId(), request.themeId())) {
            throw new IllegalArgumentException("해당 시간에 이미 예약된 테마입니다.");
        }

        Reservation reservation = ReservationSaveRequest.toEntity(request, reservationTime, theme, member);
        return reservationRepository.save(reservation);
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
