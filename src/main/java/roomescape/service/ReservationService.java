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
public class ReservationService {
    private static final String DUPLICATED_RESERVATION_REQUEST = "해당 날짜, 시간, 테마의 예약이 존재하여 예약할 수 없습니다.";
    private static final String INVALID_THEME_ID = "존재하지 않은 테마 id입니다.";
    private static final String INVALID_RESERVATION_TIME_ID = "존재하지 않은 시간 id입니다.";
    private static final String INVALID_RESERVATION_ID = "존재하지 않는 예약 id입니다.";
    private static final String CANNOT_DELETE_PAST_RESERVATION = "이미 지난 예약은 삭제할 수 없습니다.";
    private static final String CANNOT_UPDATE_PAST_RESERVATION = "이미 지난 예약은 수정할 수 없습니다.";
    private static final String UNAUTHORIZED_DELETE_RESERVATION_REQUEST = "해당 예약을 삭제할 권한이 없습니다.";
    private static final String UNAUTHORIZED_UPDATE_RESERVATION_REQUEST = "해당 예약을 수정할 권한이 없습니다.";
    private static final String CANNOT_UPDATE_SAME_VALUE = "기존 정보와 동일하여 수정할 내용이 없습니다.";
    private static final String CANNOT_UPDATE_RESERVATION = "수정하려는 예약이 존재하지 않아서 수정할 수 없습니다.";
    private static final String CANNOT_UPDATE_DUPLICATED_TIME = "해당 날짜, 테마, 시간으로 이미 존재하는 예약이 있습니다.";

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
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

    @Transactional
    public void deleteReservation(long id, String name) {
        Reservation reservation = getReservation(id);

        if(!reservation.name().equals(name)) {
            throw new UnauthorizedException(UNAUTHORIZED_DELETE_RESERVATION_REQUEST);
        }

        if(reservation.date().isBefore(LocalDate.now())) {
            throw new InvalidRequestValueException(CANNOT_DELETE_PAST_RESERVATION);
        }

        reservationRepository.deleteReservation(id);
    }

    @Transactional
    public void updateReservation(long id, String name, ReservationCommand reservationCommand) {
        Reservation reservation = getReservation(id);

        if (!reservation.name().equals(name)) {
            throw new UnauthorizedException(UNAUTHORIZED_UPDATE_RESERVATION_REQUEST);
        }

        if (reservation.isEqualValue(reservationCommand)) {
            throw new InvalidRequestValueException(CANNOT_UPDATE_SAME_VALUE);
        }

        if(reservation.date().isBefore(LocalDate.now())) {
            throw new InvalidRequestValueException(CANNOT_UPDATE_PAST_RESERVATION);
        }

        if (reservationRepository.existsByTimeIdAndThemeIdAndDate(reservationCommand.timeId(),
                reservationCommand.themeId(), reservationCommand.date())) {
            throw new InvalidRequestValueException(CANNOT_UPDATE_DUPLICATED_TIME);
        }

        int updatedRow = reservationRepository.updateAll(id, reservationCommand);

        if (updatedRow == 0) {
            throw new NotFoundResourceException(CANNOT_UPDATE_RESERVATION);
        }
    }

    private Reservation getReservation(long id) {
        Optional<Reservation> optionalReservation = reservationRepository.getReservation(id);

        if(optionalReservation.isEmpty()) {
            throw new NotFoundResourceException(INVALID_RESERVATION_ID);
        }

        return optionalReservation.get();
    }
}
