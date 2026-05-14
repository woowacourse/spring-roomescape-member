package roomescape.service;

import common.exception.ErrorCode;
import common.exception.RoomEscapeException;
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
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> findList(String name) {
        if (name != null) {
            return reservationRepository.findAllByName(name);
        }
        return reservationRepository.findAll();
    }

    public Reservation find(long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(
                () -> new RoomEscapeException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    @Transactional
    public Reservation reserve(ReservationCreateRequest request, LocalDateTime now) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.getTimeId())
                .orElseThrow(() -> new RoomEscapeException(ErrorCode.RESERVATION_TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(request.getThemeId()).orElseThrow(
                () -> new RoomEscapeException(ErrorCode.THEME_NOT_FOUND));

        Reservation reservation = Reservation.reserve(Name.from(request.getName()),
                ReservationDate.from(request.getDate()),
                reservationTime, theme, now);

        if (reservationRepository.existsByTimeAndThemeAndDate(request.getTimeId(), request.getThemeId(),
                request.getDate())) {
            throw new RoomEscapeException(ErrorCode.DUPLICATE_RESERVATION);
        }

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void cancel(long reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new RoomEscapeException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        reservationRepository.deleteById(reservationId);
    }
}
