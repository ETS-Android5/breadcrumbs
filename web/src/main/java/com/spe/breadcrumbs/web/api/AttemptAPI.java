package com.spe.breadcrumbs.web.api;

import com.spe.breadcrumbs.dao.AttemptDAO;
import com.spe.breadcrumbs.dao.AttemptDbDAO;
import com.spe.breadcrumbs.entity.Choice;
import com.spe.breadcrumbs.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/api/attempts")

public class AttemptAPI {
    private AttemptDAO attemptDAO = new AttemptDbDAO();

    //given a user and a choice, posts the attempt to the database
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addAttempt(@RequestBody Map<User,Choice> map){
        User u = null;
        Choice c = null;
        for(User user : map.keySet()){
            u = user;
            c = map.get(u);
        }
        if(attemptDAO.addAttempt(u,c)){
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
