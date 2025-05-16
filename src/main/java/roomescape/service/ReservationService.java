package roomescape.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.UserReservationRequest;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.model.user.Member;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservedChecker;

@Service
public class ReservationService {
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;
    private final ReservationRepository reservationRepository;
    private final ReservedChecker reservedChecker;

    public ReservationService(ReservationTimeService reservationTimeService, ThemeService themeService,
                              ReservationRepository reservationRepository, ReservedChecker reservedChecker) {
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
        this.reservationRepository = reservationRepository;
        this.reservedChecker = reservedChecker;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.getReservations(null, null, null, null);
    }

    public Reservation addReservation(Member member, UserReservationRequest userReservationRequest) {
        ReservationTime reservationTime = reservationTimeService.getReservationTimeById(
                userReservationRequest.timeId());
        Theme theme = themeService.getThemeById(userReservationRequest.themeId());
        Reservation reservationWithNoId = Reservation.createWithNoId(member, userReservationRequest, reservationTime,
                theme);

        validateUniqueReservation(userReservationRequest.date(), userReservationRequest.timeId(),
                userReservationRequest.themeId());
        return reservationRepository.addReservation(reservationWithNoId);
    }

    private void validateUniqueReservation(LocalDate reservationDate, Long timeId, Long themeId) {
        if (reservedChecker.contains(reservationDate, timeId, themeId)) {
            throw new IllegalArgumentException("Reservation already exists");
        }
    }

    public void deleteReservation(long id) {
        reservationRepository.deleteReservation(id);
    }

    public List<Reservation> getFilteredReservation(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        List<Reservation> allReservations = reservationRepository.getReservations(themeId, memberId, dateFrom, dateTo);
        List<Reservation> filteredReservation = new ArrayList<>(allReservations);
        if (themeId != null) {
            filteredReservation = allReservations.stream().filter(r -> r.getTheme().getId().equals(themeId)).toList();
        }
        if (memberId != null) {
            filteredReservation = filteredReservation.stream().filter(r -> r.getMember().getId().equals(memberId))
                    .toList();
        }
        if (dateFrom != null) {
            filteredReservation = filteredReservation.stream()
                    .filter(r -> !r.getReservationDateTime().getDate().isBefore(dateFrom)).toList();
        }
        if (dateTo != null) {
            filteredReservation = filteredReservation.stream()
                    .filter(r -> !r.getReservationDateTime().getDate().isAfter(dateTo)).toList();
        }
        return filteredReservation;
    }
}
