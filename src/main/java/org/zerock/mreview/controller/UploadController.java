package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mreview.dto.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
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

    /*
    * 업로드한 파일을 savaName으로 변경하여 저장
    * 지정된 경로(makeFolder)에 해당 파일을 저장
    **/
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
            // "\\" -> escape 문자, 하나만 사용시 인식을 못하기 때문에 2개 사용
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
                // 원본 파일 저장
                uploadFile.transferTo(savePath); // transferTo() : 업로드할 파일 데이터를 지정한 경로 파일에 저장.
                // Thumbnail 생성 1번
                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + fileName; // 파일 이름 중간에 s_ 추가
                File thumbnailFile = new File(thumbnailSaveName);

                // Thumbnail 생성 2번
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile,100,100);

                resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } // for end.
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    } // uploadFile()

    /*
    *  View에 업로드한 이미지 파일 출력
    */
    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {

        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("fileName >>>> " + fileName);
            File file = new File(uploadPath + File.separator + srcFileName);
            log.info("file >>>> " + file);

            HttpHeaders header = new HttpHeaders();
            // MIME 타입 처리
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            // 파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } // try ~ catch

        return result;
    } // getFile()

    /*
    *  이미지를 업로드하면 해당 파일을 년/월/일 단위로 나눌 폴더 생성
    **/
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

    /*
    *  업로드 파일 삭제
    **/
    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName) {
        String srcFileName = null;
        boolean result = false;
        try {
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(uploadPath + File.separator + srcFileName);
            result = file.delete(); // File 클래스의 delete()

            File thumbnail = new File(file.getParent(), "s_" + file.getName());

            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } // removeFile()
}
