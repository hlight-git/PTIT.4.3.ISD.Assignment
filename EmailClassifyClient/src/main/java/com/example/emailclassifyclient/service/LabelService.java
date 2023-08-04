package com.example.emailclassifyclient.service;

import com.example.emailclassifyclient.Constant;
import com.example.emailclassifyclient.entity.Label;
import com.example.emailclassifyclient.entity.LabelOfUser;
import com.example.emailclassifyclient.entity.User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class LabelService extends ABSService{
    public List<LabelOfUser> getAllLabelOfUser(User user){
        LabelOfUser[] response = rest.getForObject(Constant.API_URL + "/label?userId=" + user.getId(), LabelOfUser[].class);
        return response == null ? null : Arrays.stream(response).toList();
    }
    public Label addLabel(int userId, String labelName){
        return rest.getForObject(Constant.API_URL + "/label/add?userId=" + userId + "&labelName=" + labelName, Label.class);
    }
    public void removeLabel(int id){
        rest.getForObject(Constant.API_URL + "/label/delete?id=" + id, Boolean.class);
    }
}
