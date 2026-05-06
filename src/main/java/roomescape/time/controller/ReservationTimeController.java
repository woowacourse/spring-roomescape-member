package roomescape.time.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.dto.response.ReservationTimeDetailDto;

@RestController
@RequestMapping("/member")
public class ReservationTimeController {
    // TODO: 예약가능 시간 조회 엔드포인트 추가 (사용자)
    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeDetailDto>> read(@RequestParam("date") String date, @RequestParam("themeId") Long themeId){
        return null;
    }
}
