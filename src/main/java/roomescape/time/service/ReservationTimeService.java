package roomescape.time.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.time.controller.dto.ReservationTimeRequest;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional
    public ReservationTime save(ReservationTimeRequest request) {
        if (reservationTimeRepository.existsByStartAt(request.startAt())) {
            throw new IllegalArgumentException("해당 시간이 이미 존재합니다.");
        }

        ReservationTime reservationTime = ReservationTime.create(request.startAt());
        return reservationTimeRepository.save(reservationTime);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    public List<ReservationTime> findAvailableTimes(Long themeId, LocalDate date) {
        return reservationTimeRepository.findAvailableTimes(themeId, date);
    }

    public ReservationTime getById(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 예약 시간이 존재하지 않습니다. ID: " + id));
    }
}
