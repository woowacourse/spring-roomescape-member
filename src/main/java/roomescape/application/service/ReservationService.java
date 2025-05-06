package roomescape.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.exception.ReservationDuplicatedException;
import roomescape.domain.exception.ResourceNotExistException;
import roomescape.domain.model.Reservation;
import roomescape.domain.model.ReservationTime;
import roomescape.domain.model.Theme;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.repository.ThemeRepository;
import roomescape.presentation.dto.request.ReservationRequest;
import roomescape.presentation.dto.response.ReservationResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(final ReservationRepository reservationRepository, final ReservationTimeRepository reservationTimeRepository, final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public void deleteReservation(Long id) {
        int count = reservationRepository.deleteById(id);
        if (count == 0) {
            throw new ResourceNotExistException();
        }
    }

    @Transactional
    public ReservationResponse save(ReservationRequest request) {
        ReservationTime reservationTime = getReservationTime(request.timeId());
        Theme theme = getTheme(request.themeId());

        validateSaveReservation(request, reservationTime);
        Reservation reservation = new Reservation(
                request.name(),
                request.date(),
                reservationTime,
                theme
        );
        return getReservationResponse(reservation);
    }

    private ReservationTime getReservationTime(final Long timeId) {
        return reservationTimeRepository.findById(timeId);
    }

    private Theme getTheme(final Long themeId) {
        return themeRepository.findById(themeId);
    }

    private void validateSaveReservation(
            ReservationRequest request,
            ReservationTime reservationTime
    ) {
        validateIsDuplicate(request);
        validateNotPast(request.date(), reservationTime);
    }

    private void validateIsDuplicate(ReservationRequest request) {
        boolean isReservationExist = reservationRepository.existByTimeIdAndThemeIdAndDate(request.timeId(), request.themeId(), request.date());
        if (isReservationExist) {
            throw new ReservationDuplicatedException();
        }
    }

    private void validateNotPast(LocalDate date, ReservationTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("[ERROR] 현재보다 과거 시간에는 예약이 불가능합니다.");
        }
    }

    private ReservationResponse getReservationResponse(Reservation reservation) {
        return ReservationResponse.from(reservationRepository.save(reservation));
    }
}
