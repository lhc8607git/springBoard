package org.zerock.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {

	private BoardService service;

	@GetMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public void register() {}

	@GetMapping("/list")
	public void list(Criteria cri, Model model) {

		//log.info("■■list: " + cri);
		log.info("■■list 요청 들어옴");

		model.addAttribute("list", service.getList(cri));  

		int total = service.getTotal(cri);
		log.info("■■total: " + total);

		model.addAttribute("pageMaker", new PageDTO(cri, total));
	}

	@PostMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public String register(BoardVO board, RedirectAttributes rttr) {
		log.info("■■등록 한 거 - " + board);
		//log.info("■■register 요청 왔으 " + board);

		if (board.getAttachList() != null) {
			board.getAttachList().forEach(attach -> log.info(attach));
		}

		service.register(board);

		rttr.addFlashAttribute("result", board.getBno());

		return "redirect:/board/list";
	}

	@GetMapping({ "/get", "/modify" })
	public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, Model model) {

		log.info("■■/get , /modify 요청 들어옴");
		model.addAttribute("board", service.get(bno));
	}

	@PreAuthorize("principal.username == #board.writer")
	@PostMapping("/modify")
	public String modify(BoardVO board, Criteria cri, RedirectAttributes rttr) {
		//log.info("■■수정 한 거 - " + board);
		log.info("■■modify 요청 들어옴");

		if (service.modify(board)) {
			rttr.addFlashAttribute("result", "success");
		}

		return "redirect:/board/list" + cri.getListLink();
	}
	
	@PreAuthorize("principal.username == #writer")
	@PostMapping("/remove")
	public String remove(@RequestParam("bno") Long bno, Criteria cri, RedirectAttributes rttr, String writer) {

		log.info("■■삭제 한 거 - " + bno);
		// log.info("■■remove 요청 들어옴");

		// 삭제 전에 먼저 해당 게시물의 첨부파일 목록을 확보
		List<BoardAttachVO> attachList = service.getAttachList(bno);

		if (service.remove(bno)) {
			deleteFiles(attachList); // 삭제

			rttr.addFlashAttribute("result", "success");
		}
		return "redirect:/board/list" + cri.getListLink();
	}


	private void deleteFiles(List<BoardAttachVO> attachList) {

		if (attachList == null || attachList.size() == 0) {
			return;
		}

		log.info("■■삭제 파일들");
		log.info(attachList);

		attachList.forEach(attach -> {
			try {
				Path file = Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\" + attach.getUuid() + "_" + attach.getFileName());
				Files.deleteIfExists(file);

				if (Files.probeContentType(file).startsWith("image")) {
					Path thumbNail = Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\s_" + attach.getUuid() + "_" + attach.getFileName());
					Files.delete(thumbNail);
				}

			} catch (Exception e) {
				log.error("delete file error" + e.getMessage());
			} 
		});
	}


	@GetMapping(value = "/getAttachList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno) {

		log.info("■■getAttachList 요청 들어옴" + bno);

		return new ResponseEntity<>(service.getAttachList(bno), HttpStatus.OK);

	}

}
