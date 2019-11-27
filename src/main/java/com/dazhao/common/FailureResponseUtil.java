package com.dazhao.common;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FailureResponseUtil {

    public static void failureResponse(HttpServletResponse response, String mesg, int httpStatus) {
        response.setStatus(httpStatus);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(mesg);
            writer.flush();
        } catch (Exception e) {
            log.error("response error", e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
