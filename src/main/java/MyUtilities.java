import org.json.JSONObject;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rich on 1/31/2018.
 */
public class MyUtilities {

    public static void writeStringListToFile(LinkedList<String> stringList, String pathToOutput) throws Exception {
        PrintWriter writer = new PrintWriter(pathToOutput, "UTF-8");
        for(String s : stringList) {
            writer.println(s);
        }
        writer.close();
    }

    public static void appendStringToEndOfFile(String string, String pathToOutputFile) {
        try {
            Writer output;
            output = new BufferedWriter(new FileWriter(pathToOutputFile, true));
            output.append(string+"\n");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeJSONToFile(JSONObject object, String pathToOutput) {
        Set<String> stringJSON = new HashSet<String>();


        //TODO: this
    }

    public static String[] removeEltFromStringArray(String[] toShorten, int index) {
        String[] newArr = new String[toShorten.length-1];

        LinkedList<String> temporary = new LinkedList<String>();
        for (int i=0; i<toShorten.length; i++) {
            if(i!=index) {
                temporary.add(toShorten[i]);
            }
        }
        for(int i=0;i<newArr.length; i++) {
            newArr[i]=temporary.get(i);
        }
        return newArr;
    }

    // convert filename to clean filename
    public static String convertToFileSystemChar(String name) {
        String erg = "";
        Matcher m = Pattern.compile("[a-z0-9 _#&@\\[\\(\\)\\]\\-\\.]", Pattern.CASE_INSENSITIVE).matcher(name);
        while (m.find()) {
            erg += name.substring(m.start(), m.end());
        }
        if (erg.length() > 500) {
            erg = erg.substring(0, 500);
            System.out.println("cut filename: " + erg);
        }
        erg=erg.replace("/", "");   //remove forward slashes.
        erg=erg.replace("(", "_");
        erg=erg.replace(")", "_");
        erg=erg.replace("?", "_");
        return erg;
    }
}