package com.main.personalprojectback.global.util;

import jakarta.annotation.PostConstruct;
import nu.pattern.OpenCV;
import org.springframework.stereotype.Component;
/**
 * OpenCV 초기화 클래스.
 *
 * OpenCV는 C/C++ 기반의 네이티브 라이브러리이므로
 * 프로젝트 시작 시 JVM에 한 번 로드해야 한다.
 *
 * OpenPnP를 이용하여 DLL 로딩 및 JNI 연결을 자동으로 수행한다.
 */
@Component
public class OpenCvInitializer {

    @PostConstruct//이 라이브러리를 최초로 한번 등록(실행) 함
    // Spring Bean 생성이 완료되면 OpenCV(DLL)를 JVM에 로드한다.
// OpenCV는 최초 1회만 초기화하면 되므로 프로젝트 시작 시 한 번만 실행된다.
    public void init(){//참고로 반환타입 없음
        OpenCV.loadShared();
    }
}
