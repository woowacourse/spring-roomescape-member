package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import roomescape.controller.request.ReservationTimeRequest;
import roomescape.controller.response.IsReservedTimeResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.DuplicatedException;
import roomescape.exception.NotFoundException;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }


    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeRepository.findAllReservationTimes();
    }

    public ReservationTime addReservationTime(ReservationTimeRequest request) {
        LocalTime startAt = request.getStartAt();
        validateExistTime(startAt);
        ReservationTime reservationTime = new ReservationTime(startAt);
        return reservationTimeRepository.addReservationTime(reservationTime);
    }

    private void validateExistTime(LocalTime startAt) {
        Long countReservationTimeByStartAt = reservationTimeRepository.countReservationTimeByStartAt(startAt);
        if (countReservationTimeByStartAt == null || countReservationTimeByStartAt > 0) {
            throw new DuplicatedException("이미 존재하는 시간입니다.");
        }
    }

    public ReservationTime findReservationTime(long id) {
        return reservationTimeRepository.findReservationById(id);
    }

    // getReservationStatus
    public List<IsReservedTimeResponse> getIsReservedTime(LocalDate date, long themeId) {
        List<ReservationTime> allTimes = reservationTimeRepository.findAllReservationTimes();
        List<ReservationTime> bookedTimes = reservationTimeRepository.findAllReservedTimes(date, themeId);
        List<ReservationTime> notBookedTimes = filterNotBookedTimes(allTimes, bookedTimes);
        List<IsReservedTimeResponse> bookedResponse = mapToResponse(bookedTimes, true);
        List<IsReservedTimeResponse> notBookedResponse = mapToResponse(notBookedTimes, false);
        return concat(notBookedResponse, bookedResponse);
    }

    public void deleteReservationTime(long id) {
        validateNotExistReservationTime(id);
        validateReservedTime(id);
        reservationTimeRepository.deleteReservationTime(id);
    }

    private void validateReservedTime(long id) {
        Long countedReservationByTime = reservationTimeRepository.countReservedTime(id);
        if (countedReservationByTime == null || countedReservationByTime > 0) {
            throw new BadRequestException("해당 시간에 예약이 존재하여 삭제할 수 없습니다.");
        }
    }

    private void validateNotExistReservationTime(long id) {
        Long countedReservationTime = reservationTimeRepository.countReservationTime(id);
        if (countedReservationTime == null || countedReservationTime <= 0) {
            throw new NotFoundException("id(%s)에 해당하는 예약 시간이 존재하지 않습니다.".formatted(id));
        }
    }

    private List<ReservationTime> filterNotBookedTimes(List<ReservationTime> times, List<ReservationTime> bookedTimes) {
        return times.stream()
                .filter(time -> !bookedTimes.contains(time))
                .toList();
    }

    private List<IsReservedTimeResponse> mapToResponse(List<ReservationTime> times, boolean isBooked) {
        return times.stream()
                .map(time -> new IsReservedTimeResponse(time.getId(), time.getStartAt(), isBooked))
                .toList();
    }

    private List<IsReservedTimeResponse> concat(List<IsReservedTimeResponse> notBookedTimes,
                                                List<IsReservedTimeResponse> bookedTimes) {
        return Stream.concat(notBookedTimes.stream(), bookedTimes.stream()).toList();
    }
}
