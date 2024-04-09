package com.ex00.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadResultDTO {

    private String uuid;

    private String fileName;

    private boolean img; // 이미지 파일인지 확인

    public String getLink(){
        if(img){
            return "s_" + uuid + "_" + fileName; // 이미지인 경우 섬네일
        }else {
            return uuid + "_" + fileName;
        }
    }
}
