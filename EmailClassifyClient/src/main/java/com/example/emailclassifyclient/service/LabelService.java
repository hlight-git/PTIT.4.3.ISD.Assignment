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
    public void addLabel(int userId, String labelName){
        rest.postForLocation(Constant.API_URL + "/label/add?userId=" + userId + "&labelName=" + labelName, null);
    }
    public void removeLabel(int labelId, int userId){
        rest.postForLocation(Constant.API_URL + "/label/delete?labelId=" + labelId + "&userId=" + userId, null);
    }
}
