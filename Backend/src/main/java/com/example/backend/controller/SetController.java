package com.example.backend.controller;

import com.example.backend.dao.UserDao;
import com.example.backend.entity.State;
import com.example.backend.entity.UserData;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@Controller // gives page
@RestController // provide data to web page

public class SetController {

    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/getAllUsers")
    public List<UserData> getUser() {
        return userService.getAllUsers();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/editUser")
    public Map<String, Object> editUser(@RequestBody UserData userData) {

        Map<String, Object> map = new HashMap();
        Map<String, Object> validTest;
        validTest = validation(userData);
        try {
            if (!(Boolean) validTest.get("Valid")) return validTest;
//            boolean b = userService.getEmailorMobile(userData);
            map.put("Message", "Data Successfully Uploaded");
            map.put("value", userService.editUser(userData.getId(), userData));

        } catch (DataIntegrityViolationException e) {
            map.put("Message", "Email | Mobile No is used by another User");
            System.out.println(e.getMessage());
        } catch (Exception e) {

            map.put("Message", "Internal Error");
            System.out.println(e.getMessage());

        }
        return map;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/addUser")
    public Map<String, Object> addUser(@RequestBody UserData userData) {

        Map<String, Object> map = new HashMap();
        Map<String, Object> validTest;
        validTest = validation(userData);
        try {
            if (!(Boolean) validTest.get("Valid")) return validTest;
            boolean b = userService.getEmailorMobile(userData);
            if (b) {
                map.put("Message", "User Exists with Same Email or Phone No");
                return map;

            }
            map.put("Message", "Data Inserted");
            map.put("value", userService.addUser(userData));

        } catch (Exception e) {

            map.put("Message", "Internal Error");
            System.out.println(e.getMessage());

        }
        return map;
    }

    private Map<String, Object> validation(UserData userData) {
        Map<String, Object> mapvalidate = new HashMap<>();
        String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        try {
            //Email
            Pattern emailpattern = Pattern.compile(emailRegex);
            Matcher emailmatcher = emailpattern.matcher(userData.getEmail());
            if (!emailmatcher.matches()) {
                mapvalidate.put("Email", false);
                mapvalidate.put("Valid", false);
                return mapvalidate;
            }

            //PhoneNo
            Pattern phone = Pattern.compile("^\\d{10}$");
            Matcher phoneMatcher = phone.matcher(userData.getMobile());
            if (!phoneMatcher.matches()) {
                mapvalidate.put("Mobile", false);
                mapvalidate.put("Valid", false);
                return mapvalidate;
            }
            mapvalidate.put("Valid", true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            mapvalidate.put("status", "Error");
        }
        return mapvalidate;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/saveImage")
    public ResponseEntity<Object> saveUser(@RequestParam("id") Integer id,
                                           @RequestParam("photo") MultipartFile multipartFile) throws IOException {
        try {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            String uploadDir = "src/main/resources/images/" + id + "/";

            UserData user = userService.getUserByIdSetImg(id, "images/" + id + "/" + fileName);

            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

            Map<String, Object> map = new HashMap();
            map.put("Message", "Success");
            map.put("value", user);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (Exception E) {
            Map map = new HashMap();
            map.put("Message", E.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(map);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/image")
    public ResponseEntity<InputStreamResource> getImage(@RequestParam("path") String path) throws IOException {

        var imgFile = new ClassPathResource(path);

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(imgFile.getInputStream()));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/getStates")
    public List<String> getAllStates() {
        List<String> states = Stream.of(State.values()).map(Enum::name).collect(Collectors.toList());
        return states;
    }

}
