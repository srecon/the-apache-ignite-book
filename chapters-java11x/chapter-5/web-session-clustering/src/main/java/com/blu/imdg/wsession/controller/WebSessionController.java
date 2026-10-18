/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blu.imdg.wsession.controller;

import com.blu.imdg.wsession.bean.Person;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Web session controller with Jakarta annotations.
 */
@Controller
@RequestMapping("/")
@Scope("prototype")
public class WebSessionController {

    @RequestMapping(value = "/putperson", method = RequestMethod.GET)
    public String putSession(ModelMap model, HttpServletRequest request) {
        model.addAttribute("person", "Add Person to user session");
        var fName = request.getParameter("name");
        var age = Integer.parseInt(request.getParameter("age"));
        var person = new Person();
        person.setFirstName(fName);
        person.setAge(age);
        request.getSession().setAttribute(fName, person);
        return "welcome";
    }

    @RequestMapping(value = "/getperson", method = RequestMethod.GET)
    public String getSession(ModelMap model, HttpServletRequest request) {
        var fName = request.getParameter("name");
        var attr = request.getSession().getAttribute(fName);
        var age = attr != null ? "Age: " + ((Person) attr).getAge() : "Unknown";
        model.addAttribute("person", age);
        return "welcome";
    }
}
