package com.ex00.contoller;

import com.ex00.dto.upload.UploadFileDTO;
import com.ex00.dto.upload.UploadResultDTO;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
public class UpDownController {

    @Value("${com.ex00.upload.path}") // import 시에 springframework으로 시작하는 Value
    private String uploadPath;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO){
        if(uploadFileDTO.getFiles() != null){
            final List<UploadResultDTO> list = new ArrayList<>();

            uploadFileDTO.getFiles().forEach(multipartFile -> {
                String originName = multipartFile.getOriginalFilename();
                System.out.println(originName);

                String uuid = UUID.randomUUID().toString();

                Path savePath = Paths.get(uploadPath, uuid+"_"+originName);

                boolean image = false;

                try {
                    multipartFile.transferTo(savePath);

                    // 이미지 파일의 종류라면
                    if(Files.probeContentType(savePath).startsWith("image")){
                        image = true;

                        File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originName);

                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

                list.add(UploadResultDTO.builder()
                        .uuid(uuid)
                        .fileName(originName)
                        .img(image).build());
            });
            return list;
        }

        return null;
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable("fileName") String fileName){
        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);

        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @DeleteMapping("/remove/{fileName}")
    public Map<String, Boolean> removeFile(@PathVariable("fileName") String fileName){
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);

        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            // 섬네일이 존재한다면
            if(contentType.startsWith("image")){
                File thumbnailFile = new File(uploadPath+File.separator+ "s_"+ fileName);
                thumbnailFile.delete();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        resultMap.put("result", removed);

        return resultMap;
    }
}
