package org.zerock.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zerock.domain.BoardAttachVO;
import org.zerock.mapper.BoardAttachMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class FileCheckTask {

	@Setter(onMethod_ = { @Autowired })
	private BoardAttachMapper attachMapper;

	private String getFolderYesterDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);

		String str = sdf.format(cal.getTime());

		return str.replace("-", File.separator);
	}

	@Scheduled(cron = "0 0 3 * * *")
	public void checkFiles() throws Exception {

		log.warn("■■ File 체크" + new Date());

		// DB에 저장된 파일 리스트
		List<BoardAttachVO> fileList = attachMapper.getOldFiles();


		// 비교 기준 파일 리스트(Path객체)
		// List<Path> checkFilePath = new ArrayList<Path>();

		// 데이터베이스 파일 목록에 있는 디렉토리에서 파일을 확인
		// 원본 이미지
		List<Path> fileListPaths = fileList.stream()
				.map(vo -> Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName()))
				.collect(Collectors.toList());
		//fileList.forEach(vo -> {
		//	Path path = Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName());
		//	checkFilePath.add(path);
		//});		


		// 썸네일 이미지
		fileList.stream().filter(vo -> vo.isFileType() == true)
				.map(vo -> Paths.get("C:\\upload", vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName()))
				.forEach(p -> fileListPaths.add(p));
		//fileList.forEach(vo -> {
		//	Path path = Paths.get("C:\\upload", vo.getUploadPath(), "s_" +  vo.getUuid() + "_" + vo.getFileName());
		//	checkFilePath.add(path);
		//});


		// 디렉토리 파일 리스트
		File targetDir = Paths.get("C:\\upload", getFolderYesterDay()).toFile();
		File[] removeFiles = targetDir.listFiles();

		// 삭제 대상 파일 리스트(분류)
		List<File> removeFileList = new ArrayList<File>(Arrays.asList(targetFile));		
		for(File file : targetFile){
			checkFilePath.forEach(checkFile ->{
				if(file.toPath().equals(checkFile)) 
					removeFileList.remove(file);	
			});
		}


		// 삭제 대상 파일 제거
		log.warn("-----------------------------------------");
		for (File file : removeFiles) {
			log.warn(file);
			file.delete();
		}

		log.warn("===========================================");
	}
}
