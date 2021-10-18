import java.io.Console;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    //this generates a single name based on an input NameTypeTemplate
    public String GenerateName(Random rng)
    {
        String[] vowels = { "e", "a", "i", "o", "u" };
        String name = "";
        //var textAvailable = new List<String>();
        nameTypeTemplate typeModel = nameTypeTemplate;
        int randomPick = typeModel.pickGroup.ElementAt(rng.Next(typeModel.pickGroup.Count()));
        var model = typeModel.grammars.Where(nModel => nModel.id == randomPick).First().instructions;
        for (int i = 0; i < model.Count(); i++)
        {
            var textAvailable = typeModel.nameParts.Where(part => part.partType == model.ElementAt(i).instructionText);
            var textToBeInput = textAvailable.ElementAt(rng.Next(textAvailable.Count())).partText;

            if (i != 0)
            {
                //checks if there is two of the same letter in a row and if so deletes one if the letter isnt alowed to repeat
                bool duplicateFixed = false;
                do
                {
                    if (!textToBeInput.Equals(""))
                    {
                        duplicateFixed = !(textToBeInput.SubString(0, 1).Equals(name.SubString(name.Length - 1, 1)) && !(typeModel.repeatableLetters.Select(rl => rl.letter).Contains(textToBeInput.SubString(0, 1))));
                        if (!duplicateFixed)
                        {
                            textToBeInput = textToBeInput.SubString(1, textToBeInput.Length - 1);
                        }
                    }
                    else
                    {
                        duplicateFixed = true;
                    }
                } while (!duplicateFixed);

            }
            name = name + textToBeInput;
            if (name.Length > 2)
            {
                for (int j = 0; j < name.Length - 2; j++)
                {
                    //if there is a triplet
                    if ((vowels.Contains(name.SubString(j, 1)) && vowels.Contains(name.SubString(j + 1, 1)) && vowels.Contains(name.SubString(j + 2, 1))) ||
                        (!(vowels.Contains(name.SubString(j, 1))) && !(vowels.Contains(name.SubString(j + 1, 1))) && !(vowels.Contains(name.SubString(j + 2, 1)))))
                    {
                        //remove the third letter of it
                        name = name.Remove(j + 2, 1);
                        //dont move forward an increment
                        j--;
                    }
                }
            }
        }

        if (name.Length < typeModel.minLength)
        {
            return GenerateName(rng);
        }
        else if (name.Length > typeModel.maxLength)
        {
            return GenerateName(rng);
        }
        name = Capatalise(name);
        return name;
    }
    //this just gets a name from a list
    public String GetName(Random rng)
    {
        var nameOptions = nameTypeTemplate.names;
        String randomName = Capatalise(nameOptions.ElementAt(rng.Next(nameOptions.Count)).nameText);
        return randomName;
    }
    //this randomly picks from a list or creates a name using a template
    public String TwoMethodGetName(Random rng)
    {
        //Generate Name
        if (rng.Next(4) == 0)
        {
            return GetName(rng);
        }
        else
        {
            return GenerateName(rng);
        }
    }

    //gets names which have already been created
    public HashSet<String> GetNames(int numberOfNames)
    {

        //initialise all starter variables
        var watch = Stopwatch.StartNew();
        HashSet<String> pickedNames = new HashSet<String>();
        Random rng = new Random();

        //Get Names
        do
        {
            pickedNames.Add(GetName(rng));
            long timePassed = watch.ElapsedMilliseconds;
            if (timePassed > 1500)
            {
                Console.WriteLine(pickedNames.Count + "/" + numberOfNames + " Names Sourced");
                watch.Restart();
            }
        } while (pickedNames.Count() < numberOfNames);//repeat until you have all the needed values
        return pickedNames;
    }

    //this method generates random names based on entire name models.
    public HashSet<String> GenerateNames(int numberOfNames)
    {

        //initialise all starter variables
        var watch = Stopwatch.StartNew();
        HashSet<String> pickedNames = new HashSet<String>();
        Random rng = new Random();

        //Generate Names
        do
        {
            String randomName = "";
            randomName = GenerateName(rng);

            pickedNames.Add(randomName);
            long timePassed = watch.ElapsedMilliseconds;
            if (timePassed > 1500)
            {
                Console.WriteLine(pickedNames.Count + "/" + numberOfNames + " Names Generated");
                watch.Restart();
            }
        } while (pickedNames.Count() < numberOfNames);//repeat until you have all the needed values
        return pickedNames;
    }

    //this gets names using a mix of both methods
    public HashSet<String> TwoMethodGetNames(int numberOfNames)
    {

        //initialise all starter variables
        var watch = Stopwatch.StartNew();
        HashSet<String> pickedNames = new HashSet<String>();
        Random rng = new Random();
        do
        {
            pickedNames.Add(TwoMethodGetName(rng));
            long timePassed = watch.ElapsedMilliseconds;
            if (timePassed > 1500)
            {
                Console.WriteLine(pickedNames.Count + "/" + numberOfNames + " Names Sourced");
                watch.Restart();
            }
        } while (pickedNames.Count() < numberOfNames);//repeat until you have all the needed values
        return pickedNames;
    }

    public String Capatalise(String word)
    {
        return (word.SubString(0, 1).ToUpper() + word.SubString(1, word.Length - 1).ToLower());
    }
}

