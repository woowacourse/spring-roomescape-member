package roomescape.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.dto.request.CreateReservationTimeRequest;
import roomescape.dto.response.ReservationTimeResponse;
import lombok.RequiredArgsConstructor;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeResponse create(CreateReservationTimeRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.save(request.toReservationTime());
        return ReservationTimeResponse.withoutBook(reservationTime);
    }

    public List<ReservationTimeResponse> getAll() {
        return reservationTimeRepository.getAll().stream()
                .map(ReservationTimeResponse::withoutBook)
                .toList();
    }

    public List<ReservationTimeResponse> getAllByThemeIdAndDate(Long themeId, LocalDate date) {
        List<ReservationTime> allTimes = reservationTimeRepository.getAll();
        List<ReservationTime> bookedTimes = reservationTimeRepository.getAllByThemeIdAndDate(themeId, date);

        return allTimes.stream()
                .map(time -> ReservationTimeResponse.of(time, bookedTimes.contains(time)))
                .toList();
    }

    public void delete(Long id) {
        reservationTimeRepository.findById(id)
                .ifPresent(reservationTimeRepository::remove);
    }
}
