package com.spe.breadcrumbs.web.controller;

import com.spe.breadcrumbs.dao.*;
import com.spe.breadcrumbs.web.DBConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Blob;
import java.sql.SQLException;

@CrossOrigin
@Controller
@RequestMapping("/image")
public class ImageController {

    private MapDAO mapDAO = new MapDbDAO(new DBConnection());
    private QuestionDAO questionDAO = new QuestionDbDAO(new DBConnection());
    private HintDAO hintDAO = new HintDbDAO(new DBConnection());
    private MeetingDAO meetingDAO = new MeetingDbDAO(new DBConnection());
    private UserDAO userDAO = new UserDbDAO(new DBConnection());

    @RequestMapping(value = "/{image}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> postImage(@PathVariable("image") String image) throws SQLException {

        Blob blob = null;

        if (image.substring(0, 8).equals("venueMap")) blob = mapDAO.getMapByName(image).getPicture();
        else if (image.substring(0, 9).equals("hintImage")) blob = hintDAO.getHintByName(image).getPicture();
        //else if (image.substring(4).equals("path")) blob = pathDAO.getPath(image);
        else if (image.substring(0, 7).equals("meeting")) blob = meetingDAO.getMeeting(Long.parseLong(image)).getPicture();

        if (blob != null) {
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
        } else return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);

    }

}

