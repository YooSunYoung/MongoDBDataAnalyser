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
		if(x_type == null || x_type == "����"){
			JComboBox j = (JComboBox) this.condition_setting_panel.getEnterComponent("����");
			x_type = (String) j.getSelectedItem();
			if(x_type == "��ü"){
				x_type = "����";
			}
		}
		return x_type;
	}
	
	public void initXAxisList(){
		this.x_axis_list = new HashMap<String,JCheckBox>();
		x_axis_list.put("����", new JCheckBox(""));
		x_axis_list.put("����", new JCheckBox(""));
		x_axis_list.put("��������", new JCheckBox(""));
		x_axis_list.put("�����ڵ�", new JCheckBox(""));
		
	}
	
	public void initConditionSettingPanel(){
		this.condition_setting_panel = new YSettingPanel();
		this.condition_setting_panel.setBounds(505, 10, this.condition_setting_panel.getWidth(), this.condition_setting_panel.getHeight()*2 - 80);
		String[] intervals = {"��ü","����","��","��","��","����"};
		String[] business_form = {"��ü","����","����"};
		String[] pay = {"��ü","���ݰŷ�","�ſ�ī��ŷ�","����ī��ŷ�"};
		String[] day_time = {"��ü","0(�Ϲ�)","1(�ɾ�)","3(�ð�)","7(�ɾ�+�ð�)"};
		this.condition_setting_panel.addEnterComponent("[Condition Settings]", new JLabel(""));
		this.condition_setting_panel.addEnterComponent("���۳�¥", new JTextField("20160501"));
		this.condition_setting_panel.addEnterComponent("���ᳯ¥", new JTextField("20160502"));
		this.condition_setting_panel.addEnterComponent("����", new JComboBox<String>(intervals));
		this.condition_setting_panel.addEnterComponent("����", new JComboBox<String>(business_form));
		this.condition_setting_panel.addEnterComponent("��������", new JComboBox<String>(pay));
		this.condition_setting_panel.addEnterComponent("�����ڵ�", new JComboBox<String>(day_time));
		this.condition_setting_panel.addEnterComponent("��Ÿ", new JTextField());
		this.condition_setting_panel.getEnterComponent("��Ÿ").setSize(200, 150);
		
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
		output_list.put("��ü�Ǽ�", new JCheckBox("��ü�Ǽ�"));
		output_list.put("��ü ���� �ð�", new JCheckBox("��ü ���� �ð�"));
		output_list.put("��ü ���� �Ÿ�", new JCheckBox("��ü ���� �Ÿ�"));
		output_list.put("��ü �ݾ�", new JCheckBox("��ü �ݾ�"));
		output_list.put("��ü ���� �Ÿ�", new JCheckBox("��ü ���� �Ÿ�"));
		output_list.put("����������", new JCheckBox("����������"));
//		output_list.put("���� �Ǽ�", new JCheckBox("���� �Ǽ�"));
//		output_list.put("���� �ݾ�", new JCheckBox("���� �ݾ�"));
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
		
		JTextField jf = (JTextField) this.condition_setting_panel.getEnterComponent("��Ÿ");
		result.put("��Ÿ", jf.getText());
		jf = (JTextField) this.condition_setting_panel.getEnterComponent("���۳�¥");
		result.put("���۳�¥", jf.getText());
		jf = (JTextField) this.condition_setting_panel.getEnterComponent("���ᳯ¥");
		result.put("���ᳯ¥", jf.getText());
		JComboBox<String> jb = (JComboBox<String>) this.condition_setting_panel.getEnterComponent("����");
		result.put("����", jb.getSelectedItem().toString());
		jb = (JComboBox<String>) this.condition_setting_panel.getEnterComponent("����");
		result.put("����", jb.getSelectedItem().toString());
		jb = (JComboBox<String>) this.condition_setting_panel.getEnterComponent("�����ڵ�");
		result.put("�����ڵ�", jb.getSelectedItem().toString());
		jb = (JComboBox<String>) this.condition_setting_panel.getEnterComponent("��������");
		result.put("��������", jb.getSelectedItem().toString());
		
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
