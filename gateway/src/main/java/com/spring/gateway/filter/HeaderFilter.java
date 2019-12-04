package com.spring.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.spring.gateway.util.DESUtils;
import com.spring.gateway.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@Component
@Order(-1)
public class HeaderFilter implements GlobalFilter {

    private static final String BEARER = "token";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authentication = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String method = request.getMethodValue();
        String body = request.getBody().toString();
        String url = request.getPath().value();
        log.info("网关过滤器 :url:{},method:{},headers:{}", url, method, request.getHeaders());
        //是否验证标识
        boolean isEncrypt = Boolean.valueOf(exchange.getRequest().getHeaders().getFirst("isEncrypt"));
        //是否需要验证加解密
        if (isEncrypt) {
            //todo 验证url授权
            //验证是否带token
            if (!authentication.startsWith(BEARER) || authentication.isEmpty()) {
                log.info("请求未携带token信息 url:{},method:{},headers:{}", url, method, request.getHeaders());
                return unauthorized(exchange);
            }
            //判断heard里是否有sys 路由参数
            String sys = exchange.getRequest().getHeaders().getFirst("sys");
            if (StringUtils.isEmpty(sys)) {
                log.info("sys 为空，无法进行路由设置! url:{},method:{},headers:{}", url, method, request.getHeaders());
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                //拦截非法请求
                return exchange.getResponse().setComplete();
            }
            //判断heard是否有时间戳 时间戳为加解密密钥
            String time = exchange.getRequest().getHeaders().getFirst("time");
            //验证时间戳是否超过配置时间
            if (StringUtils.isEmpty(time) || (System.currentTimeMillis() - Long.valueOf(time)) > 3 * 60 * 1000) {
                //提示接口超时  请重新发送请求
                log.info("接口请求时间戳为空或超时，请重新请求！url:{},method:{},headers:{}", url, method, request.getHeaders());
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                return exchange.getResponse().setComplete();
            }
            //cheksum
            String checkSum = exchange.getRequest().getHeaders().getFirst("checkSum");

            String checkSumBody = MD5Util.string2MD5(body);
            if (!StringUtils.isEmpty(checkSum) || !checkSum.equals(checkSumBody)) {
                //对body进行解密 秘钥为时间戳
                try {
                    log.info("url:{},method:{},headers:{},body密文 {}", body);
                    body = DESUtils.decrypt(body, time);
                    byte[] bytes = body.getBytes();


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                log.info("checkSum验证错误 url:{},method:{},headers:{}", url, method, request.getHeaders());
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            //放行请求
            return chain.filter(exchange);
        } else if (authentication.equals("kbsTest")) {
            //特定标识  本地和测试跳过请求验证  放行请求
            return chain.filter(exchange);
        }
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> unauthorized(ServerWebExchange serverWebExchange) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        DataBuffer buffer = serverWebExchange.getResponse()
                .bufferFactory().wrap(HttpStatus.UNAUTHORIZED.getReasonPhrase().getBytes());
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }


}