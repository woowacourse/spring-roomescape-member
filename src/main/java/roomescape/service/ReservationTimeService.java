package roomescape.service;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.entity.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(final ReservationTimeRepository reservationTimeRepository,
                                  final ReservationRepository reservationRepository) {

        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse createReservationTime(final ReservationTimeRequest reservationTimeRequest) {
        LocalTime createTime = reservationTimeRequest.startAtToLocalTime();
        validateTime(createTime);
        ReservationTime reservationTime = ReservationTime.beforeSave(createTime);
        ReservationTime createdReservationTime = reservationTimeRepository.save(reservationTime);
        return ReservationTimeResponse.from(createdReservationTime);
    }

    private void validateTime(LocalTime createTime) {
        validateNoDuplication(createTime);
        validateTimeInterval(createTime);
    }

    private void validateNoDuplication(LocalTime createTime) {
        boolean isExist = reservationTimeRepository.existByTime(createTime);
        if (isExist) {
            throw new IllegalArgumentException("중복된 시간은 추가할 수 없습니다.");
        }
    }

    private void validateTimeInterval(LocalTime createTime) {
        boolean hasLess30MinDifference = reservationTimeRepository.findAll().stream()
                .anyMatch(reservationTime -> reservationTime.isInTimeInterval(createTime));
        if (hasLess30MinDifference) {
            throw new IllegalArgumentException("예약 시간은 서로 30분 이상 차이가 나야 합니다.");
        }
    }

    public List<ReservationTimeResponse> getAllReservationTime() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return ReservationTimeResponse.from(reservationTimes);
    }

    public boolean delete(Long id) {
        boolean isReservationExistInTime = reservationRepository.existByTimeId(id);
        if (isReservationExistInTime) {
            throw new IllegalArgumentException("예약이 존재하는 예약 시간은 삭제할 수 없습니다.");
        }
        return reservationTimeRepository.deleteById(id);
    }

    public ReservationTimeResponse getReservationTime(@NotNull Long timeId) {
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId);
        return ReservationTimeResponse.from(reservationTime);
    }
}
