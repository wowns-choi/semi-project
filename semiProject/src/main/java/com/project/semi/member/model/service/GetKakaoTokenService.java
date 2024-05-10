package com.project.semi.member.model.service;

import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.semi.exception.DecodingException;
import com.project.semi.member.model.dto.KakaoTokenResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetKakaoTokenService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public Map<String,String> getKakaoToken(String code){
        String url = "https://kauth.kakao.com/oauth/token";

        //HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", "9a9c9b276f33c0b00e56034c85cad4fb"); //앱키 중 REST API 키를 쓰면 된다. 
        map.add("redirect_uri", "http://115.90.212.22:11115/get-auth-code");
        map.add("code", code);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        KakaoTokenResponseDTO response = restTemplate.postForObject(url, entity, KakaoTokenResponseDTO.class);

        log.info("response={}", response);
        //response 에 들어온 것 중 JWT 는 id_token 에 들어온 것 뿐이다.
        String idToken = response.getId_token();
        log.info("response.id_token={}", idToken);
        // id_token 만 꺼내보면 아래와 같다. JWT 구성요소로는 헤더(Header), 페이로드(Payload), 서명(Signature) 가 있다.
        // 헤더 : eyJraWQiOiI5ZjI1MmRhZGQ1ZjIzM2Y5M2QyZmE1MjhkMTJmZWEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9
        // 페이로드 : .eyJhdWQiOiI5YTljOWIyNzZmMzNjMGIwMGU1NjAzNGM4NWNhZDRmYiIsInN1YiI6IjM0MjU0MDUwMjEiLCJhdXRoX3RpbWUiOjE3MTI0NTM4NzMsImlzcyI6Imh0dHBzOi8va2F1dGgua2FrYW8uY29tIiwibmlja25hbWUiOiJ3b3duczU5MCIsImV4cCI6MTcxMjQ3NTQ3MywiaWF0IjoxNzEyNDUzODczfQ
        // 서명 : .GDwhiYAbEWSt4uEU8F27hPPcntVQEnCCQN7mjfON3clFxPweSaEx-mfTNepL3nCyqnsvXeX-P44Gbg6lQkly9mP56bImbQ02czXWRKk43l8jCRHv3g-UAd0lxCvzKgWwpL2Rd4GZjDhUyWMmEhFe0SVJWiK8hVQY4EKZdaEsrjKwRve0pwbaElrO1ZueQ4BQRruOm_GL0fZTljQybqPUMTcQeh0vVWNJbYKB3Yc8JLY-0CPkgjKx8y0Rczl6PnN0svcgT5xPM4air5znoY-fS7_OLquES5feesubTzbu0sSZN_1km2xJ8jOEHJ7WpKfA-RoTXkMCeZBdazSvNEA6PQ
        // 위와 같이 . 을 기준으로 헤더 페이로드 서명 이 구분된다.

        Map<String, String> returnMap = new ConcurrentHashMap<>();


        try {
            // 페이로드를 디코딩하기 전에, 일단 서명이 올바른지에 대해 검증해야함.
            DecodedJWT jwtOrigin = JWT.decode(idToken);
            log.info("키 아이디 =========={}", jwtOrigin.getKeyId()); // 밑에서 사용될건데, https://kauth.kakao.com 에서 가져온 공개키 들 중 어떤 공개키인지를 구분할 수 있게 해주는 아이디 역할.
            log.info("토큰=========={}", jwtOrigin.getToken()); // <= id_token(JWT) 의 헤더 페이로드 서명 모두가 나오더라.
            log.info("헤더=========={}", jwtOrigin.getHeader());  // 헤더만 나오던데.
            log.info("페이로드=========={}", jwtOrigin.getPayload()); // 페이로드만 나옴.
            log.info("서명=========={}", jwtOrigin.getSignature()); // 서명만 나옴.
            // 이걸 통해 DecodedJWT 타입 객체에 대해 좀 알게 되었다.
            // JWT 라는 static 클래스의 decode 메서드를 호출하면서 JWT 를 매개변수로 넘겨주면,
            // 매개변수로 받은 JWT 를 분석해서 그 JWT 에 대한 정보를 바인딩한 DecodedJWT 라는 객체를 리턴해주는 구나.

            // 2. 공개키 프로바이더 준비
            // 	implementation 'com.auth0:jwks-rsa:0.21.1' 라고 build.gradle 에 추가해줬잖아.
            //  이 라이브러리의 역할은, 서버(https://kauth.kakao.com)로부터 HTTP GET 요청을 보내서 공개키들을 얻어오는 역할을 한다.
            //  가져온 공개키들은 JwkProvider 타입의 객체에 담기게 된다.
            JwkProvider provider = new JwkProviderBuilder("https://kauth.kakao.com")
                    .cached(10, 7, TimeUnit.DAYS) // 캐싱 설정. 왜? 카카오로그인 할 때마다, HTTP요청 보내서 공개키 가져오지 말고, 그냥 캐싱에 7일간 담아두고 꺼내 쓰겠다는 뜻. 10은 한번에 캐싱할 수 있는 공개키의 최대 개수.
                    .build();
            // 공개키들 중 JWT(idToken) 에 담겨있는 keyId 와 일치하는 keyId 를 가진 공개키를 꺼내온다.
            // 이때, Jwk 타입 객체는 공개키와 관련된 데이터를 담는 객체라고 보면 된다.
            Jwk jwk = provider.get(jwtOrigin.getKeyId());

            // 3. 검증 및 디코딩
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null); // 공개키 객체에서 publicKey 를 이용하여 알고리즘 객체를 만든다.
            JWTVerifier verifier = JWT.require(algorithm).build(); // 이렇게 하면 알고리즘객체를 이용해서 JWTVerifier 라는 서명검증기계인 JWTVerifier 타입 객체가 나온다고 생각하면 된다.
            DecodedJWT jwt = verifier.verify(idToken); // 서명검증기계를 이용해서, 카카오서버가 준 idToken(JWT) 에 들어있는 서명이 올바른지를 통해(카카오의 공개키와 같은지를 통해) 내가 받은 JWT가 카카오가 보낸것인지를 검증하는 것이다.
            // 만약 서명이 일치하지 않는다면? JwkException 이라는 예외가 발생하게 되어있다. catch 문에서 처리.

            //------------------------------------------------------------------------

            //여기까지 왔다는 건, 서명이 일치한다는 뜻.
            // JWT 중 페이로드 디코딩하기 시작

            Map<String, Object> payload = decodeJWT(response.getId_token());

            if (payload != null && payload.get("sub") != null) {
                //payload 라는 Map 자료구조에 뭐가 들어있나 보자.
                log.info("payload={}", payload);
                //payload={
                //aud=9a9c9b어쩌구저쩌구, <= 내 앱키 중 REST API 키.
                //sub=34어쩌구저쩌구, <= 이게 뭐냐면, 카카오서버에서 사용자별로 부여한 고유한 번호임. 
                //auth_time=1712453912, <= 사용자가 카카오 로그인을 통해 인증을 완료한 시각.
                //iss=https://kauth.kakao.com, <= ID토큰(JWT의 종류 중 사용자에 대한 데이터를 담고 있는 JWT)를 발급한 인증기관 정보로서, https://kauth.kakao.com 으로 고정되어 있음.
                //nickname=wowns590, <= 카카오에서의 nickname 임.
                //exp=1712475512, <= 발급받은 토큰의 만료 시간.
                // iat=1712453912 <= 토큰의 발급 시각.
                //}

                // 위 데이터 중 뭐 써야될까?
                String sub = (String) payload.get("sub"); 
                String nickname = (String) payload.get("nickname"); 

                returnMap.put("sub", sub);
                returnMap.put("nickname", nickname);

            } else {
                // JWT 디코딩 중 오류가 발생해서 payload 에 null 이 왔다는 거거든?
                // 어떻게 해줘야 할까?
                // 사용자정의 예외 클래스를 하나 만들고 그 클래스로 만든 예외를 발생시켜주자
                throw new DecodingException();
            }

        } catch (JwkException e) {
            log.error("GetKakaoTokenService.java - 카카오 서명 검증 중 예외 발생. 서명 불일치 ", e);
            e.printStackTrace();
            returnMap.put("signatureError", "true");
            return returnMap;
        } catch (DecodingException e) {
            returnMap.put("decodingError", "true");
            return returnMap;
        }

        return returnMap;
    }

    private Map<String, Object> decodeJWT(String jwtToken) {

        // 카카오 서버에서 부터 시작해보자.
        // 카카오 서버에서 JWT(Json Web Token) JSON 형태의 문자열을 바이트배열로 변환한 다음, 그 바이트배열을 Base64 방식으로 문자열로 인코딩해서 나한테 준것.
        // 나는 String 타입 필드인 id_token 으로 그 문자열을 받았다.
        // 그래서 이제 뭘 해줘야 하냐면,
        // 1단계 : 그 문자열을 다시 바이트배열로 디코딩해줘야 함.
        // 2단계 : 바이트 배열을 다시 JSON 형식의 문자열로 변환해줘야 함.

        try {
            String[] parts = jwtToken.split("\\."); // 정규표현식에서 '.' 와 같은 특수문자를 표현하기 위한 이스케이프가 '\\' 거든 결론적으로, '\\.' 라고 하면, '.' 을 기준으로 split 하라는 말이다
            String payload = parts[1];
            // 1단계
            byte[] decode = Base64.getUrlDecoder().decode(payload); //문자열을 바이트배열로 디코딩
            // 2단계
            String decoded = new String(decode, StandardCharsets.UTF_8); // UTF_8 방식으로 다시 데이터 형식을 문자열로 변환.

            // 현재 decoded 는 JSON 형태의 문자열이다. log 를 찍어봤더니, 아래와 같았다.
            // {
            // "aud":"9a9c9b어쩌구저쩌구",
            // "sub":"342어쩌구저쩌구",
            // "auth_time":1712461060,
            // "iss":"https://kauth.kakao.com",
            // "nickname":"wowns590",
            // "exp":1712482660,
            // "iat":1712461060
            // }
            // 이런 JSON 문자열을 꺼내서 쓰기에는 굉장히 불편하잖아. 어떻게 sub 라는 키에 담긴 값을 꺼내올건데?
            // 그래서,
            // 이러한 JSON 형태의 문자열을 자바 자료구조인 Map 이라든지, 아니면 자바 객체(내가 DTO객체 만들어야겠지) 로 변환시켜주려면
            // ObjectMapper 의 readValue 를 이용하면 된다. 아래애서는 Map자료구조로 그냥 받는 거임.

            return objectMapper.readValue(decoded, Map.class);

        } catch (Exception e) {
            log.error("Failed to decode JWT", e);
            return null;
        }
    }

}
