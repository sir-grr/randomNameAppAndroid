package com.example.randomnameapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class NameMaker
{
    private NameTypeTemplate nameTypeTemplate = new NameTypeTemplate();

    public NameMaker()
    {
        nameTypeTemplate = new NameTypeTemplate();
    }
    public NameMaker(NameTypeTemplate ntt)
    {
        nameTypeTemplate = ntt;
    }

    //this generates a single name based on an input com.example.randomnameapp.NameTypeTemplate
    public String GenerateName(Random rng)
    {
        List<String> vowels = Arrays.asList("e", "a", "i", "o", "u");
        String name = "";
        //var textAvailable = new List<String>();
        List<String> pick = nameTypeTemplate.grammars.get(rng.nextInt(nameTypeTemplate.grammars.size()));
        List<String> model = pick;
        System.out.println("using grammar " + pick);
        for (int i = 0; i < model.size(); i++)
        {
            List<String> textAvailable = new ArrayList<String>();
            for(NamePart namePart : nameTypeTemplate.nameParts){
                if(namePart.partType == model.get(i)){
                    textAvailable.add(namePart.partText);
                }
            }
            String textToBeInput = textAvailable.get(rng.nextInt(textAvailable.size()));
            name = name + textToBeInput;

        }

        if (name.length() < nameTypeTemplate.minLength)
        {
            return GenerateName(rng);
        }
        else if (name.length() > nameTypeTemplate.maxLength)
        {
            return GenerateName(rng);
        }
        name = Capatalise(name);
        return name;
    }

    //this method generates random names based on entire name models.
    public HashSet<String> GenerateNames(int numberOfNames)
    {

        //initialise all starter variables
        HashSet<String> pickedNames = new HashSet<String>();
        Random rng = new Random();

        //Generate Names
        do
        {
            String randomName = "";
            randomName = GenerateName(rng);

            pickedNames.add(randomName);
        } while (pickedNames.size() < numberOfNames);//repeat until you have all the needed values
        return pickedNames;
    }

    public String Capatalise(String word)
    {
        return (word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1).toLowerCase(Locale.ROOT));
    }
}
/*
            if (name.length() > 2)
            {
                for(int j = 0; j < name.length() - 1; j++){
                    boolean duplicateFixed = false;
                    duplicateFixed = !(textToBeInput.substring(j, j+1).equals(name.substring(j+1, j+2)) && !(nameTypeTemplate.repeatableLetters.contains(textToBeInput.substring(j, j+1))));
                    if (!duplicateFixed) {
                        textToBeInput = textToBeInput.replaceFirst(textToBeInput.substring(j, j+1),"");
                    }
                }
                for (int j = 0; j < name.length() - 2; j++)
                {
                    //if there is a triplet
                    if ((vowels.contains(name.substring(j, j+1)) && vowels.contains(name.substring(j + 1, j+2)) && vowels.contains(name.substring(j + 2, j+3))) ||
                        (!(vowels.contains(name.substring(j, j+1))) && !(vowels.contains(name.substring(j + 1, j+2))) && !(vowels.contains(name.substring(j + 2, j+3)))))
                    {
                        //remove the third letter of it
                        name = name.replaceFirst(name.substring(j, j+1),"");
                        //dont move forward an increment
                        j--;
                    }
                }
            }
 */

