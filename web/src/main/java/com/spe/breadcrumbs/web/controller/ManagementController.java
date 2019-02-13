package com.spe.breadcrumbs.web.controller;

import com.spe.breadcrumbs.dao.*;
import com.spe.breadcrumbs.entity.Choice;
import com.spe.breadcrumbs.entity.Expert;
import com.spe.breadcrumbs.entity.Question;
import com.spe.breadcrumbs.entity.User;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/management")

public class ManagementController {

    private UserDAO userDAO = new UserDbDAO();
    private ExpertDAO expertDAO = new ExpertDbDAO();
    private QuestionDAO questionDAO = new QuestionDbDAO();

    @RequestMapping(method = RequestMethod.GET)
    public String tableContent(Model m){
        //TODO make this function faster (INNER JOINs)
        m.addAttribute("users",userDAO.getAllUsers());
        m.addAttribute("experts", expertDAO.getAllExperts());
        m.addAttribute("questions", questionDAO.getAllQuestions());
        return "views/management";
    }

    //////////////// USER UPDATE ADD ////////////////////////

    @RequestMapping(method = RequestMethod.GET, value= "/user/{id}")
    public String updateUser(@PathVariable Long id, Model m) {
        User match = userDAO.getUser(id);
        m.addAttribute("user", match);
        return "views/management_userEdit";
    }

    @PostMapping("/user/updateUser/{id}")
    public RedirectView updateUser(@ModelAttribute User user, @PathVariable Long id) {
        userDAO.update(id, user);
        return new RedirectView("http://localhost:8080/management");
    }

    @RequestMapping(method = RequestMethod.GET, value= "/user")
    public String addUser(Model m) {
        m.addAttribute("user", new User());
        return "views/management_userAdd";
    }

    @PostMapping("/addUser")
    public RedirectView addUser(@ModelAttribute User user) {
        userDAO.addUser(user);
        return new RedirectView("http://localhost:8080/management");
    }

    @PostMapping("/user/deleteUser/{id}")
    public RedirectView deleteUser(@PathVariable Long id) {
        userDAO.deleteUser(id);
        return new RedirectView("http://localhost:8080/management");
    }

    //////////////// EXPERT UPDATE ADD /////////////////////

    @RequestMapping(method = RequestMethod.GET, value= "/expert/{id}")
    public String updateExpert(@PathVariable Long id, Model m) {
        Expert match = expertDAO.getExpert(id);
        m.addAttribute("expert", match);
        return "views/management_expertEdit";
    }

    @PostMapping("/expert/updateExpert/{id}")
    public RedirectView updateExpert(@ModelAttribute Expert expert, @PathVariable Long id) {
        expertDAO.update(id, expert);
        return new RedirectView("http://localhost:8080/management");
    }

    @RequestMapping(method = RequestMethod.GET, value= "/expert")
    public String addExpert(Model m) {
        m.addAttribute("expert", new Expert());
        return "views/management_expertAdd";
    }

    @PostMapping("/addExpert")
    public RedirectView addExpert(@ModelAttribute Expert expert) {
        expertDAO.addExpert(expert);
        return new RedirectView("http://localhost:8080/management");
    }

    @PostMapping("/expert/deleteExpert/{id}")
    public RedirectView deleteExpert(@PathVariable Long id) {
        expertDAO.deleteExpert(id);
        return new RedirectView("http://localhost:8080/management");
    }

    //////////////// BREADCRUMB UPDATE ////////////////////////

    @RequestMapping(method = RequestMethod.GET, value= "/breadcrumb/{id}")
    public String updateBreadcrumb(@PathVariable Long id, Model m) {
        Question match = questionDAO.findById(id);
        m.addAttribute("question", match);
        //List<Choice> choices = questionDAO.getChoices(id);
        //m.addAttribute("choices", choices);
        return "views/management_breadcrumbEdit";
    }

    @PostMapping("/breadcrumb/updateBreadcrumb/{id}")
    public RedirectView updateBreadcrumb(@ModelAttribute Question question, @PathVariable Long id) {
        questionDAO.update(id, question);
        return new RedirectView("http://localhost:8080/management");
    }

}
