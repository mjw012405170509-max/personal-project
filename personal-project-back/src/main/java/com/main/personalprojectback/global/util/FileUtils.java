package com.main.personalprojectback.global.util;

import com.main.personalprojectback.global.config.S3Config;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@RequiredArgsConstructor -> Lombok이 반드시 초기화해야 하는 필드만 받는 생성자를 만들어 주는 어노테이션입니다.아래가 예시

//    private final S3Client s3Client;      이처럼 final이거나 @NotNull이 붙여 씀(생성자 주입)

//헷갈릴시에 @Autowired로 가져와도 동작은 한다(필드 주입).다만, 실무기준 @RAC(생성자 주입)를 쓴다.
@RequiredArgsConstructor
@Component
public class FileUtils {

    // S3Config.java에서 만든 S3Client Bean 주입
    private final S3Client s3Client;

    // application.properties의 cloud.aws.s3.bucket 값을 읽어옴
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // application.properties의 cloud.aws.region.static 값을 읽어옴
    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * [핵심] S3에 파일 업로드
     * MultipartFile(브라우저에서 받은 파일)을 S3 버킷에 저장
     * @return s3Key - S3에 저장된 경로+파일명 (DB에 저장할 값)
     * String fileRepo 부분은 아래 upload의 2번 부분 참조
     */
//    public String upload(MultipartFile file, String fileRepo) throws IOException {
//
//        // 1. 원본 파일명에서 확장자 추출 (예: ".jpg", ".pdf")
//        String originalName = file.getOriginalFilename();
//        String ext = originalName != null && originalName.contains(".")
//                ? originalName.substring(originalName.lastIndexOf("."))
//                : "";
//
//        // 2. 파일명 충돌 방지 → UUID로 고유한 파일명 생성
//        //    저장 경로 예시: posts/3/550e8400-e29b-41d4-a716.jpg
//        /*  여기서부터 민지원 작성, 고유한 파일명을 위해서 fileRepo 부분에 들어와야할 정보는
//        /  각각의 컨트롤러에 url요청 + "/" + 브랜드는 브랜드식별자,
//                                         후기는 후기 번호,
//                                         썸네일은 회원식별값
//                                         아니면 아예 UUID.randomUUID를 이용한 고유값,예를 들어
//                                         String groupId = UUID.randomUUID() ->UUID.randomUUID는 원래 36자의 하이폰 포함 문자열 뽑음
//                                                        .toString()//String 변환(현재는 타입이 UUID 타입임)
//                                                        .replace("-", "")//(하이폰 제거)
//                                                        .substring(0, 12);//(12자로 제한(굳이 할 필요는 없음))
//        */
//        String s3Key = fileRepo + "/" + UUID.randomUUID() + ext;
//
//        // 3. S3에 업로드할 요청 객체 생성
//        //    bucket: 어느 버킷에, key: 어떤 경로/이름으로 저장할지
//        PutObjectRequest putRequest = PutObjectRequest.builder()
//                .bucket(bucket)
//                .key(s3Key)
//                .contentType(file.getContentType())  // 파일 MIME 타입 (image/jpeg 등)
//                .contentLength(file.getSize())        // 파일 크기 (bytes)
//                .build();
//
//        // 4. 실제 S3 업로드 실행
//        s3Client.putObject(putRequest,
//                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
//
////        log.info("S3 업로드 완료: {}", s3Key);
//
//        // 5. 저장된 s3Key 반환 → PostService에서 DB에 저장
//        return s3Key;
//    }
    private String upload(byte[] data, String s3Key,String contentType){

//        String s3Route = "";
//         3. S3에 업로드할 요청 객체 생성
        //    bucket: 어느 버킷에, key: 어떤 경로/이름으로 저장할지
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .contentType(contentType)  // 파일 MIME 타입 (image/jpeg 등)
                .contentLength((long) data.length)//data(byte)정보 안에 length가 들어있음 // 파일 크기 (bytes)
                .build();
//
//        // 4. 실제 S3 업로드 실행
        s3Client.putObject(putRequest,
                RequestBody.fromBytes(data));//지금 타입을 전부 byte로 받아서 기존 파일 받을때보다 문이 더 축소됨
//
////        log.info("S3 업로드 완료: {}", s3Key);
//
//        // 5. 저장된 s3Key 반환 → PostService에서 DB에 저장
//        return s3Key;
//    }

        return s3Key;
    }
    /**
     * fileRepo 앞뒤의 슬래시를 정리한다.
     *
     * 예를 들어 "/menus/10/"이 들어오면 "menus/10"으로 바꿔서
     * S3 key가 "//"처럼 지저분해지는 것을 막는다.
     */
    private String normalizeFileRepo(String fileRepo) {
        if (fileRepo == null || fileRepo.trim().isEmpty()) {
            return "images";
        }

        return fileRepo.trim()
                .replaceAll("^/+", "")
                .replaceAll("/+$", "");
    }
    /**
     * S3에서 파일 삭제
     * 게시글 삭제/수정 시 기존 파일을 S3에서도 제거
     * @param s3Key 삭제할 파일의 S3 경로+파일명
     */
    public void delete(String s3Key) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)  // 삭제할 파일 경로
                    .build();
            s3Client.deleteObject(deleteRequest);
//            log.info("S3 삭제 완료: {}", s3Key);
        } catch (Exception e) {
//            log.error("S3 삭제 실패: {}", s3Key, e);
        }
    }

    /**
     * S3 삭제 실패 시 예외를 던지는 삭제 메서드.
     * 첨부파일 개별 삭제처럼 DB 삭제와 강결합된 흐름에서 사용한다.
     */
    public void deleteOrThrow(String s3Key) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)
                    .build();
            s3Client.deleteObject(deleteRequest);
//            log.info("S3 삭제 완료: {}", s3Key);
        } catch (Exception e) {
//            log.error("S3 삭제 실패: {}", s3Key, e);
            throw new RuntimeException("S3 파일 삭제에 실패했습니다.");
        }
    }

    /**
     * S3 key → 브라우저에서 접근 가능한 URL로 변환 -> 처음에 파일 집어넣을때 남긴 저장경로를 다시 URL로 만듬
     * 예: posts/3/uuid.jpg
     *  → https://my-bucket.s3.ap-northeast-2.amazonaws.com/posts/3/uuid.jpg
     */
    public String getFileUrl(String s3Key) {
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + s3Key;
    }
    public String extractS3Key( String url) {
        String prefix = "https://" + bucket + ".s3." + region + ".amazonaws.com/";
        return url.replace(prefix, "");
    }
//    public List<String> resize(String width, String height){
//
//        return null;
//    }




}
