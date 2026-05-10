package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.AvailableTimeFindRequest;
import roomescape.controller.dto.ReservationTimeCreateRequest;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {
    private static final String TIME_SLOT_DOES_NOT_EXIST = "조회된 타임 슬롯이 없습니다.";
    public static final String INVALID_TIME_ID = "요청한 시간을 찾을 수 없습니다";
    private static final String DATE_SHOULD_NOT_BE_PAST = "기준 날짜는 과거일 수 없습니다.";

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional
    public ReservationTime create(ReservationTimeCreateRequest request) {
        ReservationTime reservationTime = ReservationTime.of(request.getStartAt());
        return reservationTimeRepository.save(reservationTime);
    }

    public ReservationTime find(long reservationTimeId) {
        return reservationTimeRepository.findById(reservationTimeId)
                .orElseThrow(() -> new IllegalArgumentException(TIME_SLOT_DOES_NOT_EXIST));
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    public List<ReservationTime> findAvailable(AvailableTimeFindRequest request) {
        LocalDate now = LocalDate.now();
        if (now.isAfter(request.getDate())) {
            throw new IllegalArgumentException(DATE_SHOULD_NOT_BE_PAST);
        }

        return reservationTimeRepository.findByDateAndTheme(request.getDate(), request.getThemeId());
    }

    @Transactional
    public void delete(long reservationTimeId) {
        reservationTimeRepository.findById(reservationTimeId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_TIME_ID));

        reservationTimeRepository.delete(reservationTimeId);
    }
}
