package org.zerock.domain;

import lombok.Data;

@Data
public class BoardAttachVO {

  private String uuid;			// UUID가 포함된 이름을 PK로 하는 칼럼
  private String uploadPath;	               // 실제 파일이 업로드된 경로
  private String fileName;		// 파일 이름
  private boolean fileType;		// 이미지 파일 여부
  
  private Long bno;		// 게시물 번호
  
}
