package com.example.dragonhelpertext1.Bean.MessageBean;

public class CustomNewsBean {

    private final String no;
    private final String text;

    public CustomNewsBean(String no, String text){
        this.no = no;
        this.text = text;
    }

    public String getNo(){
        return no;
    }

    public String getText(){
        return text;
    }
}
