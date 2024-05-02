package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.request.ReservationTimeRequest;
import roomescape.controller.response.AvailableReservationTimeResponse;
import roomescape.controller.response.ReservationTimeResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            ReservationTimeRepository reservationTimeRepository,
            ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTimeResponse> findAll() {
        List<ReservationTime> savedReservationTimes = reservationTimeRepository.findAll();

        return savedReservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<AvailableReservationTimeResponse> findAllWithAvailability(LocalDate date, long themeId) {
        List<ReservationTime> savedReservationTimes = reservationTimeRepository.findAll();
        List<ReservationTime> bookedReservationTimes = getBookedReservationTimes(date, themeId);

        return savedReservationTimes.stream()
                .map(time -> AvailableReservationTimeResponse.from(time, bookedReservationTimes.contains(time)))
                .toList();
    }

    private List<ReservationTime> getBookedReservationTimes(final LocalDate date, final long themeId) {
        List<Reservation> bookedReservations = reservationRepository.findByDateAndThemeId(date, themeId);
        return bookedReservations.stream()
                .map(Reservation::getTime)
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
