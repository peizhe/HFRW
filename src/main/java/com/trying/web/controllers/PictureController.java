package com.trying.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "picture")
public class PictureController {

    @RequestMapping(value = "load")
    public ModelAndView load(){
        return new ModelAndView("logosPeoplePictures");
    }
}