package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.DuplicateReservationException;
import roomescape.domain.Reservation;
import roomescape.controller.dto.CreateReservationRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.repository.ReservationRepository;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public List<ReservationResponse> findAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse reserve(CreateReservationRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId());
        Theme theme = themeRepository.findById(request.themeId());
        validateReservationAvailable(request.date(), time, theme);

        Reservation reservation = reservationRepository.save(
                new Reservation(null, request.name(), request.date(), time, theme));

        return ReservationResponse.from(reservation);
    }

    private void validateReservationAvailable(LocalDate date, ReservationTime time, Theme theme) {
        List<Long> reservedIds = reservationTimeRepository.findIdByCondition(theme.getId(), date);
        if(reservedIds.contains(time.getId())) {
            throw new DuplicateReservationException();
        }
    }

    @Transactional
    public void cancelReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
