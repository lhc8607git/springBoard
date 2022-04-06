package org.zerock.mapper;

import java.util.List;

import org.zerock.domain.BoardAttachVO;

public interface BoardAttachMapper {
	// 첨부 파일 추가
	public void insert(BoardAttachVO vo);
	// 첨부 파일 삭제
	public void delete(String uuid);
	// 첨부 파일 찾기
	public List<BoardAttachVO> findByBno(Long bno);

	public void deleteAll(Long bno);
	// 첨부된 모든 파일의 목록
	public List<BoardAttachVO> getOldFiles();

}