package roomescape.reservationtime.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.request.ReservationTimeCreateRequest;
import roomescape.reservationtime.dto.response.AvailableReservationTimeResponse;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;
import roomescape.reservationtime.exception.ReservationTimeAlreadyExistsException;
import roomescape.reservationtime.exception.ReservationTimeInUseException;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(final ReservationTimeRepository reservationTimeRepository,
                                  final ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void delete(long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new ReservationTimeInUseException("해당 시간에 대한 예약이 존재하여 삭제할 수 없습니다.");
        }
        if (!reservationTimeRepository.deleteById(id)) {
            throw new ReservationTimeNotFoundException("요청한 id와 일치하는 예약 시간 정보가 없습니다.");
        }
    }

    public ReservationTimeResponse create(final ReservationTimeCreateRequest request) {
        validateIsTimeUnique(request);
        ReservationTime newReservationTime = reservationTimeRepository.save(request.toReservationTime());
        return ReservationTimeResponse.from(newReservationTime);
    }

    private void validateIsTimeUnique(final ReservationTimeCreateRequest request) {
        if (reservationTimeRepository.checkExistsByStartAt(request.startAt())) {
            throw new ReservationTimeAlreadyExistsException("중복된 예약 시간을 생성할 수 없습니다");
        }
    }

    public List<AvailableReservationTimeResponse> getAvailableReservationTimes(final LocalDate date,
                                                                               final Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);
        Map<ReservationTime, Boolean> availabilities = new HashMap<>();

        for (ReservationTime reservationTime : reservationTimes) {
            Long timeId = reservationTime.getId();
            availabilities.put(reservationTime, false);

            for (Reservation reservation : reservations) {
                if (Objects.equals(timeId, reservation.getTime().getId())) {
                    availabilities.put(reservationTime, true);
                }
            }
        }
        return availabilities.entrySet().stream()
                .map(entry -> {
                    ReservationTime reservationTime = entry.getKey();
                    return new AvailableReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt(),
                            entry.getValue());
                })
                .toList();
    }
}
