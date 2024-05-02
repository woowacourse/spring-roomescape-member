package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository timeRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public Long addReservation(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = findReservationTime(reservationRequest);
        Theme theme = findTheme(reservationRequest);
        validateReservationNotDuplicate(reservationRequest);
        validateUnPassedDate(reservationRequest.date(),reservationTime.getStartAt());
        Reservation reservationToSave = reservationRequest.toEntity(reservationTime, theme);
        return reservationRepository.save(reservationToSave);
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse getReservation(Long id) {
        validateIdExist(id);
        Reservation reservation = reservationRepository.findById(id);
        return ReservationResponse.from(reservation);
    }

    public void deleteReservation(Long id) {
        validateIdExist(id);
        reservationRepository.delete(id);
    }

    private ReservationTime findReservationTime(ReservationRequest reservationRequest) {
        Long timeId = reservationRequest.timeId();
        if (!timeRepository.existId(timeId)) {
            throw new IllegalArgumentException("[ERROR] time_id가 존재하지 않습니다 : " + timeId);
        }
        return timeRepository.findById(timeId);
    }

    private Theme findTheme(ReservationRequest reservationRequest) {
        Long themeId = reservationRequest.themeId();
        if (!themeRepository.existId(themeId)) {
            throw new IllegalArgumentException("[ERROR] theme_id가 존재하지 않습니다 : " + themeId);
        }
        return themeRepository.findById(themeId);
    }

    private void validateReservationNotDuplicate(ReservationRequest reservationRequest) {
        if (reservationRepository.existDateTimeAndTheme(
                reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId())
        ) {
            throw new IllegalArgumentException("[ERROR] 이미 해당 시간에 동일한 테마의 예약이 존재합니다.");
        }
    }

    private void validateUnPassedDate(LocalDate date, LocalTime time) {
        if ((date.isBefore(LocalDate.now()) && time.isBefore(LocalTime.now())) ||
                date.isEqual(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("[ERROR] 지나간 날짜와 시간에 대한 예약 생성은 불가능합니다. : " + date + " " + time);
        }
    }

    public void validateIdExist(Long id) {
        if (!reservationRepository.existId(id)) {
            throw new IllegalArgumentException("[ERROR] id가 존재하지 않습니다 : " + id);
        }
    }
}
