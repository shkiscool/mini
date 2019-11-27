package com.dazhao.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class JWTUtil {

    private static final long EXPIRE_TIME = (long) (24 * 60 * 60 * 1000);
    private static final String SECRET = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY";
    private static final Map HEADMAP = new ImmutableMap.Builder<String, String>().put("alg", "HS256").put("typ", "JWT").build();
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);
    private static final String HREADER_TOKEN_PARAM = "authorization";

    private JWTUtil() {
    }

    /**
     * 生成签名，30分后过期
     *
     * @param userId 用户名
     * @return 加密的token
     */
    public static String sign(int userId) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        return JWT.create()
                .withHeader(HEADMAP)
                .withClaim("userId", userId)
                .withExpiresAt(date)
                .sign(ALGORITHM);
    }

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token) {
        try {
            JWT.require(ALGORITHM).build().verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static Integer getUserInfo(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("userId").asInt();
    }

    /**
     * 获得token中的用户id
     *
     * @return token中包含的用户名
     */
    public static Integer getUserId(HttpServletRequest request) {
        DecodedJWT jwt = JWT.decode(request.getHeader(HREADER_TOKEN_PARAM));
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("userId").asInt();
    }
}
