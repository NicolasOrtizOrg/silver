package org.silver.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.silver.exceptions.GenericException;
import org.silver.utils.HeaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HeaderInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = request.getHeader("userId");
        if (userId == null)
            throw new GenericException("Deberías agregar un 'userId' en los headers para hacer peticiones a este endpoint.");

        HeaderUtils.saveHeader("userId", userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        HeaderUtils.deleteHeader();
    }
}
