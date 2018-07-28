package com.example.kayda.mendle.Areas.Requests.Models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Request {

    public String UserRequestId;
    public String UserRequestImage;
    public String UserRequestName;
    public String RequestStatus;


    public Request(){

    }
    public Request(String UserRequestId, String UserRequestImage, String UserRequestName){
        this.UserRequestId = UserRequestId;
        this.UserRequestImage = UserRequestImage;
        this.UserRequestName = UserRequestName;
        this.RequestStatus = "not sent";
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("UserRequestId", UserRequestId);
        result.put("UserRequestImage", UserRequestImage);
        result.put("UserRequestName", UserRequestName);
        result.put("RequestStatus", RequestStatus);

        return result;
    }
}
