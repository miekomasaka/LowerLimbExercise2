package com.example.lowerlimbexercise;

public class ListData {
    String id;
    String name;
    String yomi;
    String kentyo;
    String yomi_kentyo;

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setName (String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setYomi(String yomi){
        this.yomi = yomi;
    }

    public  String getYomi(){
        return yomi;
    }

    public void setKentyo(String kentyo){
        this.kentyo = kentyo;
    }

    public String getKentyo(){
        return kentyo;
    }

    public void setYomi_kentyo(String yomi_kentyo){
        this.yomi_kentyo = yomi_kentyo;
    }

    public String getYomi_kentyo(){
        return yomi_kentyo;
    }
}
