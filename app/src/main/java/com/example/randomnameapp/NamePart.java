package com.example.randomnameapp;

public class NamePart
{
    public int id;
    public String partType;
    public String partText;
    public int pickCount;

    public NamePart(String partText,String partType, int pickCount){
        this.partText = partText;
        this.partType = partType;
        this.pickCount = pickCount;
    }

    public NamePart(){
        this.partText = "";
        this.partType = "";
        this.pickCount = 0;
    }

}
