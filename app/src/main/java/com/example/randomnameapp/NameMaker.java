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
        NameTypeTemplate typeModel = nameTypeTemplate;
        int randomPick = typeModel.pickGroup.get(rng.nextInt(typeModel.pickGroup.size()));
        NameGrammar pick = new NameGrammar();
        for(NameGrammar grammar : typeModel.grammars){
            if(grammar.id == randomPick){
                pick = grammar;
                break;
            }
        }
        List<String> model = pick.instructions;
        for (int i = 0; i < model.size(); i++)
        {
            List<String> textAvailable = new ArrayList<String>();
            for(NamePart namePart : typeModel.nameParts){
                if(namePart.partType == model.get(i)){
                    textAvailable.add(namePart.partText);
                }
            }
            String textToBeInput = textAvailable.get(rng.nextInt(textAvailable.size()));

            if (i != 0)
            {
                //checks if there is two of the same letter in a row and if so deletes one if the letter isnt alowed to repeat
                boolean duplicateFixed = false;
                do
                {
                    if (!textToBeInput.equals(""))
                    {
                        duplicateFixed = !(textToBeInput.substring(0, 1).equals(name.substring(name.length() - 1, 1)) && !(typeModel.repeatableLetters.contains(textToBeInput.substring(0, 1))));
                        if (!duplicateFixed)
                        {
                            textToBeInput = textToBeInput.substring(1, textToBeInput.length() - 1);
                        }
                    }
                    else
                    {
                        duplicateFixed = true;
                    }
                } while (!duplicateFixed);

            }
            name = name + textToBeInput;
            if (name.length() > 2)
            {
                for (int j = 0; j < name.length() - 2; j++)
                {
                    //if there is a triplet
                    if ((vowels.contains(name.substring(j, 1)) && vowels.contains(name.substring(j + 1, 1)) && vowels.contains(name.substring(j + 2, 1))) ||
                        (!(vowels.contains(name.substring(j, 1))) && !(vowels.contains(name.substring(j + 1, 1))) && !(vowels.contains(name.substring(j + 2, 1)))))
                    {
                        //remove the third letter of it
                        name = name.substring(j + 2, 1);
                        //dont move forward an increment
                        j--;
                    }
                }
            }
        }

        if (name.length() < typeModel.minLength)
        {
            return GenerateName(rng);
        }
        else if (name.length() > typeModel.maxLength)
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
        return (word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1, word.length() - 1).toLowerCase(Locale.ROOT));
    }
}

