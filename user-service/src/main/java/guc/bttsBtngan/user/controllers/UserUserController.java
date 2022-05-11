package guc.bttsBtngan.user.controllers;

import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import guc.bttsBtngan.user.data.UserUserInteraction;
import guc.bttsBtngan.user.services.UserUserService;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping(path = "users")
public class UserUserController {

    @Autowired
    private UserUserService userUserService;

    @GetMapping("/users")
    public String getAllUsers() {
        // TODO: implement
        return userUserService.getAllUsers();
    }


    @PostMapping("/users/register")
    public void registerUser(@RequestBody UserUserInteraction user) {
        userUserService.registerUser(user);
    }


    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable("userId") long id) {
        userUserService.deleteUser(id);
    }

    @PutMapping(path = "/users/user-profile/{userId}")
    public void updateUser(@PathVariable("userId") long id,
                           @RequestParam(required = false) String username,
                           @RequestParam(required = false) String email,
                           @RequestParam(required = false) String oldPassword,
                           @RequestParam(required = false) String newPassword
                        ,@RequestParam(name="photo",required = false) MultipartFile photo
    ) throws MinioException, IOException {
        userUserService.updateUser(id, username, email, oldPassword, newPassword,photo);
    }

//    @GetMapping(path = "users/photo/{photoRef}")
//    public String getAllphotoRef(@PathVariable("photoRef") String photoRef) {
//        return userUserService.getAllphotoRef(photoRef);
//    }
//
//    // moderator can ban users
//    @PostMapping(path = "users/ban/{userId}")
//    public String banUser(@PathVariable("userId") String userId, String moderatorId) {
//        return userUserService.banUser(userId, moderatorId);
//    }
//
//    // moderator can unban users
//    @PostMapping(path = "users/unban/{userId}")
//    public String unbanUser(@PathVariable("userId") String userId, String moderatorId) {
//        return userUserService.unbanUser(userId, moderatorId);
//    }

}
