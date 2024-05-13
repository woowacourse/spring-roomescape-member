package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationRepository;
import roomescape.dao.ReservationTimeRepository;
import roomescape.domain.ReservationTime;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.ReservationTimeCreateRequest;
import roomescape.service.dto.ReservationTimeReadRequest;
import roomescape.service.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(final ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse create(final ReservationTimeCreateRequest reservationTimeCreateRequest) {
        ReservationTime reservationTime = reservationTimeRepository.save(
                new ReservationTime(reservationTimeCreateRequest.startAt()));
        return new ReservationTimeResponse(reservationTime);
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public void deleteById(final long id) {
        validateByReservation(id);
        reservationTimeRepository.deleteById(id);
    }

    private void validateByReservation(long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new InvalidReservationException("해당 시간에 예약이 존재해서 삭제할 수 없습니다.");
        }
    }

    public List<ReservationTimeResponse> findAvailableTimes(ReservationTimeReadRequest reservationTimeReadRequest) {
        return reservationTimeRepository.getReferenceByDateAndTheme(reservationTimeReadRequest.date(),
                        reservationTimeReadRequest.themeId()).stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }
}
