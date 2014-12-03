package com.trying.web.controllers;

import com.trying.fe.utils.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {

    @RequestMapping("/")
    public ModelAndView index(@RequestParam(value="name", required=false, defaultValue="World") String name) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("name", name);
        Pair<String, Integer> p = new Pair<>("asdasdasd", 123123123);
        mav.addObject("p", p);
        return mav;
    }
}