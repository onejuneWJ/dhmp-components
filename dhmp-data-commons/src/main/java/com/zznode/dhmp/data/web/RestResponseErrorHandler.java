package com.zznode.dhmp.data.web;

import com.zznode.dhmp.data.constant.CustomHeaders;
import org.springframework.core.ResolvableType;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 描述
 *
 * @author 王俊
 */
public class RestResponseErrorHandler extends DefaultResponseErrorHandler {

    private List<HttpMessageConverter<?>> messageConverters = Collections.emptyList();


    public void setMessageConverters(List<HttpMessageConverter<?>> converters) {
        this.messageConverters = Collections.unmodifiableList(converters);
    }

    @Override
    public boolean hasError(@NonNull ClientHttpResponse response) throws IOException {

        return super.hasError(response) || Objects.equals(response.getHeaders().getFirst(CustomHeaders.HAS_ERROR), "1");
    }

    @Override
    protected void handleError(@NonNull ClientHttpResponse response, @NonNull HttpStatusCode statusCode) throws IOException {
        String statusText = response.getStatusText();
        HttpHeaders headers = response.getHeaders();
        byte[] body = getResponseBody(response);
        Charset charset = getCharset(response);
        String message = getErrorMessage(statusCode.value(), statusText, body, charset);

        RestClientResponseException ex;
        if (statusCode.is4xxClientError()) {
            ex = HttpClientErrorException.create(message, statusCode, statusText, headers, body, charset);
        } else if (statusCode.is5xxServerError() || statusCode.is2xxSuccessful()) {
            // 重写父类方法，这里返回200的有异常的也这样
            ex = HttpServerErrorException.create(message, statusCode, statusText, headers, body, charset);
        } else {
            ex = new UnknownHttpStatusCodeException(message, statusCode.value(), statusText, headers, body, charset);
        }

        if (!CollectionUtils.isEmpty(this.messageConverters)) {
            ex.setBodyConvertFunction(initBodyConvertFunction(response, body));
        }
        throw ex;
    }

    @Override
    protected Function<ResolvableType, ?> initBodyConvertFunction(ClientHttpResponse response, byte[] body) {
        Assert.state(!CollectionUtils.isEmpty(messageConverters), "Expected message converters");
        return resolvableType -> {
            try {
                HttpMessageConverterExtractor<?> extractor =
                        new HttpMessageConverterExtractor<>(resolvableType.getType(), messageConverters);

                return extractor.extractData(new ClientHttpResponse() {
                    @Override
                    public HttpHeaders getHeaders() {
                        return response.getHeaders();
                    }

                    @Override
                    public HttpStatusCode getStatusCode() throws IOException {
                        return response.getStatusCode();
                    }

                    @Override
                    public String getStatusText() throws IOException {
                        return response.getStatusText();
                    }

                    /**
                     *
                     */
                    @Override
                    public void close() {
                        response.close();
                    }

                    @Override
                    public InputStream getBody() {
                        return new ByteArrayInputStream(body);
                    }
                });
            } catch (IOException ex) {
                throw new RestClientException("Error while extracting response for type [" + resolvableType + "]", ex);
            }
        };
    }


    private String getErrorMessage(int rawStatusCode, String statusText, @Nullable byte[] responseBody,
                                   @Nullable Charset charset) {

        String preface = rawStatusCode + " " + statusText + ": ";

        if (ObjectUtils.isEmpty(responseBody)) {
            return preface + "[no body]";
        }

        charset = (charset != null ? charset : StandardCharsets.UTF_8);

        String bodyText = new String(responseBody, charset);
        bodyText = LogFormatUtils.formatValue(bodyText, -1, true);

        return preface + bodyText;
    }
}
