package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservedTimes;
import roomescape.service.dto.CreateReservationCommand;
import roomescape.controller.dto.ReservationPagingQuery;
import roomescape.controller.dto.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
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

    public List<ReservationResponse> getReservations(ReservationPagingQuery query) {
        return reservationRepository.findAll(query.size(), query.offset()).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse createReservation(CreateReservationCommand command) {
        ReservationTime time = reservationTimeRepository.findById(command.timeId())
                .orElseThrow(ReservationTimeNotFoundException::new);
        Theme theme = themeRepository.findById(command.themeId())
                .orElseThrow(ThemeNotFoundException::new);
        ReservedTimes reservedTimes = new ReservedTimes(reservationTimeRepository.findReservedTimeIds(
                theme.getId(),
                command.date()
        ));
        reservedTimes.validateAvailable(time.getId());

        Reservation reservation = reservationRepository.save(
                Reservation.createNew(command.name(), command.date(), time, theme));

        return ReservationResponse.from(reservation);
    }

    @Transactional
    public void cancelReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
