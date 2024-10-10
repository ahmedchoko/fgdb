package com.example.fgdb.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Gateway Acces Controller
 */
@RestController
public class Home {

    /**
     * Test Gateway Access
     *
     * @return
     */
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public Object home() {
        return "Hello From Gateway";
    }
}
