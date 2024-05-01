package roomescape.service.reservation;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.SaveReservationRequest;

@Service
public class ReservationCreateService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationCreateService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public Reservation createReservation(SaveReservationRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간 입니다."));
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마 입니다."));
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(request.date(), request.timeId(), request.themeId())) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
        Reservation reservation = SaveReservationRequest.toEntity(request, reservationTime, theme);

        return reservationRepository.save(reservation);
    }
}
