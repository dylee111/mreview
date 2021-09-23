package org.zerock.mreview.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UploadTestController {

    // 요청되는 url은 Resource에 포함되어있다.
    @GetMapping("/uploadEx")
    public void uploadEx() {}
}
