package dataBaseManaging;
//@author : Yoo Sun Young e-mail : luysunyoung9@gmail.com

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DataWriter {
	String file_path = null;
	
	FileWriter file_writer;
	BufferedWriter buffered_writer;
	public DataWriter(){
		
	}
	public DataWriter(String file_path) {
		try {
			file_writer = new FileWriter(file_path);
		} catch (IOException e) {
			System.out.println("Please check the path of the file or read&write permission.");
			e.printStackTrace();
		}
		
		buffered_writer = new BufferedWriter(file_writer);
		
		this.file_path = file_path;
	}
	
	public void setFilePath(String file_path) throws IOException{
		this.file_path = file_path;
		
		if(this.buffered_writer!=null){
			this.CloseWriter();
		}
		
		this.file_writer = new FileWriter(file_path);
		this.buffered_writer = new BufferedWriter(file_writer);
	}
	
	public void writeLine(String line) throws IOException{
		if(!line.endsWith("\n")){
			line = line + "\n";
		}
		buffered_writer.write(line);
		buffered_writer.flush();
	}
	
	public void writeLines(String[] lines) throws IOException{
		for (int i = 0 ; i < lines.length ; i ++){
			this.writeLine(lines[i]);
		}
	}

	public void OpenWriter() throws IOException{
		if(file_path==null){
			System.out.println("Open a file with default path C:\\");
			this.file_path = "C:\\";
		}
		file_writer = new FileWriter(this.file_path);
		buffered_writer = new BufferedWriter(this.file_writer);
	}

	public void CloseWriter() throws IOException{
		buffered_writer.close();
		file_writer.close();
	}
	
	
	
	
	
	public static void main(String[] agrs) throws IOException{
		DataWriter dw = new DataWriter("C:\\newsun.csv");
		dw.writeLine("Hello Worlds!");
		dw.writeLine("Hello Worlds!");
	}

}
