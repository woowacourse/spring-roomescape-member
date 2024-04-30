package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.AvailableTimeResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class AvailableTimeService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public AvailableTimeService(ReservationRepository reservationRepository,
                                ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    //todo : 메서드 개선
    public List<AvailableTimeResponse> findByThemeAndDate(LocalDate date, long themeId) {
        Set<Long> alreadyReservedTimeIds = reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getDate().equals(date))
                //todo Reservation에 메서드 구현하기
                .filter(reservation -> reservation.getTheme().getId().equals(themeId))
                .map(Reservation::getReservationTime)
                .map(ReservationTime::getId)
                .collect(Collectors.toSet());

        return reservationTimeRepository.findAll().stream()
                .map(reservationTime -> {
                    long id = reservationTime.getId();
                    boolean isBooked = alreadyReservedTimeIds.contains(id);
                    return new AvailableTimeResponse(id, reservationTime.getStartAt(), isBooked);
                })
                .toList();
    }
}
