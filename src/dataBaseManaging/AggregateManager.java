package dataBaseManaging;
//@author : Yoo Sun Young e-mail : luysunyoung9@gmail.com

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class AggregateManager {
	public AggregateManager(){

	}
	
	public DBObject matching(ArrayList<DBObject> conditions){
		DBObject match = new BasicDBObject();
		DBObject and = new BasicDBObject();
		conditions.addAll(this.getBasicCondition());
		and.put("$and", conditions);
		match.put("$match", and);
		return match;
	}
	
	public ArrayList<DBObject> getBasicCondition(){
		ArrayList<DBObject> basic = new ArrayList<DBObject>();
		basic.add(new BasicDBObject("영업거리(M)",new BasicDBObject("$gt",5)));
		basic.add(new BasicDBObject("영업거리(M)",new BasicDBObject("$lte",500000)));
		basic.add(new BasicDBObject("운행시간",new BasicDBObject("$gt",5)));
		basic.add(new BasicDBObject("운행시간",new BasicDBObject("$lte",7200)));
		basic.add(new BasicDBObject("금액",new BasicDBObject("$gte",3000)));
		basic.add(new BasicDBObject("금액",new BasicDBObject("$lte",100000)));
		basic.add(new BasicDBObject("승차일시",new BasicDBObject("$ne",Long.parseLong("18991230000000"))));
		basic.add(new BasicDBObject("하차일시",new BasicDBObject("$ne",Long.parseLong("18991230000000"))));
		return basic;
	}
	
	public DBObject daymatchingCondition(String start_day,String end_day){
		if(start_day.length()==7){
			start_day = start_day + "040000";
		}
		if(end_day.length() == 7){
			end_day = end_day + "035959";
		}
		while(start_day.length()!=0&&start_day.length()<=13){
			start_day = start_day + "0";
		}
		while(end_day.length()!=0&&end_day.length()<=13){
			end_day = end_day + "0";
		}
		ArrayList<DBObject> start_end = new ArrayList<DBObject>();
		if(start_day.length()!=0){
			start_end.add(new BasicDBObject("수집일시" , new BasicDBObject("$gte",Long.parseLong(start_day))));
		}
		if(end_day.length()!=0){
			start_end.add(new BasicDBObject("수집일시" , new BasicDBObject("$lte",Long.parseLong(end_day))));
		}
		DBObject and = new BasicDBObject("$and",start_end);
		
		return and;
	}
	public DBObject matchingCondition(String key,Long value){
		DBObject cond = new BasicDBObject(key,value);
		return cond;
	}
	public DBObject matchingCondition(String key,String value){
		DBObject cond = new BasicDBObject(key,value);
		return cond;
	}
	public DBObject matchingCondition(String json){
		DBObject cond = (DBObject) JSON.parse(json);
		return cond;
	}
	
	
	public DBObject grouping(String _id,HashMap<String,Boolean> output){
		DBObject group = new BasicDBObject();
		switch(_id){
			case "null": group.put("$group", groupTotalField(output)); break;
			case "차량ID": group.put("$group", groupCarCountField()); break;
			case "결제구분": group.put("$group", groupPayCountField()); break;
		}
		return group;
	}
	
	public DBObject groupTotalField(HashMap<String,Boolean> output){
		
		DBObject groupFields = new BasicDBObject("_id","null");
		if(output.get("전체건수")){
			groupFields.put("전체건수", new BasicDBObject( "$sum", 1));
		}
		if(output.get("전체 운행 시간")){
			groupFields.put("전체 운행 시간", new BasicDBObject( "$sum", "$운행시간"));
		}
		if(output.get("전체 운행 거리")){
			groupFields.put("전체 운행 거리", new BasicDBObject( "$sum", "$영업거리(M)"));
		}
		if(output.get("전체 금액")){
			groupFields.put("전체 금액", new BasicDBObject( "$sum", "$금액"));
		}
		if(output.get("전체 공차 거리")){
			groupFields.put("전체 공차 거리", new BasicDBObject( "$sum", "$공차거리(M)"));
		}
		
		return groupFields;
	}
	
	public DBObject groupCarCountField(){
		DBObject groupcarid = new BasicDBObject();
		groupcarid.put("_id", "$차량ID");
		groupcarid.put("건수", new BasicDBObject( "$sum", 1));
		return groupcarid;
	}
	
	public DBObject groupPayCountField(){
		DBObject grouppayid = new BasicDBObject();
		grouppayid.put("_id", "$결제구분");
		grouppayid.put("결제건수", new BasicDBObject( "$sum", 1));
		grouppayid.put("전체 금액", new BasicDBObject( "$sum", "$금액"));
		return grouppayid;
	}
}
