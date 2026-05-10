package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ReservationRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.global.exception.DuplicateReservationException;
import roomescape.global.exception.ReservationTimeNotFoundException;
import roomescape.global.exception.ThemeNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public List<ReservationResponse> getReservations(int page, int size) {
        return reservationRepository.findAll(page, size).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(ReservationTimeNotFoundException::new);
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(ThemeNotFoundException::new);
        validateReservationAvailable(request.date(), time, theme);

        Reservation reservation = reservationRepository.save(
                Reservation.createNew(request.name(), request.date(), time, theme));

        return ReservationResponse.from(reservation);
    }

    private void validateReservationAvailable(LocalDate date, ReservationTime time, Theme theme) {
        List<Long> reservedTimeIds = reservationTimeRepository.findReservedTimeIds(theme.getId(), date);
        if(reservedTimeIds.contains(time.getId())) {
            throw new DuplicateReservationException();
        }
    }

    @Transactional
    public void cancelReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
