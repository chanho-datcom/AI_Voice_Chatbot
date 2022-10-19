package com.example.roda.model;


public class botModels {

    private String cnt;

    public botModels(String botChat) {
        this.cnt = botChat;
    }

    public String getBotChat() {
        return cnt;
    }

    public void setBotChat(String botChat) {
        this.cnt = botChat;
    }

}
