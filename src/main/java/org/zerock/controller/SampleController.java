package org.zerock.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;



@Log4j
@RequestMapping("/sample/*") 
@Controller
public class SampleController {          // 연습 코드

  @GetMapping("/all")
  public void doAll() {
    log.info("모두 접근");
  }
  
  @GetMapping("/member")
  public void doMember() {
    log.info("로그인 멤버 접근");
  }
  
  @GetMapping("/admin")
  public void doAdmin() {
    log.info("관리자 접근");
  }  
  
  
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
  @GetMapping("/annoMember")
  public void doMember2() {
    log.info("권한 준 멤버 로그인 접근");
  }
  
  
  @Secured({"ROLE_ADMIN"})
  @GetMapping("/annoAdmin")
  public void doAdmin2() {
    log.info("권한 준 관리자 접근");
  }
}
