package com.knuaf.chickenstock.controller;

import com.knuaf.chickenstock.dto.ResponseDto;
import com.knuaf.chickenstock.dto.SignInDto;
import com.knuaf.chickenstock.dto.SignUpDto;
import com.knuaf.chickenstock.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody SignUpDto signUpDto) throws Exception {

        ResponseDto responseDto = memberService.join(signUpDto);

        return ResponseEntity.ok(responseDto);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SignInDto signInDto) throws Exception {

        ResponseDto responseDto = memberService.login(signInDto);
        return ResponseEntity.ok(responseDto);
    }
}
