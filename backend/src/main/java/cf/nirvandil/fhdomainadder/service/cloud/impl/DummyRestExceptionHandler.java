package cf.nirvandil.fhdomainadder.service.cloud.impl;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * {@link ResponseErrorHandler}, which never reports an error and allows to deal with them on your own.
 */
public class DummyRestExceptionHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) {
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse response) {

    }
}
