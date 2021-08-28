package com.dummy.stub.config.filters;

import com.dummy.stub.util.constant.HeaderConstant;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * This class is an implementation of {@link Filter}
 */
@Component
public class CorrelationLogFilter implements javax.servlet.Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = ((HttpServletRequest) request);
        String correlationID = httpServletRequest.getHeader(HeaderConstant.CORRELATION_ID.getValue());
        String xCorrelationID = httpServletRequest.getHeader(HeaderConstant.X_CORRELATION_ID.getValue());
        String xForwardedFor = httpServletRequest.getHeader(HeaderConstant.X_FORWARDED_FOR.getValue());
        String xSessionId = httpServletRequest.getHeader(HeaderConstant.X_SESSION_ID.getValue());

        if (Objects.isNull(correlationID)) {
            if (Objects.nonNull(xCorrelationID)) {
                correlationID = xCorrelationID;
            } else {
                correlationID = UUID.randomUUID().toString();
            }
        }
        try {
            MDC.put(HeaderConstant.CORRELATION_ID.getValue(), correlationID);
            setInMdcIfPresent(HeaderConstant.X_FORWARDED_FOR.getValue(), xForwardedFor);
            setInMdcIfPresent(HeaderConstant.X_SESSION_ID.getValue(), xSessionId);

            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void destroy() {
    }

    private void setInMdcIfPresent(String headerName, String value) {
        Optional.ofNullable(value)
                .filter(v -> !StringUtils.isEmpty(v))
                .ifPresent(
                        forwardedFor -> MDC.put(headerName, value)
                );
    }
}