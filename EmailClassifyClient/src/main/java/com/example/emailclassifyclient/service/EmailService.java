package com.example.emailclassifyclient.service;

import com.example.emailclassifyclient.Constant;
import com.example.emailclassifyclient.entity.Email;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class EmailService extends ABSService{
    public List<Email> getEmails(int userId, int page){
        Email[] response = rest.getForObject(Constant.API_URL + "/email?receiverId=" + userId + "&page=" + page, Email[].class);
        return response == null ? null : Arrays.stream(response).toList();
    }

    public List<Email> getEmails(int userId, int page, int labelId){
        String url = Constant.API_URL + "/email?receiverId=" + userId + "&labelId=" + labelId + "&page=" + page;
        Email[] response = rest.getForObject(url, Email[].class);
        return response == null ? null : Arrays.stream(response).toList();
    }

    public Email getEmail(int id) {
        return rest.getForObject(Constant.API_URL + "/email/detail?id=" + id, Email.class);
    }

    public void saveEmail(Email email){
        rest.postForLocation(Constant.API_URL + "/email/save", email);
    }
    public void changeLabel(int emailId, int labelId){
        rest.postForLocation(Constant.API_URL + "/email/update?emailId=" + emailId + "&labelId=" + labelId, null);
    }
}
