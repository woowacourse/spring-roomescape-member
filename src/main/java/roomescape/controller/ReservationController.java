package roomescape.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.annotation.Auth;
import roomescape.domain.member.Member;
import roomescape.dto.request.CreateReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createByAuth(@Auth Member member, @RequestBody CreateReservationRequest request) {
        CreateReservationRequest authoredRequest = CreateReservationRequest.setMember(request, member.getId());
        ReservationResponse response = reservationService.saveReservation(authoredRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // TODO: 관리자 전용 API - 분리 필요
    @PostMapping("admin/reservations")
    public ResponseEntity<ReservationResponse> createByMemberId(@RequestBody CreateReservationRequest request) {
        ReservationResponse response = reservationService.saveReservation(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // TODO: 관리자 전용 API - 분리 필요
    @GetMapping("admin/reservations/lists")
    public ResponseEntity<List<ReservationResponse>> readWithFilter(
            @RequestParam(required = false) Long themeId,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo
    ) {

        // TODO: 필터 조건을 ENUM 으로 관리하기.
        // ? 필터 조건이 추가될때마다 Controller -> Service -> Repository 의 파라미터가 늘어나는 걸 막고 싶었다.
        // ? 따라서 Map 으로 받도록 했는데, 파라미터가 계속 변경되는 문제는 해결했지만 결국엔 필터 조건 추가마다 Controller 와 Repository 를 수정해야 한다.
        // ? 더 좋은 해결법이 없을까? RequestParam 을 한번에 받아서, 필터 정보를 표현하는 객체로 변환하는 방법은 어떨까?

        Map<String, Object> filter = new HashMap<>();
        if (themeId != null) filter.put("themeId", themeId);
        if (memberId != null) filter.put("memberId", memberId);
        if (dateFrom != null) filter.put("dateFrom", dateFrom);
        if (dateTo != null) filter.put("dateTo", dateTo);

        List<ReservationResponse> responses = reservationService.readAllWithFilter(filter);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> readAll() {
        List<ReservationResponse> responses = reservationService.readReservations();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
