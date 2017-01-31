package analysisEngine;
//@author : Yoo Sun Young e-mail : luysunyoung9@gmail.com
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import dataBaseManaging.AggregateManager;
import dataBaseManaging.DataBaseManager;
import ui.AnalysingListener;
import ui.AnalysingUI;

public class AnalysingManager {
	private DataBaseManager manager;
	private AnalysingUI analysing_ui;
	private AnalysingListener analysing_listener;
	private AggregateManager am;
	
	public static Long[] hours = {(long) 4,(long) 5,(long) 6,(long) 7,(long) 8,(long) 9,(long) 10,(long) 11,(long) 12,(long) 13,(long) 14,(long) 15,(long) 16,(long) 17,(long) 18,(long) 19,(long) 20,(long) 21,(long) 22,(long) 23,(long) 0,(long) 1,(long) 2,(long) 3};
	public static String[] weekday = {"월","화","수","목","금","토","일"};
	public final static String[] business_type = {"개인","법인"};
	public final static String[] pay_type = {"현금거래","신용카드거래","선불카드거래"};
	public final static Long[] premium_type = {(long) 0,(long) 1,(long) 3,(long) 7};
	
	public final static String DATATRANSPORTING = "transport";
	public final static String DATAANALYSING = "analysing";
	
	public AnalysingManager(){
		initAnalysing();
	}
	
	public void initAnalysing(){
		this.manager = new DataBaseManager(DataBaseManager.MongoDB);
		this.analysing_ui = new AnalysingUI();
		this.analysing_listener = new AnalysingListener(this);
		analysing_ui.addActionListener(analysing_listener);
		am = new AggregateManager();
		
	}
	
