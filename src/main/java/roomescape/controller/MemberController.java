package roomescape.controller;


import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.dto.SignUpRequest;
import roomescape.service.MemberService;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAll(){
        return ResponseEntity.ok(memberService.findAll());
    }


    @PostMapping
    public ResponseEntity<Object> create(@RequestBody SignUpRequest signUpRequest) {
        Long id = memberService.addUser(signUpRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }
}
