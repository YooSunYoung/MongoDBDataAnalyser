package ui;
//@author : Yoo Sun Young e-mail : luysunyoung9@gmail.com

import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dataBaseManaging.DataBaseManager;
import ycommonUI.frame.YFrame;
import ycommonUI.panel.control.YControlPanel;
import ycommonUI.panel.setting.YSettingPanel;

public class AnalysingUI {
	private HashMap<String,String> settings;
	private JFrame frame;

	private YSettingPanel path_setting_panel;
	private YSettingPanel condition_setting_panel;
	private YSettingPanel output_setting_panel;
	private YControlPanel control_panel;
	
	private HashMap<String,JCheckBox> output_list;
	private HashMap<String,JCheckBox> x_axis_list;
	

	
	public AnalysingUI(){
		this.init();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void init(){
		frame = new JFrame();


		this.initPathSettingPanel();
		this.initXAxisList();
		this.initConditionSettingPanel();
		this.initOutputList();
		this.initOutputSettingPanel();
		this.initControlPanel();
		
		frame.setTitle("");
		frame.add(path_setting_panel);
		frame.add(condition_setting_panel);
		frame.add(output_setting_panel);
		frame.add(control_panel);
		frame.setLayout(null);

		this.initFrame();

		this.frame.setVisible(true);
	}
	
	public void initFrame(){
		this.frame.setBounds(0,0,980,800);
		frame.setTitle("Taxi Analysis");
	}
	
	public void initPathSettingPanel(){
		path_setting_panel = new YSettingPanel();
		this.path_setting_panel.addEnterComponent("[Path Settings]", new JLabel(""));
		this.path_setting_panel.addEnterComponent("db server url", new JTextField("mongodb://localhost:27017"));
		this.path_setting_panel.addEnterComponent("db name", new JTextField("test"));
		this.path_setting_panel.addEnterComponent("collection name", new JTextField("taxi_20160501"));
		this.path_setting_panel.addEnterComponent("file name", new JTextField("C://test.csv"));
	}
	
	public String getXAxistType(){
		String x_type = null;
		for (String key : x_axis_list.keySet()){
			if(x_axis_list.get(key).isSelected()){
				x_type = key;
			}
		}
		if(x_type == null || x_type == "간격"){
			JComboBox j = (JComboBox) this.condition_setting_panel.getEnterComponent("간격");
			x_type = (String) j.getSelectedItem();
			if(x_type == "전체"){
				x_type = "요일";
			}
		}
		return x_type;
	}
	
	public void initXAxisList(){
		this.x_axis_list = new HashMap<String,JCheckBox>();
		x_axis_list.put("간격", new JCheckBox(""));
		x_axis_list.put("구분", new JCheckBox(""));
		x_axis_list.put("결제수단", new JCheckBox(""));
		x_axis_list.put("할증코드", new JCheckBox(""));
		
	}
	
	public void initConditionSettingPanel(){
		this.condition_setting_panel = new YSettingPanel();
		this.condition_setting_panel.setBounds(505, 10, this.condition_setting_panel.getWidth(), this.condition_setting_panel.getHeight()*2 - 80);
		String[] intervals = {"전체","연도","월","일","시","요일"};
		String[] business_form = {"전체","개인","법인"};
		String[] pay = {"전체","현금거래","신용카드거래","선불카드거래"};
		String[] day_time = {"전체","0(일반)","1(심야)","3(시계)","7(심야+시계)"};
		this.condition_setting_panel.addEnterComponent("[Condition Settings]", new JLabel(""));
		this.condition_setting_panel.addEnterComponent("시작날짜", new JTextField("20160501"));
		this.condition_setting_panel.addEnterComponent("종료날짜", new JTextField("20160502"));
		this.condition_setting_panel.addEnterComponent("간격", new JComboBox<String>(intervals));
		this.condition_setting_panel.addEnterComponent("구분", new JComboBox<String>(business_form));
		this.condition_setting_panel.addEnterComponent("결제수단", new JComboBox<String>(pay));
		this.condition_setting_panel.addEnterComponent("할증코드", new JComboBox<String>(day_time));
		this.condition_setting_panel.addEnterComponent("기타", new JTextField());
		this.condition_setting_panel.getEnterComponent("기타").setSize(200, 150);
		
		for (String key : this.x_axis_list.keySet()){
			JCheckBox j = this.x_axis_list.get(key);
			int x = this.condition_setting_panel.getEnterComponent(key).getX();
			int y = this.condition_setting_panel.getEnterComponent(key).getY();
			j.setBounds(x - 30, y + 5 , 30, 30);
			this.condition_setting_panel.add(j);	
		}
		
	}
	
	public void initOutputList(){
		this.output_list = new HashMap<String,JCheckBox>();
		output_list.put("전체건수", new JCheckBox("전체건수"));
		output_list.put("전체 운행 시간", new JCheckBox("전체 운행 시간"));
		output_list.put("전체 운행 거리", new JCheckBox("전체 운행 거리"));
		output_list.put("전체 금액", new JCheckBox("전체 금액"));
		output_list.put("전체 공차 거리", new JCheckBox("전체 공차 거리"));
		output_list.put("운행차량수", new JCheckBox("운행차량수"));
//		output_list.put("결제 건수", new JCheckBox("결제 건수"));
//		output_list.put("결제 금액", new JCheckBox("결제 금액"));
	}

	public void initOutputSettingPanel(){
		this.output_setting_panel = new YSettingPanel();
		this.output_setting_panel.setBounds(10,400,this.output_setting_panel.getWidth(),this.output_setting_panel.getHeight());
		this.output_setting_panel.addEnterComponent("[Output Settings]", new JLabel(""));
		int i = 0;
		int width = this.output_setting_panel.getComponent_width();
		int height = this.output_setting_panel.getComponent_height();
		int interval = this.output_setting_panel.getComponent_interval();
		for (String key : output_list.keySet()){
			JCheckBox j = output_list.get(key);
			this.output_setting_panel.add(j);
			j.setSelected(true);
			if(i>2){
				j.setBounds(width + 2*interval, (i-2)*height + (i-1)*interval , width,height);
			} else if (i>=0 && i <3){
				j.setBounds(interval, (i+1)*height + (i+2)*interval , width, height);
			}
			i++;
		}
	}
	
	public void addActionListener(AnalysingListener al){
		this.control_panel.getStopButton().addActionListener(al);
		this.control_panel.getStartButton().addActionListener(al);
	}
	
	public void initControlPanel(){
		this.control_panel = new YControlPanel();
		this.control_panel.setBounds(this.condition_setting_panel.getX(), 655, this.control_panel.getWidth(), 90);
		this.control_panel.setName("Analysis");
		this.control_panel.getStartButton().setText("analysis");
		this.control_panel.getStartButton().setActionCommand("analysis");
		this.control_panel.getStopButton().setText("summary");
		this.control_panel.getStopButton().setActionCommand("summary");
	}
	
	public HashMap<String,String> getPathSettings(){
		HashMap<String,String> result = new HashMap<String,String>();
		
		JTextField jf = (JTextField) this.path_setting_panel.getEnterComponent("db server url");
		result.put("db_server_url", jf.getText());
		jf = (JTextField) this.path_setting_panel.getEnterComponent("db name");
		result.put("db_name", jf.getText());
		jf = (JTextField) this.path_setting_panel.getEnterComponent("collection name");
		result.put("collection_name", jf.getText());
		jf = (JTextField) this.path_setting_panel.getEnterComponent("file name");
		result.put("file_name", jf.getText());
		
		return result;
	}
	public HashMap<String, String> getConditionSettings(){
		HashMap<String,String> result = new HashMap<String,String>();
		
		JTextField jf = (JTextField) this.condition_setting_panel.getEnterComponent("기타");
		result.put("기타", jf.getText());
		jf = (JTextField) this.condition_setting_panel.getEnterComponent("시작날짜");
		result.put("시작날짜", jf.getText());
		jf = (JTextField) this.condition_setting_panel.getEnterComponent("종료날짜");
		result.put("종료날짜", jf.getText());
		JComboBox<String> jb = (JComboBox<String>) this.condition_setting_panel.getEnterComponent("간격");
		result.put("간격", jb.getSelectedItem().toString());
		jb = (JComboBox<String>) this.condition_setting_panel.getEnterComponent("구분");
		result.put("구분", jb.getSelectedItem().toString());
		jb = (JComboBox<String>) this.condition_setting_panel.getEnterComponent("할증코드");
		result.put("할증코드", jb.getSelectedItem().toString());
		jb = (JComboBox<String>) this.condition_setting_panel.getEnterComponent("결제수단");
		result.put("결제수단", jb.getSelectedItem().toString());
		
		return result;
	}
	
	public HashMap<String,Boolean> getOutputList(){
		HashMap<String,Boolean> output = new HashMap<String,Boolean>();
		for(String key : this.output_list.keySet()){
			output.put(key, this.output_list.get(key).isSelected());
		}
		return output;
	}
	

	
	public static void main(String[] args){
		AnalysingUI ui = new AnalysingUI();
		System.out.println(ui.getPathSettings());
		System.out.println(ui.getConditionSettings());
	}
}
