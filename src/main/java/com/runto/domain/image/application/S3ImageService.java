package com.runto.domain.image.application;

import com.runto.domain.image.dto.ImageDto;
import com.runto.domain.image.dto.ImageUrlDto;
import com.runto.domain.image.exception.ImageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.runto.global.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3ImageService {

    private static final List<String> SUPPORT_IMAGE_EXTENSION = List.of("jpg", "jpeg", "png", "bmp", "webp");
    private static final String TEMPORARY_STORE_PREFIX = "temp/";
    private static final String PERMANENT_STORE_PREFIX = "permanent/";
    private static final String FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/temp_images/";

    private final S3Client s3Client;
    private final ImageOptimizeService imageOptimizeService;


    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.cloud.aws.region.static}")
    private String region;


    public List<ImageUrlDto> uploadContentImages(List<ImageDto> images,
                                                 String imageNamePrefix) {

        log.info("모임글 이미지 등록 S3서비스 진입");
        if (images == null || images.size() < 1) {
            throw new ImageException(INVALID_FILE);
        }

        List<ImageUrlDto> imageUrls = new ArrayList<>();
        images.forEach(imageDto -> imageUrls
                .add(uploadImage(imageDto, imageNamePrefix)));

        return imageUrls;
    }


    private ImageUrlDto uploadImage(ImageDto imageDto, String imageNamePrefix) {

        File convertedFile = null;
        File optimizedFile = null;

        try {
            MultipartFile requestFile = imageDto.getMultipartFile();

            if (requestFile == null || !StringUtils.hasText(requestFile.getOriginalFilename())) {
                throw new ImageException(INVALID_FILE);
            }

            // 지원하는 이미지 확장자 파일인지 검증
            validateImageExtension(requestFile);

            // 고유의 이미지 이름 생성
            String imageName = createUniqueFileName(imageNamePrefix);

            // 파일 객체로 변환 & 서버에 임시저장
            convertedFile = convertToFile(imageName, requestFile);

            // 이미지 최적화 & 서버에 임시저장
            optimizedFile = convertToWebp(imageName, convertedFile);


            // s3에 webp형식으로 최적화한 이미지 업로드
            s3Client.putObject(getPutObjectRequest(optimizedFile),
                    RequestBody.fromFile(optimizedFile));

            // 실제로 업로드가 잘 됐는지 검증
            validateUpload(optimizedFile);

            //S3 URL과 , 요청에서 받아온 이미지 순서를 다시 넣어서 반환 (이건 아직 실제로 저장돼있는 url 이 아님)
            return new ImageUrlDto(getImageUrl(optimizedFile, PERMANENT_STORE_PREFIX), imageDto.getOrder());
            

        } catch (ImageException e) {
            throw new ImageException(e.getErrorCode());
        } catch (Exception e) { // 직접 의도하지 않은 에러 처리용
            throw new ImageException(e, IMAGE_SERVER_ERROR);

        } finally {
            deleteFile(optimizedFile);
            deleteFile(convertedFile);
        }
    }

    private PutObjectRequest getPutObjectRequest(File file) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(TEMPORARY_STORE_PREFIX + file.getName())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();
    }


    //s3의 temp/ 폴더에 있던 파일을 정식폴더에 복사한 후 삭제
    public void moveImageProcess(String imageUrl) {

        String imageName = extractImageName(imageUrl);

        validateUpload(TEMPORARY_STORE_PREFIX + imageName);

        copyS3ObjectToNewPath(imageName, TEMPORARY_STORE_PREFIX, PERMANENT_STORE_PREFIX);
        deleteS3ObjectFromImageName(imageName, TEMPORARY_STORE_PREFIX);
    }


    private String getImageUrl(File pressedFile, String prefix) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName, region, prefix + pressedFile.getName());
    }

    private String getImageUrl(String imageName, String prefix) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName, region, prefix + imageName);
    }

    public void validateUpload(File pressedFile) {

        // 업로드된 객체 확인 (headObject 사용)
        HeadObjectRequest headRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(TEMPORARY_STORE_PREFIX + pressedFile.getName())
                .build();

        HeadObjectResponse response = s3Client.headObject(headRequest);

        if (response == null) {
            throw new ImageException(S3_OBJECT_NOT_FOUND);
        }
    }

    public String createUniqueFileName(String imageNamePrefix) {
        return imageNamePrefix + "-" + UUID.randomUUID();
    }

    private File convertToFile(String uniqueName, MultipartFile multipartFile) throws IOException {

//        if (multipartFile == null || multipartFile.isEmpty()) {
//            throw new ImageException(INVALID_FILE);
//        }

        File file = new File(FILE_PATH + uniqueName + "." +
                extractExtension(Objects.requireNonNull(multipartFile.getOriginalFilename())));

        // 해당 경로의 폴더가 존재하지 않는다면 생성
        if (!file.exists()) {
            file.mkdirs();
        }
        multipartFile.transferTo(file); // multipartFile를 변환 후 file객체의 경로에 생성
        return file;
    }

    private File convertToWebp(String fileName, File originalFile) {
        return imageOptimizeService.convertToWebp(fileName, originalFile);
    }

    private void validateImageExtension(MultipartFile multipartFile) {

//        if (multipartFile == null || !StringUtils.hasText(multipartFile.getOriginalFilename())) {
//            throw new ImageException(INVALID_FILE);
//        }

        String extension = extractExtension(multipartFile.getOriginalFilename());
        if (!SUPPORT_IMAGE_EXTENSION.contains(extension)) {
            throw new ImageException(UNSUPPORTED_IMAGE_EXTENSION);
        }
    }

    private String extractExtension(String originalFilename) {
        return originalFilename
                .substring(originalFilename.indexOf(".") + 1);
    }

    private String extractImageName(String imageUrl) {

        String[] parts = imageUrl.split("/"); // URL에서 파일명 부분만 추출

        if (parts.length == 0) {
            return "";
        }

        return parts[parts.length - 1]; // 마지막 부분이 파일 이름
    }

    public void validateUpload(String imageName) {

        // 해당 url이 s3에 업로드가 돼있는 객체인지 확인 (headObject 사용)
        HeadObjectRequest headRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(imageName)
                .build();

        try {
            s3Client.headObject(headRequest);
        } catch (Exception e) {
            throw new ImageException(S3_OBJECT_NOT_FOUND);
        }
    }

    public void copyS3ObjectToNewPath(String imageName, String beforePath, String newPath) {

        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                .sourceBucket(bucketName)
                .sourceKey(beforePath + imageName)
                .destinationBucket(bucketName)
                .destinationKey(newPath + imageName)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3Client.copyObject(copyObjectRequest);
    }

    private void deleteS3ObjectFromImageName(String imageName, String prefixPath) {

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(prefixPath + imageName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            log.error("S3 이미지 삭제 실패 ={}", imageName);
        }
    }

    public void deleteS3Object(String imageUrl, String prefixPath) {

        if (!StringUtils.hasText(imageUrl)) return;

        String imageName = extractImageName(imageUrl);
        log.info("삭제 요청 이미지 이름={}", imageName);

        if (StringUtils.hasText(imageName)) {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(prefixPath + imageName)
                    .build();
            try {
                s3Client.deleteObject(deleteObjectRequest);
            } catch (Exception e) {
                log.error("S3 이미지 삭제 실패 ={}", imageName);
            }
        }

    }

    private void deleteFile(File file) {
        if (file != null && file.delete()) {
            log.info("File 삭제 ={}", file.getName());
        }
    }
}
