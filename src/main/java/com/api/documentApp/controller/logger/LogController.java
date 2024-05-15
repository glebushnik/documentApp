package com.api.documentApp.controller.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {
    Logger logger = LoggerFactory.getLogger(LogController.class);

    @RequestMapping("/log")
    public String index() {
        logger.trace("A TRACE Message");
        logger.error("An ERROR Message");
        return  "Logging";
    }
}
