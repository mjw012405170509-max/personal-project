package com.main.personalprojectback.global.util;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.List;

@Component
public class ImageProcessor {
    //자바에서 이미지 보관 객체 -> openCv에서 쓸 객체로 전환
    private Mat BufImgRoMat(BufferedImage bufImg){
        //BufferedImage타입을 가져와서 openCv가 읽을 수 있는 형태인 Mat자료형으로 변환
        //이때, 바로 변환이 안되어서, 바이트 단위로 꺼낸다음, 그걸 Mat에게 줘서 새로운 mat에 넣는다.
        //Raster->픽셀저장공간 getDataBuffer->실제 픽셀로 저장하는 버퍼
        bufImg = convertTo3ByteBGR(bufImg);//밑의 함수
        byte[] pixels = ((DataBufferByte) bufImg.getRaster().getDataBuffer()).getData();
        Mat mat = new Mat(
                bufImg.getHeight(),
                bufImg.getWidth(),
                CvType.CV_8UC3//8U → 8비트(unsigned) 정수 & C3 → Channel 3개(BGR)
        );
        mat.put(0,0,pixels);
        return mat;
    }
    //BufferedImage의 내부 저장 형식을 OpenCV가 처리하기 쉬운 TYPE_3BYTE_BGR로 통일한다.
    private BufferedImage convertTo3ByteBGR(BufferedImage bufImg){
        //이미 타입이 TYPE_3BYTE_BGR이면 return
        if(bufImg.getType() == BufferedImage.TYPE_3BYTE_BGR){
            return bufImg;
        }
        // 동일한 크기의 TYPE_3BYTE_BGR 이미지를 새로 생성
        BufferedImage convertImg = new BufferedImage(
                bufImg.getWidth(),
                bufImg.getHeight(),
                BufferedImage.TYPE_3BYTE_BGR//여기서 타입 강제
        );

        //새 이미지에 원본을 그려 넣는 로직
        //Graphics2D:이미지에 그림 그리는 도구
        Graphics2D g = convertImg.createGraphics();

        // (결과적으로 내부 저장 방식만 TYPE_3BYTE_BGR로 변경된다.)
        g.drawImage(bufImg,0,0,null);//내용 복사로 설정됨
        g.dispose();//그림 그리는 도구 파기(자원 반납)
        return convertImg;
    }
    private BufferedImage matToBufImg(Mat mat){
        BufferedImage image = new BufferedImage(
                mat.cols(),//width
                mat.rows(),//height
                BufferedImage.TYPE_3BYTE_BGR
        );
        //Mat의 전체 byte수=전체 픽셀수 * 채널수(색깔 개수)
        byte[] pixels = new byte[(int) (mat.total() * mat.channels())];

        //(0,0)부터 모든 픽셀을 byte[]로 복사하라.
        mat.get(0,0,pixels);
        //이제 byte[]를 BufferedImage에 복사
        image.getRaster().setDataElements(
                0,
                0,
                mat.cols(),
                mat.rows(),
                pixels
        );
        return image;
    }


    public BufferedImage toGrayScale(){//회색조 (색을 회색으로 통일) 변환

        return null;
    }
    public BufferedImage threshold(){//이진화(글자와 배경을 분리)

        return null;
    }
    public BufferedImage denoise(){//노이즈 제거(점이나 먼지등)

        return null;
    }
    public BufferedImage deskew(BufferedImage image){//기울기 보정
        Mat src = BufImgRoMat(image);
        // 1. 글자가 있는 좌표 추출(Mat은 ArrayList처럼 다양한 정보를 담을수 있는 객체)
        Mat points = new Mat();
        Core.findNonZero(src,points);
        // 다음 함수는 좌표들의 짐합을 받아야 하는데, Mat을 MatOfPoint2f형태로 변환해야함
        MatOfPoint2f pointssf =
                new MatOfPoint2f((
                        points.reshape(2,(int) points.total())
                        ));

        return matToBufImg(src);
    }
    public BufferedImage sharpen(){//선명도

        return null;
    }
    public BufferedImage resize(){//사이즈 조절

        return null;
    }
}