	public void setPathSettings(){
		HashMap<String,String> settings = this.analysing_ui.getPathSettings();
		String url = settings.get("db_server_url");
		String db_name = settings.get("db_name");
		String collection_name = settings.get("collection_name");
		String file_path = settings.get("file_name");
		this.manager.setDataBaseURL(url);
		this.manager.setDataBaseName(db_name);
		this.manager.setCollectionName(collection_name);
		try {
			this.manager.setWriterFilePath(file_path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void analysing() throws IOException{
		this.setPathSettings();
		System.out.println(this.analysing_ui.getConditionSettings());

		HashMap<String,String> conditions = this.analysing_ui.getConditionSettings();
		HashMap<String,List<DBObject>> aggs = new HashMap<String,List<DBObject>>();
		String interval = conditions.remove("간격");
		
		HashMap<String,String> temp = conditions;
		

		DBObject group = am.grouping("null",this.analysing_ui.getOutputList());
		System.out.println(group);
		
		String x_type = this.analysing_ui.getXAxistType();
		
		for(DBObject x_axis : this.getXAxisCondition()){
			conditions = temp;
			ArrayList<DBObject> matchingFields = this.getConditions(conditions);
			matchingFields.add(x_axis);
			DBObject match = am.matching(matchingFields);
			ArrayList<DBObject> agg = new ArrayList<DBObject>();
			agg.add(match);
			agg.add(group);
			aggs.put(String.valueOf(x_axis.get(x_type)),agg);
		}
		
		HashMap<String,DBObject> result_list = new HashMap<String,DBObject>();
		
		for(String x_axis : aggs.keySet()){
			List<DBObject> agg = aggs.get(x_axis);
			
			AggregationOutput output = this.manager.aggregateToDB(agg);
			Iterable<DBObject> result_cursor = output.results();
			Iterator cursor = result_cursor.iterator();
			if(cursor.hasNext()){
				DBObject result =  (DBObject) cursor.next();
				System.out.println(result);
				if(this.analysing_ui.getOutputList().get("운행차량수")){
					ArrayList<DBObject> aggg = new ArrayList<DBObject>();
					aggg.add(agg.get(0));
					aggg.add(this.am.grouping("차량ID", this.analysing_ui.getOutputList()));
					System.out.println(aggg);
					output = this.manager.aggregateToDB(aggg);
					Long count = (long) 0;
					Iterator i = output.results().iterator();
					while(i.hasNext()){
						i.next();
						count++;
					}
					result.put("운행차량수", count);
				}
				result_list.put(x_axis,result);
			}
			
		}
		Set<String> y_axis = new HashSet<String>();
		for(String key:this.analysing_ui.getOutputList().keySet()){
			if(this.analysing_ui.getOutputList().get(key)){
				y_axis.add(key);
			}
		}
		this.manager.saveFilesFromDataBase(x_type,result_list,result_list.keySet(),y_axis);
		
		System.out.println(result_list);

	}
	
	public ArrayList<DBObject> getXAxisCondition(){
		String x_type = this.analysing_ui.getXAxistType();
		ArrayList<DBObject> x_axis = new ArrayList<DBObject>();
		if(x_type == "연도"){
			Long s_year = Long.parseLong(this.analysing_ui.getConditionSettings().get("시작날짜").substring(0, 4));
			Long e_year = Long.parseLong(this.analysing_ui.getConditionSettings().get("종료날짜").substring(0, 4));
			
			for (Long i = s_year; i <= e_year ; i ++){
				x_axis.add(am.matchingCondition("연도", i));
			}
		}else if(x_type == "월"){
			for (Long i = (long) 1; i <= 12 ; i ++){
				x_axis.add(am.matchingCondition("월", i));
			}
		}else if(x_type == "일"){
			for (Long i = (long) 1; i <= 31 ; i ++){
				x_axis.add(am.matchingCondition("일", i));
			}
		}
		else if(x_type == "시"){
			for (Long i : this.hours){
				x_axis.add(am.matchingCondition("시", i));
			}
		}
		else if(x_type == "요일"){
			for (String i : this.weekday){
				x_axis.add(am.matchingCondition("요일", i));
			}
		}
		else if(x_type == "결제수단"){
			for (String i : this.pay_type){
				x_axis.add(am.matchingCondition("결제구분", i));
			}
		}
		else if(x_type == "구분"){
			for (String i : this.business_type){
				x_axis.add(am.matchingCondition("구분", i));
			}
		}else if(x_type == "할증코드"){
			for (Long i : this.premium_type){
				x_axis.add(am.matchingCondition("할증코드", i));
			}
		}
		
		
		return x_axis;
	}
	
	public ArrayList<DBObject> getConditions(HashMap<String,String> conditions){
		ArrayList<DBObject> cond = new ArrayList<DBObject>();
		String start = conditions.get("시작날짜");
		String stop = conditions.get("종료날짜");
		String etc = conditions.get("기타");
		DBObject day_match_field = am.daymatchingCondition(start, stop);
		cond.add(day_match_field);
		if(etc.length()!=0 && etc.startsWith("{") && etc.endsWith("}")){
			DBObject etc_match_field = (DBObject) JSON.parse(etc);
			cond.add(etc_match_field);
		}
		for(String key: conditions.keySet()){
			if(conditions.get(key)!="전체"&&conditions.get(key)!=""){
				if(key=="할증코드"){
					Long v = Long.parseLong(conditions.get(key).substring(0, 1));
					cond.add(am.matchingCondition(key, v));
				} else if(key=="구분"||key=="결제수단"){
					cond.add(am.matchingCondition(key, conditions.get(key)));	
				}
			}
		}
		return cond;
	}
	
	
	public void summary() throws ParseException, IOException{
		this.setPathSettings();
		System.out.println(this.analysing_ui.getConditionSettings());
		SimpleDateFormat ff = new SimpleDateFormat("yyyyMMddhhmmss");
		HashMap<String,String> conditions = this.analysing_ui.getConditionSettings();
		HashMap<String,List<DBObject>> aggs = new HashMap<String,List<DBObject>>();
		String interval = conditions.remove("간격");
		
		HashMap<String,String> temp = conditions;
		
		String x_type = this.analysing_ui.getXAxistType();
		
		DBObject group_field = new BasicDBObject("_id","$차량ID");
		group_field.put("count", new BasicDBObject("$sum",1));
		group_field.put("시작시간", new BasicDBObject("$min","$승차일시"));
		group_field.put("끝시간", new BasicDBObject("$max","$하차일시"));
		group_field.put("금액", new BasicDBObject("$sum","$금액"));
		group_field.put("영업거리", new BasicDBObject("$sum","$영업거리(M)"));
		group_field.put("공차거리", new BasicDBObject("$sum","$공차거리(M)"));

		DBObject group = new BasicDBObject("$group",group_field);
		
		for(DBObject x_axis : this.getXAxisCondition()){
			conditions = temp;
			ArrayList<DBObject> matchingFields = this.getConditions(conditions);
			matchingFields.add(x_axis);
			DBObject match = am.matching(matchingFields);
			ArrayList<DBObject> agg = new ArrayList<DBObject>();
			agg.add(match);
			agg.add(group);
			aggs.put(String.valueOf(x_axis.get(x_type)),agg);
		}
		
		HashMap<String,DBObject> result_list = new HashMap<String,DBObject>();
		
		for(String x_axis : aggs.keySet()){
			List<DBObject> agg = aggs.get(x_axis);
			
			AggregationOutput output = this.manager.aggregateToDB(agg);
			Iterable<DBObject> result_cursor = output.results();
			Iterator cursor = result_cursor.iterator();
			if(cursor.hasNext()){
			Long tot_taxi = (long) 0;
			Long money = (long) 0;
			Long count = (long) 0;
			Long event_count = (long) 0;
			Long tot_wl = (long) 0;
			Long tot_wtl = (long) 0;
			Long asd = (long) 0;
			Long[] score = new Long[8] ;
			for(int j = 0 ; j < 8 ; j++){
				score[j] = (long) 0;
			}
			Long tot_score = (long) 0;
			while(cursor.hasNext()){
				asd++;
				DBObject taxi = (DBObject) cursor.next();
				tot_taxi += 1;
				String end = String.valueOf(taxi.get("끝시간"));
				String start = String.valueOf(taxi.get("시작시간"));
				Date end_d = ff.parse(end);
				Date start_d = ff.parse(start);
				
				Long time = end_d.getTime() - start_d.getTime();

				Long working_length = (Long) taxi.get("영업거리");
				Long waiting_length = (Long) taxi.get("공차거리");
				
				Long percent = working_length*100/(working_length+waiting_length);
				if(time>=21*60*60*1000&&working_length>=350000&&percent >= 50){
					money += (Long) taxi.get("금액");
					count += 1;
					event_count += Long.parseLong(Integer.toString((int) taxi.get("count")));
					tot_wl += working_length;
					tot_wtl += waiting_length;

					System.out.println(end+"\t"+start+"\t"+working_length+"\t"+taxi.get("금액")+"\t"+taxi.get("count"));
				}
				if(time>=10*60*60*1000){
					score[7] ++;
				}else if(time>=9*60*60*1000&&time<10*60*60*1000){
					score[6] ++;
				}else if(time>=8*60*60*1000&&time<9*60*60*1000){
					score[5] ++;
				}else if(time>=7*60*60*1000&&time<8*60*60*1000){
					score[4] ++;
				}else if(time>=6*60*60*1000&&time<7*60*60*1000){
					score[3] ++;
				}else if(time>=5*60*60*1000&&time<6*60*60*1000){
					score[2] ++;
				}else if(time>=4*60*60*1000&&time<5*60*60*1000){
					score[1] ++;
				}else if(time<4*60*60*1000){
					score[0] ++;
				}
				
			}

			for(int j = 0 ; j < 8 ; j ++){
				tot_score += (long) (score[j] * ((j+1)*0.125));
			}
			
			DBObject result = new BasicDBObject();
			
			result.put("총", String.valueOf(asd));
			result.put("총 차량 대수", String.valueOf(count));
			result.put("총 결제 건수", String.valueOf(event_count));
//			result.put("평균 결제 건수", String.valueOf(event_count/count));
			result.put("공차 거리", String.valueOf(tot_wtl));
			result.put("영업 거리", String.valueOf(tot_wl));
			result.put("총 금액", String.valueOf(money));
			result.put("당시총차량", String.valueOf(tot_taxi));
			result.put("당시가동률", String.valueOf(tot_score));
			
			result_list.put(x_axis, result);
			}
		}
		Set<String> y_axis = new HashSet<String>();
				y_axis.add("총");
				y_axis.add("총 차량 대수");
				y_axis.add("총 결제 건수");
				y_axis.add("공차 거리");
				y_axis.add("영업 거리");
				y_axis.add("총 금액");
				y_axis.add("당시총차량");
				y_axis.add("당시가동률");
		this.manager.saveFilesFromDataBase(x_type,result_list,result_list.keySet(),y_axis);
		
		System.out.println(result_list);
	}
	
	public static void main(String[] args){
		AnalysingManager am = new AnalysingManager();
	}

}
