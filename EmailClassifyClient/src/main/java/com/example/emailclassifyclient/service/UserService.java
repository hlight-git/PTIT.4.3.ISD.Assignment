package com.example.emailclassifyclient.service;

import com.example.emailclassifyclient.Constant;
import com.example.emailclassifyclient.entity.User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService extends ABSService{
    public User get(int id){
        return rest.getForObject(Constant.API_URL + "/user/" + id, User.class);
    }
    public List<User> getAll(){
        return Arrays.stream(rest.getForObject(Constant.API_URL + "/user", User[].class)).toList();
    }
    public User validate(User loginUser){
        return rest.postForObject(Constant.API_URL + "/user/validate", loginUser, User.class);
    }
}
