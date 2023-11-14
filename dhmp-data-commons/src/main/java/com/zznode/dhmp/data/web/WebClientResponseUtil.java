package com.zznode.dhmp.data.web;

import com.zznode.dhmp.core.constant.BaseConstants;
import com.zznode.dhmp.core.exception.CommonException;
import com.zznode.dhmp.data.constant.CustomHeaders;
import com.zznode.dhmp.data.exception.ExceptionResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * WebClient 响应处理工具类
 *
 * <p>
 * <pre>
 * webClient.get()
 *    .uri(this.baseUrl)
 *    .exchangeToMono(clientResponse -&gt; {
 *        return WebClientResponseUtil.mapToMono(clientResponse, FileInfo.class)
 *    })
 *    .onErrorMap(WebClientResponseException.class, e -&gt; {
 *      return WebClientResponseUtil.mapErr(e)
 *    });
 * </pre>
 *
 * @author 王俊
 */
public class WebClientResponseUtil {

    public static <T> Mono<T> mapToMono(ClientResponse clientResponse, Class<T> type) {
        if (isSuccess(clientResponse)) {
            return clientResponse.bodyToMono(type);
        } else {
            return clientResponse.createError();
        }
    }

    public static <T> Mono<T> mapToMono(ClientResponse clientResponse, ParameterizedTypeReference<T> elementTypeRef) {
        if (isSuccess(clientResponse)) {
            return clientResponse.bodyToMono(elementTypeRef);
        } else {
            return clientResponse.createError();
        }
    }

    public static <T> Flux<T> mapToFlux(ClientResponse clientResponse, Class<T> type) {
        if (isSuccess(clientResponse)) {
            return clientResponse.bodyToFlux(type);
        } else {
            Mono<T> errorMono = clientResponse.createError();
            return errorMono.flux();
        }
    }

    public static <T> Flux<T> mapToFlux(ClientResponse clientResponse, ParameterizedTypeReference<T> elementTypeRef) {
        if (isSuccess(clientResponse)) {
            return clientResponse.bodyToFlux(elementTypeRef);
        } else {
            Mono<T> errorMono = clientResponse.createError();
            return errorMono.flux();
        }
    }

    public static Throwable mapErr(Throwable e) {
        if (e instanceof WebClientResponseException webClientResponseException) {
            ExceptionResponse exceptionResponse = webClientResponseException.getResponseBodyAs(ExceptionResponse.class);
            return new CommonException(exceptionResponse != null ? exceptionResponse.getMessage() : BaseConstants.ErrorCode.ERROR);
        }
        return e;
    }

    private static boolean isSuccess(ClientResponse clientResponse) {
        return clientResponse.statusCode().is2xxSuccessful() &&
                !clientResponse.headers().asHttpHeaders().containsKey(CustomHeaders.HAS_ERROR);
    }
}
