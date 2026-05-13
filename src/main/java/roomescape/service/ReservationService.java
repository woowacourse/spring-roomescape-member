package roomescape.service;

import static roomescape.service.ReservationTimeService.TIME_SLOT_DOES_NOT_EXIST;
import static roomescape.service.ThemeService.THEME_DOES_NOT_EXISTS;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {
    public static final String INVALID_RESERVATION_ID = "요청한 예약을 찾을 수 없습니다.";
    private static final String DUPLICATED_RESERVATION = "이미 예약된 테마의 시간대입니다.";

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation find(long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(
                () -> new IllegalArgumentException(INVALID_RESERVATION_ID));
    }

    @Transactional
    public Reservation reserve(ReservationCreateRequest request, LocalDateTime now) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.getTimeId())
                .orElseThrow(() -> new IllegalArgumentException(TIME_SLOT_DOES_NOT_EXIST));
        Theme theme = themeRepository.findById(request.getThemeId()).orElseThrow(
                () -> new IllegalArgumentException(THEME_DOES_NOT_EXISTS));

        Reservation reservation = Reservation.reserve(Name.from(request.getName()),
                ReservationDate.from(request.getDate()),
                reservationTime, theme, now);

        if (reservationRepository.existsByTimeAndThemeAndDate(request.getTimeId(), request.getThemeId(),
                request.getDate())) {
            throw new IllegalArgumentException(DUPLICATED_RESERVATION);
        }

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void cancel(long reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new IllegalArgumentException(INVALID_RESERVATION_ID);
        }
        reservationRepository.deleteById(reservationId);
    }
}
