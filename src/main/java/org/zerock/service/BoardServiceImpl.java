package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
@AllArgsConstructor
//BoardService 인터페이스를 이용한 클래스. Mapper를 이용해 비즈니스 로직을 수행
public class BoardServiceImpl implements BoardService {

	@Setter(onMethod_ = @Autowired)
	private BoardMapper mapper;

	@Setter(onMethod_ = @Autowired)
	private BoardAttachMapper attachMapper;
	
	@Transactional
	@Override
	public void register(BoardVO board) {     
		// log.info("register : " + boardVO);
		mapper.insertSelectKey(board);
	}

	@Override
	public BoardVO get(Long bno) {
		log.info("get 요청");
		return mapper.read(bno);
	}

	@Transactional
	@Override
	public boolean modify(BoardVO board) {

		// log.info("modify : " +board);
		attachMapper.deleteAll(board.getBno());

		return modifyResult = mapper.update(board) == 1;  // 성공적으로 됏으면 1, 아니면 0 을 반환
	}

	@Transactional
	@Override
	public boolean remove(Long bno) {
		log.info("remove 요청 : " + bno);
		attachMapper.deleteAll(bno);

		return mapper.delete(bno) == 1;
	}

	@Override
	public List<BoardVO> getList(Criteria cri) {
		return mapper.getListWithPaging(cri);
	}
	
	
	@Override
	public int getTotal(Criteria cri) {
		return mapper.getTotalCount(cri);
	}

	@Override
	public List<BoardAttachVO> getAttachList(Long bno) {
		return attachMapper.findByBno(bno);
	}
	
	
	
}
