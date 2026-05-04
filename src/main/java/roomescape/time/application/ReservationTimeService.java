package roomescape.time.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.validator.ReservationTimeValidator;
import roomescape.time.domain.ReservationTimeRepository;
import roomescape.time.presentation.dto.AvailableReservationTimeRequest;
import roomescape.time.presentation.dto.AvailableReservationTimeResponse;
import roomescape.time.presentation.dto.ReservationTimeRequest;
import roomescape.time.presentation.dto.ReservationTimeResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeValidator reservationTimeValidator;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    @Transactional(readOnly = true)
    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse addReservationTime(ReservationTimeRequest request) {
        ReservationTime time = ReservationTimeRequest.toEntity(request);
        return ReservationTimeResponse.from(reservationTimeRepository.save(time));
    }

    public void deleteReservationTime(Long id) {
        reservationTimeValidator.validateDeletable(id);
        reservationTimeRepository.deleteById(id);
    }

    public AvailableReservationTimeResponse getAvailableReservationTime(AvailableReservationTimeRequest request) {
        Theme theme = themeRepository.getById(request.themeId());
        List<Reservation> reservations = reservationRepository.findByThemeAndDate(
                request.themeId(), request.date());
        List<ReservationTime> allTimes = reservationTimeRepository.findAll();
        Set<ReservationTime> reservedTimes = reservations.stream()
                .map(Reservation::getTime)
                .collect(Collectors.toSet());
        List<ReservationTime> availableTime = allTimes.stream()
                .filter(time -> !reservedTimes.contains(time))
                .toList();
        return AvailableReservationTimeResponse.from(theme, availableTime);
    }
}
