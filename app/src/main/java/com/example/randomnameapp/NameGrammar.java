package com.example.randomnameapp;

import java.util.ArrayList;
import java.util.List;

public class NameGrammar
{
    public int id;
    public List<String> instructions;
    public int pickCount;

    public NameGrammar(List<String> instructions, int pickCount){
        this.instructions = instructions;
        this.pickCount = pickCount;
    }
    public NameGrammar(){
        this.instructions = new ArrayList<String>();
        this.pickCount = 0;
    }
}
