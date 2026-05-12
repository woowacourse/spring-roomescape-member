package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationInfo;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.exception.DuplicatedRequestException;
import roomescape.exception.InvalidRequestValueException;
import roomescape.exception.NotFoundResourceException;
import roomescape.exception.UnauthorizedException;
import roomescape.repository.theme.ThemeRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

@Service
public class RoomReservationService {
    private static final String ADMIN_NAME = "admin";

    private static final String DUPLICATED_RESERVATION_REQUEST = "해당 날짜, 시간, 테마의 예약이 존재하여 예약할 수 없습니다.";
    private static final String INVALID_THEME_ID = "존재하지 않은 테마 id입니다.";
    private static final String INVALID_RESERVATION_TIME_ID = "존재하지 않은 시간 id입니다.";
    private static final String INVALID_RESERVATION_ID = "존재하지 않는 예약 id입니다.";
    private static final String CANNOT_DELETE_PAST_RESERVATION = "이미 지난 예약은 삭제할 수 없습니다.";
    private static final String UNAUTHORIZED_DELETE_RESERVATION_REQUEST = "해당 예약을 삭제할 권한이 없습니다.";

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public RoomReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationInfo> getAllReservation(String name) {
        return reservationRepository.getAllReservation(name);
    }

    @Transactional
    public ReservationInfo addReservation(ReservationCommand reservationCommand) {
        ReservationTime reservationTime = reservationTimeRepository.getReservationTime(reservationCommand.timeId())
                .orElseThrow(() -> new NotFoundResourceException(INVALID_RESERVATION_TIME_ID));

        Theme theme = themeRepository.getTheme(reservationCommand.themeId())
                .orElseThrow(() -> new NotFoundResourceException(INVALID_THEME_ID));

        if (reservationRepository.existsByTimeIdAndThemeIdAndDate(reservationCommand.timeId(), reservationCommand.themeId(), reservationCommand.date())) {
            throw new DuplicatedRequestException(DUPLICATED_RESERVATION_REQUEST);
        }

        return reservationRepository.addReservation(reservationCommand, reservationTime, theme);
    }

    public void deleteReservation(long id, String name) {
        Optional<Reservation> optionalReservation = reservationRepository.getReservation(id);

        if(optionalReservation.isEmpty()) {
            throw new NotFoundResourceException(INVALID_RESERVATION_ID);
        }

        Reservation reservation = optionalReservation.get();

        if(reservation.date().isBefore(LocalDate.now())) {
            throw new InvalidRequestValueException(CANNOT_DELETE_PAST_RESERVATION);
        }

        if(name == null || (!reservation.name().equals(name) && !name.equals(ADMIN_NAME))) {
            throw new UnauthorizedException(UNAUTHORIZED_DELETE_RESERVATION_REQUEST);
        }

        reservationRepository.deleteReservation(id);
    }
}
