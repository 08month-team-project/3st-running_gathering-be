package com.runto.domain.image.application;

import com.runto.domain.image.exception.ImageException;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import org.springframework.stereotype.Service;

import java.io.File;

import static com.runto.global.exception.ErrorCode.IMAGE_CONVERSION_FAILED;

@Service
public class ImageOptimizeService {

    // 저장 경로는 수정될 여지가 많음
    private final String filePath = "/images/";

    public File convertToWebp(String fileName, File originalFile) {
        try {
            return ImmutableImage.loader()// 라이브러리 객체 생성
                    .fromFile(originalFile) // 서버에 저장돼있는 이미지 파일 가져옴
                    .output(WebpWriter.DEFAULT,  // 손실 압축 설정, fileName.webp로 파일 생성
                            new File(System.getProperty("user.dir") + filePath + fileName + ".webp"));
        } catch (Exception e) {
            throw new ImageException(e, IMAGE_CONVERSION_FAILED);
        }
    }


//    public File convertToWebpWithLossless(String fileName, File originalFile) {
//        try {
//            return ImmutableImage.loader()// 라이브러리 객체 생성
//                    .fromFile(originalFile) // 서버에 저장돼있는 이미지 파일 가져옴
//                    .output(WebpWriter.DEFAULT.withLossless(),  // 무손실 압축 설정, fileName.webp로 파일 생성
//                            new File(System.getProperty("user.dir") + filePath + fileName + ".webp"));
//        } catch (Exception e) {
//            throw new ImageException(e, IMAGE_CONVERSION_FAILED);
//        }
//    }
}
