package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DataProcessor {

	private BufferedReader br;
	
	ArrayList<String> vNegative = new ArrayList<String>();
	ArrayList<String> negative = new ArrayList<String>();
	
	ArrayList<String> vPositive = new ArrayList<String>();
	ArrayList<String> positive = new ArrayList<String>();
	
	ArrayList<String> stopWords = new ArrayList<String>();
	
	ArrayList<String> rawData = new ArrayList<String>();
	ArrayList<String> dataValue = new ArrayList<String>();
	
	ArrayList<String> feature = new ArrayList<String>();
	
	public void ReadDictionary(String filename)
	{
		File file = new File(filename);
		FileReader fr = null;
		try {
			fr = new FileReader(file);
			
			br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null){
			    
				String[] splited = new String[2];
				splited = line.split("\t");
				
				int score = Integer.parseInt(splited[1]);
				String value = splited[0];
				
				if(score == -4 || score == -5) {
		            vNegative.add(value);
				}else if(score == -3 || score == -2 || score == -1) {
		            negative.add(value);
				}else if( score == 3 || score == 2 || score == 1) {
		            positive.add(value);
				}else if( score == 4 || score == 5) {
		            vPositive.add(value);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void ReadStopWords(String filename)
	{
		File file = new File(filename);
		FileReader fr = null;
		try {
			fr = new FileReader(file);
			
			br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null){
			    
				stopWords.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void ReadData(String filename)
	{
		File file = new File(filename);
		FileReader fr = null;
		try {
			fr = new FileReader(file);
			
			br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null){
				String[] splited = line.split("\"");
				String[] splited2 = splited[2].split(" ");
				rawData.add(splited[1]);
				dataValue.add(splited2[1]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void PreProcessor()
	{
		for (int i=0; i<rawData.size(); i++) {
			
			String data = rawData.get(i);
			
			// make data lowercase
			data = data.toLowerCase();
			
			// remove stopwords from reviews
            for(int j=0; j<stopWords.size(); j++)
            {
            	
            	String regex = "\\b" + stopWords.get(j) + "\\b";
            	data = data.replaceAll(regex, "");
            }
            
            rawData.set(i, data);
		}
	}
	
	public void CreateFeatureVector()
	{
		
		int vNNumber = 0;
		int nNumber = 0;
		int vPNumber = 0;
		int pNumber = 0;
		for (int i=0; i<rawData.size(); i++) {
			
			vNNumber = 0;
			nNumber = 0;
			vPNumber = 0;
			pNumber = 0;
			
			String data = rawData.get(i);
			
			String[] token = data.split(" ");
			for(int j=0; j<token.length;j++)
			{
				if(vNegative.contains(token[j])) {
					vNNumber++;
				}
				if(negative.contains(token[j])) {
					nNumber++;
				}
				if(vPositive.contains(token[j])) {
					vPNumber++;
				}
				if(positive.contains(token[j])) {
					pNumber++;
				}
			}
			String fStr = vNNumber + ", " + nNumber + ", " + vPNumber + ", " + pNumber;
			feature.add(fStr);
			System.out.println(fStr);
		}
		
	}
	
	public void WriteFeatureVectorToFile(String filename)
	{
		File file = new File(filename);
		FileWriter fw;
		try {
			fw = new FileWriter(file);
		
			fw.write("@relation \"10K\"");
			fw.write(System.getProperty("line.separator"));
			fw.write(System.getProperty("line.separator"));
			
			fw.write("@attribute Very_Negative	NUMERIC");
			fw.write(System.getProperty("line.separator"));
			
			fw.write("@attribute Negative	NUMERIC");
			fw.write(System.getProperty("line.separator"));
			
			fw.write("@attribute Very_Positive	NUMERIC");
			fw.write(System.getProperty("line.separator"));
			
			fw.write("@attribute Positive	NUMERIC");
			fw.write(System.getProperty("line.separator"));
			
			fw.write("@attribute \"class\" {1,2,3,4,5");
			
			fw.write(System.getProperty("line.separator"));
			fw.write(System.getProperty("line.separator"));

			fw.write("@data");
			fw.write(System.getProperty("line.separator"));
			for (int i=0; i<feature.size(); i++) {
				
				String data = feature.get(i);
				
				fw.write(data+", "+dataValue.get(i));
				fw.write(System.getProperty("line.separator"));
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}