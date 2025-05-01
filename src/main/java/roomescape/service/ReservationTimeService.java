package roomescape.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.request.CreateReservationTimeServiceRequest;
import roomescape.service.dto.response.ReservationTimeServiceResponse;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeServiceResponse create(CreateReservationTimeServiceRequest command) {
        ReservationTime reservationTime = reservationTimeRepository.save(command.toReservationTime());
        return ReservationTimeServiceResponse.withoutBook(reservationTime);
    }

    public List<ReservationTimeServiceResponse> getAll(Long themeId, LocalDate date) {
        List<ReservationTime> allTimes = reservationTimeRepository.getAll();
        List<ReservationTime> bookedTimes = reservationTimeRepository.getAllByThemeIdAndDate(themeId, date);

        return allTimes.stream()
                .map(time -> ReservationTimeServiceResponse.of(time, bookedTimes.contains(time)))
                .toList();
    }

    public void delete(Long id) {
        // TODO: noContent vs IllegalArgumentException
        ReservationTime reservation = reservationTimeRepository.getById(id);
        reservationTimeRepository.remove(reservation);
    }
}
