package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardMapper {
	

	public List<BoardVO> getList();
	
	//게시글의 페이지 번호와 읽어올 개수에 따라 게시글을 조회하는 함수
	public List<BoardVO> getListWithPaging(Criteria cri);
	
	public void insert(BoardVO board);

	//데이터 생성 후 PRIMARY KEY를 알아야 할 떄
	public Integer insertSelectKey(BoardVO board);
	
	public BoardVO read(Long bno);
	
	//삭제 된 Row가 있으면 1, 아니면 0을 반환
	public int delete(Long bno);

	// 업데이트 된 Row 가 있으면 1, 아니면 0 을 반환
	public int update(BoardVO board);

	public int getTotalCount(Criteria cri);	
	
	public void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);
	
}
