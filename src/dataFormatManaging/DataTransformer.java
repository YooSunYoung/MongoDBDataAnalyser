package dataFormatManaging;
//@author : Yoo Sun Young e-mail : luysunyoung9@gmail.com

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataTransformer {
	private JSONParser parser;
	
	
	
	public DataTransformer(){
		parser = new JSONParser();
	}
	
	public String listToJSONString(String[] schema, String[] data){
		String result = "{ ";
		if(true){
				//schema.length==data.length){
			for (int i = 0; i < schema.length; i++){
				result += schema[i] ;
				result += " : ";
				result += data[i];
				if(i == schema.length - 1){
					result += " }";
				} else  { result += " , "; }
			}
			return result;
		}
		else {
			String error = "the length of the schema and the data must be same!";
			System.out.println(error);
			result = null;
			return result;
		}
	}
	
	public JSONObject stringToJSONOB(String json){
		JSONObject jsonobj = new JSONObject();
		try {
			jsonobj = (JSONObject)parser.parse(json);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonobj;
	}
	
	
}
