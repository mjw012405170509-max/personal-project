package com.main.personalprojectback.global.util;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PdfConverter {

    public List<BufferedImage> pdfToImg(MultipartFile file) throws IOException {
        byte[] pdfBytes = file.getBytes();//pdfBox는 자바거라 멀티파트파일(스프링 것)을 못 읽어서 0101010형식의 바이트로 변환
        // 메모리에 pdf등록
        try (PDDocument document = Loader.loadPDF(pdfBytes)) {//try(try-with-resource)는 자원(close())을 자동으로 정리(document를 닫아야함)document.close();
            if(document.getNumberOfPages()==0){
                throw new IllegalArgumentException("pdf파일의 페이지가 없는 것으로 보입니다");
            }
            PDFRenderer renderer = new PDFRenderer(document);//이미지로 변환(정확히는 그려주는 객체)
//            BufferedImage image = new BufferedImage();
                List<BufferedImage> images = new ArrayList<>();
            for(int i=0;i<document.getNumberOfPages();i++){
                BufferedImage image = renderer.renderImageWithDPI(i,300);//이미지 생성0 : 첫 번째 페이지 ,300 : 300DPI 해상도(for문으로 바뀌어야 함)
                images.add(image);
            }
            return images;
        }
    }
}
