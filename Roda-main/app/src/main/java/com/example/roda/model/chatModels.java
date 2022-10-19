package com.example.roda.model;

import java.util.HashMap;
import java.util.Map;

public class chatModels {

    public Map<String,Boolean> users = new HashMap<>();
    public Map<String,Comment> comment = new HashMap<>();

    public static class Comment{
        public String uid;
        public String message;
    }


}
