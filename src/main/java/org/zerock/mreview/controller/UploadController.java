package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mreview.dto.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for(MultipartFile uploadFile: uploadFiles) {

            if(uploadFile.getContentType().startsWith("image") == false) {
                log.warn("This file is not image type."); // warn == warning
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } // if end.

            String originalName = uploadFile.getOriginalFilename();
            // ex. C:\classData\workspace\ex.jpg -> 마지막 \을 찾음.
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1); // \\ 다음 위치 -> 실제 파일명 시작위치

            log.info("fileName >>>> "+ fileName);
            // 날짜 폴더 생성
            String folderPath = makeFolder();

            // UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름 중간 "_"를 이용하여 구분
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            Path savePath = Paths.get(saveName); // 파일을 저장할 위치 지정

            try {
                uploadFile.transferTo(savePath); // transferTo() : 업로드할 파일 데이터를 지정한 경로 파일에 저장.
                resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } // for end.
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    } // uploadFile()

    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/", File.separator);

        //Make Folder---
        File uploadPathFolder = new File(uploadPath, folderPath);

        if(uploadPathFolder.exists() == false) { // uploadPathFolder가 존재 X -> 새로운 Folder 생성
            uploadPathFolder.mkdirs(); // 폴더생성 함수 (디렉토리를 만드는데 사용)
        }
        return folderPath;
    } // makeFolder()
}
