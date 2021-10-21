package com.example.randomnameapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class NameTypeTemplate
{
    public Integer id;
    public String identifyingName;
    public List<NamePart> nameParts;
    public List<List<String>> grammars;
    public Set<String> repeatableLetters;
    public Integer minLength;
    public Integer maxLength;


    public NameTypeTemplate()
    {
        identifyingName = "";
        grammars = new ArrayList<List<String>>();
        nameParts = new ArrayList<NamePart>();
        repeatableLetters = new HashSet<String>();
        minLength = 0;
        maxLength = 0;

    }

    public NameTypeTemplate(String modelName, List<String> names)
    {
        //initialise vowels so we can differentiate between vowels and consonants later
        List<String> vowels = Arrays.asList("a","o","e","i","u");

        //Ititialising the parts we've been given
        this.identifyingName = modelName;
        this.nameParts = new ArrayList<NamePart>();
        this.repeatableLetters = new HashSet<String>();
        this.grammars = new ArrayList<List<String>>();

        //setting all the names to lower case
        for(String name : names){
            names.set(names.indexOf(name),name.toLowerCase(Locale.ROOT));
        }


        //using the names we've added

        //setting the default max an min values
        this.maxLength = names.get(0).length();
        this.minLength = names.get(0).length();

        //looping for all the names
        for(String name : names)
        {
            //getting the max and min values
            if (name.length() > this.maxLength)
            {
                this.maxLength = name.length();
            }
            else if (name.length() < this.minLength)
            {
                this.minLength = name.length();
            }
        }
        System.out.println("Length Segment Complete Max: " + this.maxLength + " Min: " + this.minLength);

        //looping for all the names
        for(String name : names)
        {
            //getting the starts of names
            for (Integer i = 2; i < 4; i++)
            {
                if (i <= name.length())
                {
                    String start = name.substring(0, i);
                    MakePart(start, "START");
                }

            }
        }
        System.out.println("Start parts retrieved");



        //looping for all the names
        for(String name : names)
        {
            //getting the ends of names
            for (Integer l = 2; l < 4; l++)
            {
                if (l <= name.length())
                {
                    String end = name.substring(name.length() - l,name.length());
                    MakePart(end, "END");
                }
            }
        }
        System.out.println("End parts retrieved");

        //looping for all the names
        for(String name : names)
        {
            //getting ph sounds and repeatables
            for (Integer i = 0; i < name.length() - 1; i++)
            {
                String ph = name.substring(i, i+2);
                if ((vowels.contains(name.substring(i, i+1)) && !(vowels.contains(name.substring(i + 1, i+2)))) || (!(vowels.contains(name.substring(i, i+1))) && vowels.contains(name.substring(i + 1, i+2))))
                {
                    MakePart(ph,"PH");
                }
                else if (name.substring(i, i+1).equals(name.substring(i + 1, i+2)))
                {
                    repeatableLetters.add(name.substring(i, i+1));
                }

            }
            System.out.println("PH parts retrieved");

            //getting c and v values
            for (Integer i = 0; i < name.length(); i++)
            {
                String letter = name.substring(i, i+1);
                if (vowels.contains(letter))
                {
                    MakePart(letter, "V");
                }
                else
                {
                    MakePart(letter, "C");
                }
            }
            System.out.println("Vs and Cs retrieved");
        }

        System.out.println("Parts Added");
        for (NamePart part : nameParts){
            System.out.println("Type: " + part.partType + " Text: " + part.partText);
        }
        //automatically generating grammars off the names we will pass in
        MakeGrammars(names);
        System.out.println("Grammars generated");
        for(List<String> ng : grammars){
            System.out.println(ng);
        }
    }

    //this adds a part to the part list if it meets certain conditions
    public void MakePart(String partTextIn, String partTypeIn)
    {
        boolean partIsNew = true;
        //this loop checks the part doesnt already exist and if it does and is of the correct type we up its pick count
        for (Integer c = 0; c < this.nameParts.size(); c++)
        {
            if (partTextIn.equals(this.nameParts.get(c).partText))
            {
                partIsNew = false;
            }
        }
        if (partIsNew)
        {
            nameParts.add(new NamePart(partTextIn,partTypeIn,1));
            System.out.println("adding part Type: " + partTypeIn + " Text: " + partTextIn );
        }
    }
    public void MakeGrammars(List<String> names)
    {

        for(String name : names)
        {
            MakeGrammarsForName(name, new ArrayList<String>());
        }

    }

    public void MakeGrammarsForName(String unAnalysedPartOfName, List<String> currentGrammar)
    {
        System.out.println("Make grammars for name running");
        //find whats shorter the length or four
        int maxPartLength = unAnalysedPartOfName.length();
        if(unAnalysedPartOfName.length() > 3){
            maxPartLength = 3;
        }
        //loop for the start of the name
        for (Integer i = 1; i <= maxPartLength; i++)
        {
            //System.out.println("loop " + i);
            NamePart np = new NamePart();
            for (Integer j = 0; j < nameParts.size(); j++)
            {
               if(nameParts.get(j).partText.equals(unAnalysedPartOfName.substring(0, i)))
               {
                    np = nameParts.get(j);
                    //System.out.println("Should match "+ nameParts.get(j).partText + " = " + unAnalysedPartOfName.substring(0, i));
                    String passDownUnAnalysedPartOfName = unAnalysedPartOfName.substring(i);
                    List<String> newGrammar = new ArrayList<>();
                    newGrammar.addAll(currentGrammar);
                    newGrammar.add(np.partType);
                    if(passDownUnAnalysedPartOfName.length()>0){
                        //System.out.println("should continue with " + passDownUnAnalysedPartOfName);
                        MakeGrammarsForName(passDownUnAnalysedPartOfName, newGrammar);
                    }
                    else if(!(grammars.contains(newGrammar))){
                        //System.out.println("should make a grammar");
                        this.grammars.add(newGrammar);
                    }
               }
            }

        }
    }


/*if (isAPart)
            {
                //make a version of the grammar with this part type added to it
                List<String> grammarWithPart = currentGrammar;
                grammarWithPart.add(np.partType);
                //System.out.println("Grammar with part" + grammarWithPart);

                //if this isnt the end of the name
                if (!((unAnalysedPartOfName.length() - i) == 0))
                {
                    System.out.println("more than 0 Letters remaining in " + unAnalysedPartOfName);
                    //go down another level and pass through the part of the name you havent analysed and the state the grammar should be in
                    String passDownUnAnalysedPartOfName = unAnalysedPartOfName.substring(i);
                    System.out.println("Passing Down " + passDownUnAnalysedPartOfName);
                    MakeGrammarsForName(passDownUnAnalysedPartOfName, grammarWithPart);
                }
                else // if it is the end of the name
                {
                    System.out.println("all letters used");
                    //checks to see if the structure generated is a valid grammar and saves it to a boolean,
                    //this includes checking the grammar begins with a start and ends in an end or is made of few enough parts then checks of it starts correctly or ends correctly.
                    if (((grammarWithPart.get(0).equals("START") && grammarWithPart.get(-1).equals("END")) || (grammarWithPart.size() <= 2) && ((grammarWithPart.get(0).equals("START") || grammarWithPart.get(-1).equals("END")))))
                    {
                        System.out.println("valid and creating grammar");
                        System.out.println("Grammar with part" + grammarWithPart);
                        //add it to the models grammars
                        this.grammars.add(new NameGrammar(grammarWithPart));
                    }
                }
            }*/

}

