package dataBaseManaging;
//@author : Yoo Sun Young e-mail : luysunyoung9@gmail.com
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import dataFormatManaging.DataTransformer;
import ui.AnalysingUI;

public class DataBaseManager {
	public static final int MongoDB = 0;
	//public static final int MySQL = 1;
	
	private int database_type;
	
	private int start_index = 0;
	private int stop_index = 2000000;
	
	private MongoDBManager mongo_manager;
	
	
	private String workingDirectory;
	private boolean header = true;

	private DataReader data_reader;
	
	
	private String schema = "";
	private DataWriter data_writer;
	
	private DataTransformer data_transformer;
	
	private String splitter = "," ;
	
	public DataBaseManager(String database_url, int database_type){
		this.database_type = database_type;
		if(database_type == this.MongoDB){
			mongo_manager = new MongoDBManager(database_url);
		}
		this.data_transformer = new DataTransformer();
		this.data_reader = new DataReader();
		this.data_writer = new DataWriter();
	}
	public DataBaseManager(int database_type){
		this.database_type = database_type;
		this.data_transformer = new DataTransformer();
		this.data_reader = new DataReader();
		this.data_writer = new DataWriter();
	}
	
	public void setDBName(String db_name){
		this.mongo_manager.setDBName(db_name);
	}
	
	public void setCollectionName(String collection){
		this.mongo_manager.setCollectionName(collection);
	}
	
	public void setDataBaseName(String db_name){
		this.mongo_manager.setDBName(db_name);
	}
	public void setDataBaseURL(String url){
		if(database_type == this.MongoDB){
			mongo_manager = new MongoDBManager(url);
		}
	}
	public AggregationOutput aggregateToDB(List<DBObject> agg){
		return this.mongo_manager.aggregateToDB(agg);
	}

	
	
	public void setWorkingDirectory(String path){
		this.workingDirectory = path;
	}
	public void setWriterFilePath(String file_path) throws IOException{
		this.data_writer.setFilePath(file_path);
	}
	
	
	public void saveFilesFromDataBase(AggregationOutput output) throws IOException{
		Iterable<DBObject> result = output.results();
		this.saveFilesFromDataBase(result.iterator());
	}
	public void saveFilesFromDataBase(Iterator cursor) throws IOException{
		int i = 0;
		this.data_writer.OpenWriter();
		this.data_writer.writeLine("");
		while(cursor.hasNext()&&i<10){
			DBObject object = (DBObject) cursor.next();
			object.removeField("_id");
			String line = "";
			for (String field : object.keySet()){
				line += object.get(field);
				line += ",";
			}

			this.data_writer.writeLine(line);
//			System.out.println(line);
			i++;
		}
		this.data_writer.CloseWriter();

	}
	
	public void saveFilesFromDataBase(DBCursor cursor) throws IOException{
		int i = 0;
		this.data_writer.setFilePath("F://taxi//taxi.csv");
		this.data_writer.OpenWriter();
		this.data_writer.writeLine("");
		while(cursor.hasNext()&&i<10){
			DBObject object = cursor.next();
//			object.removeField("_id");
			String line = "";
			for (String field : object.keySet()){
				line += object.get(field);
				line += ",";
			}

			this.data_writer.writeLine(line);
			System.out.println(line);
			i++;
		}
		this.data_writer.CloseWriter();

	}
	
	public String getDayOfWeek(int num){
		String dayofweek = null;
		switch (num){
			case 1 :{
				dayofweek = "일";
			}
			case 2 : {
				dayofweek = "월";
			}
			case 3 : {
				dayofweek = "화";
			}
			case 4 : {
				dayofweek = "수";
			}
			case 5 : {
				dayofweek = "목";
			}
			case 6 : {
				dayofweek = "금";
			}
			case 7 : {
				dayofweek = "토";
			}
			
		}
		
		return dayofweek;
	}
	
	

	
	public void saveFilesFromDataBase(String x_axis_type,HashMap<String, DBObject> result_list, Set<String> x_axis, Set<String> y_axis) throws IOException {
		this.data_writer.OpenWriter();
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> x_list = new ArrayList<String>();
		ArrayList<String> y_list = new ArrayList<String>();
		for(String i : x_axis){
			x_list.add(i);
		}
		for(String i : y_axis){
			y_list.add(i);
		}
		String header = x_axis_type;
		for(String i : x_list){
			header = header + "," + i ;
		} result.add(header);
		for(String i : y_list){
			String line = i;
			for(String y : x_list){
				line += "," + result_list.get(y).get(i);
			}
			result.add(line);
		}
		for(String line:result){
			data_writer.writeLine(line);
		}
		data_writer.CloseWriter();
	}
	
		
	
	public boolean isNumeric(String str){
		boolean numeric=true;
		try {
			Float.parseFloat(str);
		}catch (NumberFormatException nfe){
			numeric = false;
		}
		return numeric;
	}

}
