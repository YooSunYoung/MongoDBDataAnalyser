package ui;
//@author : Yoo Sun Young e-mail : luysunyoung9@gmail.com

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

import analysisEngine.AnalysingManager;

public class AnalysingListener implements ActionListener{
	private AnalysingManager am;
	
	public AnalysingListener(AnalysingManager am){
		this.am = am;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.contentEquals("analysis")){
			try {
				am.analysing();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(command.contentEquals("summary")){
			try {
				am.summary();
			} catch (ParseException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
}
