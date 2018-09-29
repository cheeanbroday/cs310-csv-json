package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            // INSERT YOUR CODE HERE
            
            String[] line = iterator.next();
            
            ArrayList<List> data = new ArrayList<>();
            ArrayList<String> header = new ArrayList<>();
            ArrayList<String> row = new ArrayList<>();
            JSONObject jsonObject = new JSONObject();
            
            for(int i = 0; i < line.length; i++){
                header.add(line[i]);
            }
            
            
            while(iterator.hasNext()){
                line = iterator.next();
                ArrayList<Integer> ints = new ArrayList<>();
                for(int i = 0; i < line.length; i++){
                    if(i == 0){ row.add(line[i]); }
                    else{
                        
                        ints.add(Integer.parseInt(line[i]));
                        
                    }
                }
                data.add(ints);
            }
            jsonObject.put("rowHeaders", row);
            
            jsonObject.put("data", data);
            
            jsonObject.put("colHeaders", header);
            
            
            String jsonString = JSONValue.toJSONString(jsonObject);
            results = jsonString;
            
        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            // INSERT YOUR CODE HERE
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject)parser.parse(jsonString);
            
            
            //Make contrainer objects and parser
            JSONArray data = (JSONArray)json.get("data");
            JSONArray colHeaders = (JSONArray)json.get("colHeaders");
            JSONArray rowHeaders = (JSONArray)json.get("rowHeaders");
            
            
            //create string arrays for col and row
            String[] colStrings = new String[colHeaders.size()];
            String[] rowStrings = new String[rowHeaders.size()];
            String[] dataStrings = new String[data.size()];
            
            
            //fill string arrays
            for(int i = 0; i < colHeaders.size(); i++){
                colStrings[i] = colHeaders.get(i).toString();
            }
            csvWriter.writeNext(colStrings); //add col headers to csv
            for(int i = 0; i < rowHeaders.size(); i++){
                rowStrings[i] = rowHeaders.get(i).toString();
            }
            for(int i = 0; i < data.size(); i++){
                dataStrings[i] = data.get(i).toString();
            }
            
            //combine rowStrings and data strings to form final rows
            for(int i = 0; i < dataStrings.length; i++){
                JSONArray dataValues = (JSONArray)parser.parse(dataStrings[i]);
                String[] row = new String[dataValues.size() +1];
                row[0] = rowStrings[i];
                for(int j = 0; j < dataValues.size(); j ++){
                    row[j+1] = dataValues.get(j).toString();
                }
                csvWriter.writeNext(row);
            }
            
            //print writer
            results = writer.toString();
        }
        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }

}