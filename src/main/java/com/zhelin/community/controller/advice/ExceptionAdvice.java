package com.zhelin.community.controller.advice;

import com.zhelin.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // log error
        logger.error("Server error:" + e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            logger.error(element.toString());
        }
        // send error message
        String xRequestedWith = request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equalsIgnoreCase(xRequestedWith)) {
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1, "Server error"));
        } else {
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}
