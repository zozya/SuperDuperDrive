package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest httpRequest, Model model) {

        String errorMsg = "";
        Object status = httpRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(status != null) {
            Integer httpErrorCode = Integer.valueOf(status.toString());
            switch (httpErrorCode) {
                case 400: {
                    errorMsg = "Http Error Code: 400. Bad Request";
                    break;
                }
                case 401: {
                    errorMsg = "Http Error Code: 401. Unauthorized";
                    break;
                }
                case 403: {
                    errorMsg = "Http Error Code: 403. Forbidden";
                    break;
                }
                case 404: {
                    errorMsg = "Http Error Code: 404. Resource not found";
                    break;
                }
                case 500: {
                    errorMsg = "Http Error Code: 500. Internal Server Error";
                    break;
                }
                default: {
                    errorMsg = "Http Error Code: " + httpErrorCode + "Unknown Error";
                }
        }
        }
        model.addAttribute("errorMsg", errorMsg);
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
