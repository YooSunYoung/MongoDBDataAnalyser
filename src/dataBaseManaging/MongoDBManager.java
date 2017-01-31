package dataBaseManaging;
//@author : Yoo Sun Young e-mail : luysunyoung9@gmail.com

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.*;

import dataFormatManaging.DataTransformer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MongoDBManager extends DBManager{
	private String raw_data_collection_name = "RawData";
	private String data_base_name = "TaxiData";
	private String default_address = "mongodb://58.229.253.182:50015";
	
	private DataTransformer transformer;
	
	private MongoClient mongo_client;
	private DB data_base;
	private DBCollection collection;
	private DBObject option;
	
	private JSONParser parser;
	
	public MongoDBManager(String address){
		this.setAddress(address);
		this.connectDB();
		this.init();
	}
	
	public void init(){
		parser = new JSONParser();
		transformer = new DataTransformer();
	}
	
	
	public void connectDB(){
		if(this.getAddress()==null){
			this.setAddress(default_address);
		}
		
		try {
			mongo_client = new MongoClient(new MongoClientURI(this.getAddress()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mongo_client.setWriteConcern(WriteConcern.JOURNALED);
		
		data_base = mongo_client.getDB(this.data_base_name);
		option = BasicDBObjectBuilder.start().add("capped", false).get();
		
		if(!data_base.collectionExists(this.raw_data_collection_name)){
			collection = data_base.createCollection(raw_data_collection_name, option);
		} else { collection = data_base.getCollection(raw_data_collection_name);}
		
	}
	
	
	public void connectDB(String address){
		this.setAddress(address);
		this.connectDB();
	}
	
	public void setCollectionName(String collection_name){
		this.collection = data_base.getCollection(collection_name);
	}
	
	public void setDBName(String db_name){
		this.data_base = this.mongo_client.getDB(db_name);
	}
	
	public String insertJSON(String json){
		return this.insertJSON(transformer.stringToJSONOB(json));
	}
	
	public String insertJSON(JSONObject jsonobj){
		BasicDBObject object = new BasicDBObject();
		
		object = new BasicDBObject(jsonobj);
		
		collection.save(object);
		return "";
	}

	
	
	public DBCursor queryToDB(JSONObject filter){
		DBCursor cursor;
		BasicDBObject filter_ob = new BasicDBObject(filter);
		cursor = collection.find(filter_ob);
		return cursor;
	}
	
	public DBCursor queryToDB(String filter_string){
		JSONObject filter = this.transformer.stringToJSONOB(filter_string);
		DBCursor cursor;
		BasicDBObject filter_ob = new BasicDBObject(filter);
		cursor = collection.find(filter_ob);
		return cursor;
	}
	
	public AggregationOutput aggregateToDB(List<DBObject> object){
		return this.collection.aggregate(object);
	}

	public AggregationOutput aggregateToDB(DBObject ob) {
		return this.collection.aggregate(ob);
	}
	
	public WriteResult remove(DBObject ob){
		WriteResult wr = this.collection.remove(ob);
		return wr;
	}
	
	
	
}
