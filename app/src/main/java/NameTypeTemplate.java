import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NameTypeTemplate
{
    public Integer id;
    public String identifyingName;
    public List<NamePart> nameParts;
    public List<Integer> pickGroup;
    public List<NameGrammar> grammars;
    public Set<RepeatableLetter> repeatableLetters;
    public Integer minLength;
    public Integer maxLength;


    public NameTypeTemplate()
    {
        identifyingName = "";
        pickGroup = new ArrayList<Integer>();
        grammars = new ArrayList<NameGrammar>();
        nameParts = new ArrayList<NamePart>();
        repeatableLetters = new HashSet<RepeatableLetter>();
        minLength = 0;
        maxLength = 0;

    }

    public NameTypeTemplate(String modelName, List<String> names)
    {

        //initialise vowels so we can differentiate between vowels and consonants later
        String[] vowels = { "a","o","e","i","u" };

        //Ititialising the parts we've been given
        this.identifyingName = modelName;
        this.pickGroup = new ArrayList<Integer>();
        this.nameParts = new ArrayList<NamePart>();
        this.repeatableLetters = new HashSet<RepeatableLetter>();
        this.grammars = new ArrayList<NameGrammar>();


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



        //looping for all the names
        for(String name : names)
        {
            //getting the ends of names
            for (Integer l = 2; l < 4; l++)
            {
                if (l <= name.length())
                {
                    String end = name.substring(- l);
                    MakePart(end, "END");
                }
            }
        }
        //looping for all the names
        for(String name : names)
        {
            //getting ph sounds and repeatables
            for (Integer i = 0; i < name.length() - 1; i++)
            {
                String ph = name.substring(i, 2);
                if ((vowels.contains(name.substring(i, 1)) && !(vowels.Contains(name.substring(i + 1, 1)))) || (!(vowels.Contains(name.substring(i, 1))) && vowels.Contains(name.substring(i + 1, 1))))
                {
                    MakePart(ph,"PH");
                }
                if (name.substring(i, 1).equals(name.substring(i + 1, 1)))
                {
                    repeatableLetters.Add(new RepeatableLetter() { letter = name.substring(i, 1) });
                }

            }

            //getting c and v values
            for (Integer i = 0; i < name.length() - 1; i++)
            {
                String letter = name.substring(i, 1);
                if (vowels.Contains(letter))
                {
                    MakePart(letter, "V");
                }
                else
                {
                    MakePart(letter, "C");
                }
            }
        }

        //automatically generating grammars off the names we will pass in
        MakeGrammars(names);

        //setting pick counts for the grammars
        MakePicklength();

        //get how many potential names can be made with this model. there is some room for error in this. It ignores that triplets arent allowed and that ph can emulate an vc or cv
        CalculatePotentialUniques();
    }
    public void MakePicklength()
    {
        pickGroup = new List<Integer>();
        foreach (var grammar in this.grammars)
        {
            for (Integer i = 0; i < grammar.picklength; i++)
            {
                pickGroup.Add(grammar.id);
            }
        }
    }
    //this adds a part to the part list if it meets cetain conditions
    public void MakePart(String partTextIn, String partTypeIn)
    {
        boolean partIsNew = true;
        //this loop checks the part doesnt already exist and if it does and is of the correct type we up its pick count
        for (Integer c = 0; c < this.nameParts.size(); c++)
        {
            if (partTextIn == this.nameParts.get(c).partText && partTypeIn == this.nameParts.get(c).partType)
            {
                this.nameParts.get(c).picklength++;
                partIsNew = false;
            }
            else if (partTextIn == this.nameParts.get(c).partText)
            {
                partIsNew = false;
            }
        }
        if (partIsNew)
        {
            nameParts.Add(new NamePart() { picklength = 1, partText = partTextIn, partType = partTypeIn });
        }
    }
    public void MakeGrammars(List<String> names)
    {
        //here we will build an algorithm for creating grammars.
        List<String> usedGrammarsInStringForm = new List<String>();
        for(String name : names)
        {
            usedGrammarsInStringForm = MakeStringListGrammarsForName(name, new List<String>(), usedGrammarsInStringForm);
        }

    }

    public List<String> MakeStringListGrammarsForName(String unAnalysedPartOfName, List<String> currentGrammar, List<String> listOfGrammarsAsStrings)
    {
        Utility ut = new Utility();
        //loop for the size of the String
        for (Integer i = 0; i < unAnalysedPartOfName.length(); i++)
        {
            //if the part is at the start of the unAnalysed portion of the name
            bool isAPart = false;
            NamePart np = new NamePart();
            for (Integer j = 0; j < nameParts.length(); j++)
            {
               if(nameParts.get(j).partText == unAnalysedPartOfName.substring(0, (unAnalysedPartOfName.length() - i)))
               {
                    isAPart = true;
                    np = nameParts.get(j);
               }
            }
            if (isAPart)
            {
                //make a version of the grammar with this part type added to it
                List<String> grammarWithPart = currentGrammar.Concat(new List<String>() { np.partType }).ToList();

                //if this isnt the end of the name
                if (!((unAnalysedPartOfName.length() - (unAnalysedPartOfName.length() - i)) == 0))
                {
                    //go down anoother level and pass through the part of the name you havent analysed and the state the grammar should be in
                    MakeStringListGrammarsForName(unAnalysedPartOfName.Remove(0, (unAnalysedPartOfName.length() - i)), grammarWithPart, listOfGrammarsAsStrings);
                }
                else // if it is the end of the name
                {
                    //this part checks if the grammar avoids unwanted triplets
                    //default that it does
                    bool avoidsUnwantedTriples = true;
                    if (grammarWithPart.length() >= 3)
                    {
                        Integer lastIndex = grammarWithPart.length() - 1;
                        //checks if this grammar fails to avoid unwanted triple consonants
                        avoidsUnwantedTriples = ((grammarWithPart.get(lastIndex - 2).equals("C") || grammarWithPart.get(lastIndex - 2).equals("START") || grammarWithPart.get(lastIndex - 2).equals("PH")) &&
                            grammarWithPart.get(lastIndex - 1).equals("C") && grammarWithPart.get(lastIndex).equals("C"))
                        || (grammarWithPart.get(lastIndex - 2).equals("C") && grammarWithPart.get(lastIndex - 1).equals("C") &&
                            (grammarWithPart.get(lastIndex).equals("C") || grammarWithPart.get(lastIndex).equals("END") || grammarWithPart.get(lastIndex).equals("PH")))
                        //checks if this grammar fails to avoid unwanted triple vowels
                        || ((grammarWithPart.get(lastIndex - 2).equals("V") || grammarWithPart.get(lastIndex - 2).equals("START") || grammarWithPart.get(lastIndex - 2).equals("PH")) &&
                            grammarWithPart.get(lastIndex - 1).equals("V") && grammarWithPart.get(lastIndex).equals("V"))
                        || (grammarWithPart.get(lastIndex - 2).equals("V") && grammarWithPart.get(lastIndex - 1).equals("V") &&
                            (grammarWithPart.get(lastIndex).equals("V") || grammarWithPart.get(lastIndex).equals("END") || grammarWithPart.get(lastIndex).equals("PH")));
                        //inverse the result to find if it succeeds in avoiding triplets
                        avoidsUnwantedTriples = !avoidsUnwantedTriples;
                    }
                    //if the grammar does try and avoid unwanted triplets
                    if (avoidsUnwantedTriples)
                    {
                        //if the grammar has already been added
                        if (listOfGrammarsAsStrings.Contains(ut.StringListToString(grammarWithPart)))
                        {
                            //up its pick count
                            grammars.Where(gram => ut.StringListToString(gram.instructions.Select(ins => ins.instructionText).ToList()) == ut.StringListToString(grammarWithPart)).First().picklength++;
                        }
                        //checks to see if the structure generated is a valid grammar and saves it to a bool,
                        //this includes checking the grammar begins with a start and ends in an end or is made of few enough parts then checks of it starts correctly or ends correctly.
                        else if (((grammarWithPart.First().equals("START") && grammarWithPart.Last().equals("END")) || (grammarWithPart.length() <= 2) && ((grammarWithPart.First().equals("START") || grammarWithPart.Last().equals("END")))))
                        {
                            //add it to your grammar checklist
                            listOfGrammarsAsStrings.Add(ut.StringListToString(grammarWithPart));
                            //add it to the models grammars
                            this.grammars.Add(new NameGrammar()
                            {
                                instructions = ut.StringGrammarToInstructionGrammar(grammarWithPart),
                                picklength = 1,
                            });
                        }
                    }
                }
            }
        }
        return listOfGrammarsAsStrings;
    }




}

