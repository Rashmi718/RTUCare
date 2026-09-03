package com.rtucare.backend.Interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "requestStartTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME, startTime);

        log.info("Request: {} {} | Client: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
        long startTime = (long) request.getAttribute(START_TIME);
        long duration = System.currentTimeMillis() - startTime;

        if (ex != null) {
            log.error("Request failed: {} {} | Status: {} | Duration: {} ms | Error: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration,
                    ex.getMessage());
        } else if (response.getStatus() >= 400) {
            log.warn("Request completed with error: {} {} | Status: {} | Duration: {} ms",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration);
        } else {
            log.info("Request completed: {} {} | Status: {} | Duration: {} ms",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration);
        }
    }
}
