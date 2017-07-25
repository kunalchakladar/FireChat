package com.example.kunal.firechat;

/**
 * Created by Kunal on 7/25/2017.
 */

public class Message {

    private String msg, sender, reciever;

    public Message(String msg, String sender, String reciever) {
        this.msg = msg;
        this.sender = sender;
        this.reciever = reciever;
    }

    public Message(){

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }
}
