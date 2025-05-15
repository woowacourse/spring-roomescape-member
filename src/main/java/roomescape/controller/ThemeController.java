package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    @Autowired
    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<Theme> create(@RequestBody Theme theme) {
        Theme createdTheme = themeService.saveTheme(theme);
        URI location = URI.create("/themes/" + createdTheme.getId());
        return ResponseEntity.created(location).body(createdTheme);
    }

    @GetMapping
    public ResponseEntity<List<Theme>> readAll() {
        List<Theme> themes = themeService.readTheme();
        return ResponseEntity.ok(themes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }

    // [리뷰어님에게] 리스트를 생성하는 필터 조건을 파라미터로 전달할 수 있도록 설계함. (프론트엔드와의 협업 필요, 따라서 현재는 파라미터 고정)
    @GetMapping("/lists")
    public ResponseEntity<List<Theme>> readLists(
            @RequestParam(value = "order_type", required = false) String orderType,
            @RequestParam(value = "list_num", required = false) Long listNum
    ) {

        // TODO: 정렬 조건을 ENUM으로 관리하기.
        orderType = "popular_desc"; // 현재는 내림차순으로 고정
        listNum = 10L;
        List<Theme> listedTheme = themeService.readLists(orderType, listNum);
        return ResponseEntity.ok(listedTheme);
    }
}
