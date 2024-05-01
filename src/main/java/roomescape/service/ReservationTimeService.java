package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.request.ReservationTimeRequest;
import roomescape.controller.response.AvailableReservationTimeResponse;
import roomescape.controller.response.ReservationTimeResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTimeResponse> findAll() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<AvailableReservationTimeResponse> findAllByAvailability(LocalDate date, long themeId) {
        List<ReservationTime> savedReservationTimesAll = reservationTimeRepository.findAll();

        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);
        List<ReservationTime> alreadyBookedReservationTimes = reservations.stream()
                .map(Reservation::getTime)
                .toList();

        return savedReservationTimesAll.stream()
                .map(time -> AvailableReservationTimeResponse.from(time, alreadyBookedReservationTimes.contains(time)))
                .toList();
    }

    public ReservationTimeResponse save(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRequest.toEntity();

        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResponse.from(savedReservationTime);
    }

    public void deleteById(Long id) {
        reservationTimeRepository.deleteById(id);
    }
}
