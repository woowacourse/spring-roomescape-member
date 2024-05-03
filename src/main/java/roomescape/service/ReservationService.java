package roomescape.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.BookableTimeResponse;
import roomescape.dto.BookableTimesRequest;
import roomescape.dto.ReservationAddRequest;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> findAllReservation() {
        return reservationRepository.findAll();
    }

    public Reservation addReservation(ReservationAddRequest reservationAddRequest) {
        if (reservationRepository.existByDateAndTimeIdAndThemeId(reservationAddRequest.getDate(),
                reservationAddRequest.getTimeId(), reservationAddRequest.getThemeId())) {
            throw new IllegalArgumentException("예약 날짜와 예약시간 그리고 테마가 겹치는 예약은 할 수 없습니다.");
        }

        ReservationTime reservationTime = getReservationTime(reservationAddRequest.getTimeId());
        Theme theme = getTheme(reservationAddRequest);

        Reservation reservationRequest = reservationAddRequest.toEntity(reservationTime, theme);
        return reservationRepository.insert(reservationRequest);
    }

    private Theme getTheme(ReservationAddRequest reservationAddRequest) {
        return themeRepository.findById(reservationAddRequest.getThemeId())
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 테마로 예약할 수 없습니다"));
    }

    private ReservationTime getReservationTime(Long reservationTimeId) {
        return reservationTimeRepository.findById(reservationTimeId)
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 예약시각으로 예약할 수 없습니다."));
    }

    public void removeReservation(Long id) {
        if (reservationRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("해당 id를 가진 예약이 존재하지 않습니다.");
        }
        reservationRepository.deleteById(id);
    }

    public List<BookableTimeResponse> findBookableTimes(BookableTimesRequest bookableTimesRequest) {
        List<ReservationTime> bookedTimes = reservationRepository.findByDateAndTheme(bookableTimesRequest.getDate(),
                bookableTimesRequest.getThemeId());
        List<ReservationTime> allTimes = reservationTimeRepository.findAll();
        List<BookableTimeResponse> bookableTimeResponses = new ArrayList<>();
        for (ReservationTime allTime : allTimes) {
            if (!bookedTimes.contains(allTime)) {
                bookableTimeResponses.add(new BookableTimeResponse(allTime.getStartAt(), allTime.getId(), false));
            }
        }
        List<BookableTimeResponse> bookedTimeResponse = bookedTimes.stream()
                .map((reservationTime -> new BookableTimeResponse(reservationTime.getStartAt(), reservationTime.getId(),
                        true)))
                .toList();
        bookableTimeResponses.addAll(bookedTimeResponse);
        return bookableTimeResponses;
    }

    public List<Theme> getThemeRanking() {
        return reservationRepository.findThemeOrderByReservationCount();
    }
}
