package com.rcs.liferaysense.service.commonsense;

import java.io.IOException;
import java.nio.charset.Charset;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;

/**
 * Custom error handler, based on the spring framework DefaultErrorHandler.
 * But this class assumes that some HTTP status codes such as 409 and 403 are not
 * an error and there fore are not treated as errors.
 * @author juan
 */
class CommonSenseErrorHandler implements ResponseErrorHandler {

    /**
     * Delegates to {@link #hasError(HttpStatus)} with the response status code.
     */
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return hasError(response.getStatusCode());
    }

    /**
     * Template method called from {@link #hasError(ClientHttpResponse)}.
     * <p>The default implementation checks if the given status code is
     * {@link org.springframework.http.HttpStatus.Series#CLIENT_ERROR CLIENT_ERROR}
     * or {@link org.springframework.http.HttpStatus.Series#SERVER_ERROR SERVER_ERROR}. Can be overridden in subclasses.
     * @param statusCode the HTTP status code
     * @return <code>true</code> if the response has an error; <code>false</code> otherwise
     */
    protected boolean hasError(HttpStatus statusCode) {
        
        //make some statuses not be a error
        if (statusCode.value() == 409) {
            return false;
        }
        
        if (statusCode.value() == 403) {
            return false;
        }
        
        if (statusCode.value() == 404) {
            return false;
        }
        
        return (statusCode.series() == HttpStatus.Series.CLIENT_ERROR
                || statusCode.series() == HttpStatus.Series.SERVER_ERROR);
    }

    /**
     * {@inheritDoc}
     * <p>The default implementation throws a {@link HttpClientErrorException} if the response status code is
     * {@link org.springframework.http.HttpStatus.Series#CLIENT_ERROR}, a {@link HttpServerErrorException} if it is
     * {@link org.springframework.http.HttpStatus.Series#SERVER_ERROR}, and a {@link RestClientException} in other
     * cases.
     */
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = response.getStatusCode();
        MediaType contentType = response.getHeaders().getContentType();
        Charset charset = contentType != null ? contentType.getCharSet() : null;
        byte[] body = FileCopyUtils.copyToByteArray(response.getBody());
        switch (statusCode.series()) {
            case CLIENT_ERROR:
                throw new HttpClientErrorException(statusCode, response.getStatusText(), body, charset);
            case SERVER_ERROR:
                throw new HttpServerErrorException(statusCode, response.getStatusText(), body, charset);
            default:
                throw new RestClientException("Unknown status code [" + statusCode + "]");
        }
    }
}
