package roomescape.application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.global.exception.BusinessException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Reservation saveReservation(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "예약 시간 ID를 찾을 수 없습니다."));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "테마 ID를 찾을 수 없습니다."));
        validateSaveReservation(date, time, themeId);
        Reservation reservation = Reservation.create(
                name,
                date,
                time,
                theme
        );
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByDateAndTheme(LocalDate date, Long themeId) {
        return reservationRepository.findByDateAndThemeId(date, themeId);
    }

    @Transactional
    public void deleteReservation(Long id) {
        if (id == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "테마 ID가 비어있습니다.");
        }
        reservationRepository.deleteById(id);
    }

    private void validateSaveReservation(LocalDate date, ReservationTime timeId, Long themeId) {
        validateAlreadyReservation(date, timeId.getId(), themeId);
        validatePastDateReservation(date);
        validatePastTimeReservation(date, timeId);
    }

    private void validateAlreadyReservation(
            LocalDate date,
            Long timeId,
            Long themeId
    ) {
        boolean exists = reservationRepository
                .findByDateAndTimeIdAndThemeId(date, timeId, themeId)
                .isPresent();

        if (exists) {
            throw new BusinessException(HttpStatus.NOT_ACCEPTABLE, "이미 예약된 시간입니다.");
        }
    }

    private void validatePastDateReservation(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new BusinessException(HttpStatus.NOT_ACCEPTABLE, "이미 지난 날짜입니다.");
        }
    }

    private void validatePastTimeReservation(LocalDate date, ReservationTime time) {
        if (date.isEqual(LocalDate.now()) && time.getStartAt().isBefore(LocalTime.now())) {
            throw new BusinessException(HttpStatus.NOT_ACCEPTABLE, "예약 시간이 현재보다 이전일 수 없습니다.");
        }
    }
}
