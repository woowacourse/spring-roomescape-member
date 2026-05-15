package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.ReservationWithTimeAndTheme;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.domain.reservation.ReservationWithTime;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.exception.DuplicatedRequestException;
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
    private static final String UNAUTHORIZED_UPDATE_RESERVATION_REQUEST = "해당 예약을 수정할 권한이 없습니다.";
    private static final String UNAUTHORIZED_DELETE_RESERVATION_REQUEST = "해당 예약을 삭제할 권한이 없습니다.";
    private static final String CANNOT_UPDATE_RESERVATION = "수정하려는 예약이 존재하지 않아서 수정할 수 없습니다.";
    private static final String CANNOT_UPDATE_PAST_RESERVATION = "이미 지난 예약은 수정할 수 없습니다.";
    private static final String CANNOT_DELETE_PAST_RESERVATION = "이미 지난 예약은 삭제할 수 없습니다.";

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationWithTimeAndTheme> getAllReservation(String name) {
        return reservationRepository.getAllReservation(name);
    }

    public ReservationWithTimeAndTheme addReservation(ReservationCommand reservationCommand) {
        validateAddReservation(reservationCommand);

        long id = reservationRepository.addReservation(reservationCommand);
        return getReservationWithTimeAndData(id);
    }

    public void deleteReservation(long id, String name) {
        validateDeleteReservation(id, name);
        reservationRepository.deleteReservation(id);
    }

    public void updateReservation(long id, String name, ReservationCommand reservationCommand) {
        validateUpdateReservation(id, name, reservationCommand);

        int updatedRow = reservationRepository.updateAll(id, reservationCommand);

        if (updatedRow == 0) {
            throw new NotFoundResourceException(CANNOT_UPDATE_RESERVATION);
        }
    }

    private ReservationWithTime getReservationWithTime(long id) {
        return getData(() -> reservationRepository.getReservationWithTime(id), INVALID_RESERVATION_ID);
    }

    private ReservationWithTimeAndTheme getReservationWithTimeAndData(long id) {
        return getData(() -> reservationRepository.getReservationWithTimeAndTheme(id), INVALID_RESERVATION_ID);
    }

    private ReservationTime getReservationTime(long id) {
        return getData(() -> reservationTimeRepository.getReservationTime(id), INVALID_RESERVATION_TIME_ID);
    }

    private <T> T getData(Supplier<Optional<T>> supplier, String errorMessage) {
        Optional<T> optionalData = supplier.get();

        if(optionalData.isEmpty()) {
            throw new NotFoundResourceException(errorMessage);
        }

        return optionalData.get();
    }

    private void validateAddReservation(ReservationCommand reservationCommand) {
        validateAvailableReservation(reservationCommand.timeId(), reservationCommand.themeId(), reservationCommand.date());

        ReservationTime reservationTime = getReservationTime(reservationCommand.timeId());
        reservationCommand.validatePastDateTime(reservationTime);
    }

    private void validateDeleteReservation(long id, String name) {
        ReservationWithTime reservationWithTime = getReservationWithTime(id);

        if (!reservationWithTime.name().equals(name)) {
            throw new UnauthorizedException(UNAUTHORIZED_DELETE_RESERVATION_REQUEST);
        }
        reservationWithTime.validDateReservationPastDateTime(CANNOT_DELETE_PAST_RESERVATION);
    }

    private void validateUpdateReservation(long id, String name, ReservationCommand reservationCommand) {
        ReservationWithTime reservationWithTime = getReservationWithTime(id);

        if (!reservationWithTime.name().equals(name)) {
            throw new UnauthorizedException(UNAUTHORIZED_UPDATE_RESERVATION_REQUEST);
        }

        validateAvailableReservation(reservationCommand.timeId(), reservationCommand.themeId(), reservationCommand.date());

        ReservationTime reservationTime = getReservationTime(reservationCommand.timeId());
        reservationCommand.validatePastDateTime(reservationTime);

        reservationWithTime.validDateReservationPastDateTime(CANNOT_UPDATE_PAST_RESERVATION);
        reservationWithTime.validateEqualValue(reservationCommand);
    }

    private void validateAvailableReservation(long timeId, long themeId, LocalDate date) {
        if(!themeRepository.isExistsById(themeId)) {
            throw new NotFoundResourceException(INVALID_THEME_ID);
        }

        if (reservationRepository.existsByTimeIdAndThemeIdAndDate(timeId, themeId, date)) {
            throw new DuplicatedRequestException(DUPLICATED_RESERVATION_REQUEST);
        }
    }
}
