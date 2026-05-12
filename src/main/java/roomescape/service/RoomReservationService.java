package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.dto.reservation.AddReservationRequest;
import roomescape.dto.reservation.ReservationCondition;
import roomescape.exception.DuplicatedReservationRequestException;
import roomescape.exception.ErrorMessage;
import roomescape.exception.NotFoundResourceException;
import roomescape.repository.theme.ThemeRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

@Service
public class RoomReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public RoomReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> getAllReservation() {
        return reservationRepository.getAllReservation();
    }

    public List<Reservation> getAllReservationByName(ReservationCondition reservationCondition) {
        return reservationRepository.getAllReservationByName(reservationCondition.name());
    }

    @Transactional
    public Reservation addReservation(AddReservationRequest addReservationRequest) {
        validateDate(addReservationRequest.date());

        ReservationTime reservationTime = reservationTimeRepository.getReservationTime(addReservationRequest.timeId())
                .orElseThrow(() -> new NotFoundResourceException(ErrorMessage.INVALID_RESERVATION_TIME_ID));

        Theme theme = themeRepository.getTheme(addReservationRequest.themeId())
                .orElseThrow(() -> new NotFoundResourceException(ErrorMessage.INVALID_THEME_ID));

        if (reservationRepository.existsByTimeIdAndThemeIdAndDate(addReservationRequest.timeId(), addReservationRequest.themeId(), addReservationRequest.date())) {
            throw new DuplicatedReservationRequestException(ErrorMessage.DUPLICATED_RESERVATION_REQUEST);
        }

        return reservationRepository.addReservation(addReservationRequest.toEntity(reservationTime, theme));
    }

    @Transactional
    public void deleteReservation(long id) {
        reservationRepository.deleteReservation(id);
    }

    private void validateDate(LocalDate reservationDate) {
        LocalDate today = LocalDate.now();

        if (reservationDate.isBefore(today)) {
            throw new IllegalArgumentException("지난 날짜에는 예약할 수 없습니다.");
        }
    }
}
