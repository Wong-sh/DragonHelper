package com.example.dragonhelpertext1.Bean.MessageBean;

public class CustomBindingBean {

    private final String id;
    private final String nickName;
    private final String online;
    private final String phone;
    private final String rent;

    public CustomBindingBean(String id, String nickName, String online, String phone, String rent){
        this.id = id;
        this.nickName = nickName;
        this.online = online;
        this.phone = phone;
        this.rent = rent;
    }

    public String getId(){
        return id;
    }

    public String getNickName(){
        return nickName;
    }

    public String getOnline(){
        return online;
    }

    public String getPhone(){
        return phone;
    }

    public String getRent(){
        return rent;
    }
}
