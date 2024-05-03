package roomescape.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.BookableTimeResponse;
import roomescape.dto.ReservationTimeAddRequest;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private ReservationTimeRepository reservationTimeRepository;

    ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findAllReservationTime() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime addReservationTime(ReservationTimeAddRequest reservationTimeAddRequest) {
        if (reservationTimeRepository.existByStartAt(reservationTimeAddRequest.getStartAt())) {
            throw new IllegalArgumentException("이미 존재하는 예약시간은 추가할 수 없습니다.");
        }
        return reservationTimeRepository.save(reservationTimeAddRequest.toEntity());
    }

    public void removeReservationTime(Long id) {
        if (reservationTimeRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("해당 id를 가진 예약시간이 존재하지 않습니다.");
        }
        reservationTimeRepository.deleteById(id);
    }

    public List<BookableTimeResponse> findReservationTimesWithReservationStatus(LocalDate date, Long themeId) {
        List<ReservationTime> reservedTimes = reservationTimeRepository.findByReserved(date, themeId);
        List<ReservationTime> allTimes = reservationTimeRepository.findAll();
        List<BookableTimeResponse> bookableTimeResponses = new ArrayList<>();
        for (ReservationTime allTime : allTimes) {
            if (!reservedTimes.contains(allTime)) {
                bookableTimeResponses.add(new BookableTimeResponse(allTime.getStartAt(), allTime.getId(), false));
            }
        }
        List<BookableTimeResponse> bookedTimeResponse = reservedTimes.stream()
                .map((reservationTime -> new BookableTimeResponse(reservationTime.getStartAt(), reservationTime.getId(),
                        true)))
                .toList();
        bookableTimeResponses.addAll(bookedTimeResponse);
        return bookableTimeResponses;
    }
}
