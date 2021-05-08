package com.bruce.controller;

import com.bruce.pojo.User;
import com.bruce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @BelongsProject: SSM-WEB-ANNOTATION
 * @BelongsPackage: com.bruce.controller
 * @CreateTime: 2021-05-08 16:43
 * @Description: TODO
 */
@RequestMapping
@Controller
public class UserController {

    @Autowired
    UserService us;

    @ResponseBody
    @RequestMapping("/list")
    public List<User> list(){
        System.out.println("控制器");
        return us.findUsers();
    }

    @RequestMapping("/show")
    public String show(Model model){
        model.addAttribute("msg","SSM全注解整合");
        return "show";
    }

}
