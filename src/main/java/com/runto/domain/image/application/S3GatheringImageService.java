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
import java.util.UUID;

import static com.runto.global.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3GatheringImageService {

    private static final List<String> SUPPORT_IMAGE_EXTENSION = List.of("jpg", "jpeg", "png", "bmp", "webp");

    private static final String TEMPORARY_STORE_PREFIX = "temp/";

    private final S3Client s3Client;

    private final ImageOptimizeService imageOptimizeService;


    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.cloud.aws.region.static}")
    private String region;


    public List<ImageUrlDto> uploadContentImages(List<ImageDto> images) {

        if (images == null || images.size() < 1) {
            return null;
        }
        List<ImageUrlDto> imageUrls = new ArrayList<>();
        images.forEach(imageDto -> imageUrls.add(processUploadContentImage(imageDto)));

        return imageUrls;
    }


    private ImageUrlDto processUploadContentImage(ImageDto imageDto) {

        File convertedFile = null;
        File optimizedFile = null;

        try {
            MultipartFile requestFile = imageDto.getMultipartFile();

            // 지원하는 이미지 확장자 파일인지 검증
            validateImageExtension(requestFile);

            // 고유의 이미지 이름 생성
            String imageName = createUniqueFileName();

            // 파일 객체로 변환 & 서버에 임시저장
            convertedFile = convertToFile(imageName, requestFile);

            // 이미지 최적화 & 서버에 임시저장
            optimizedFile = convertToWebp(imageName, convertedFile);


            // s3에 업로드하기 위한 객체 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(TEMPORARY_STORE_PREFIX + optimizedFile.getName())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            // s3에 webp형식으로 최적화한 이미지 업로드
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(optimizedFile));

            // 실제로 업로드가 잘 됐는지 검증
            validateUpload(optimizedFile);

            // S3 URL과 , 요청에서 받아온 이미지 순서를 다시 넣어서 반환 (이건 아직 실제로 저장돼있는 url 이 아님)
            return new ImageUrlDto(getImageUrl(optimizedFile, ""), imageDto.getOrder());

        } catch (ImageException e) {
            throw new ImageException(e.getErrorCode());
        } catch (Exception e) { // 직접 의도하지 않은 에러 처리용
            throw new ImageException(e, IMAGE_SERVER_ERROR);

        } finally {
            deleteFile(optimizedFile);
        }
    }


    //s3의 temp/ 폴더에 있던 파일을 정식폴더에 복사한 후 삭제
    public void moveImageProcess(String imageUrl) {

        String imageName = extractImageName(imageUrl);

        validateUpload(TEMPORARY_STORE_PREFIX + imageName);

        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                .sourceBucket(bucketName)
                .sourceKey(TEMPORARY_STORE_PREFIX + imageName)
                .destinationKey(imageName)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();
        s3Client.copyObject(copyObjectRequest);


        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(TEMPORARY_STORE_PREFIX + imageName)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }


    private String getImageUrl(File pressedFile, String prefix) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName, region, prefix + pressedFile.getName());
    }

    private String getImageUrl(String imageName, String prefix) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName, region, prefix + imageName);
    }

    private String extractImageName(String imageUrl) {

        String[] parts = imageUrl.split("/"); // URL에서 파일명 부분만 추출
        return parts[parts.length - 1]; // 마지막 부분이 파일 이름
    }

    private void validateUpload(File pressedFile) {

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

    private void validateUpload(String imageName) {

        // 업로드된 객체 확인 (headObject 사용)
        HeadObjectRequest headRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(imageName)
                .build();

        HeadObjectResponse response = s3Client.headObject(headRequest);

        if (response == null) {
            throw new ImageException(S3_OBJECT_NOT_FOUND);
        }
    }

    private String createUniqueFileName() {
        return UUID.randomUUID().toString();
    }

    /**
     * System.getProperty("user.dir") -> 현재 프로젝트 최상위 디렉토리
     * 프로젝트 최상위 폴더 하위에 images 라는 폴더를 만들어놓고 저장중인 상황
     */
    private File convertToFile(String uniqueName, MultipartFile multipartFile) throws IOException {

        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new ImageException(INVALID_FILE);
        }

        File file = new File(System.getProperty("user.dir") +
                "/images/" + uniqueName + "." + extractExtension(multipartFile.getOriginalFilename()));

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

        if (multipartFile == null || !StringUtils.hasText(multipartFile.getOriginalFilename())) {
            throw new ImageException(INVALID_FILE);
        }

        String extension = extractExtension(multipartFile.getOriginalFilename());
        if (!SUPPORT_IMAGE_EXTENSION.contains(extension)) {
            throw new ImageException(UNSUPPORTED_IMAGE_EXTENSION);
        }
    }

    private String extractExtension(String originalFilename) {
        return originalFilename
                .substring(originalFilename.indexOf(".") + 1);
    }

    private void deleteFile(File file) {
        if (file != null && file.delete()) {
            log.info("File 삭제 ={}", file.getName());
        }
    }
}
