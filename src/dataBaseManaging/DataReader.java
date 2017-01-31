package dataBaseManaging;
//@author : Yoo Sun Young e-mail : luysunyoung9@gmail.com

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import dataFormatManaging.DataTransformer;

public class DataReader {
	FileInputStream file_input_stream;
	InputStreamReader input_stream_reader;
	BufferedReader buffered_reader;
	
	String file_path = null;
	
	public DataReader(){
		
	}
	
	public DataReader(String file_path) throws FileNotFoundException{
		this.setFilePath(file_path);
	}
	
	public void setFilePath(String file_path) throws FileNotFoundException{
		this.file_path = file_path;
		this.openReader();
	}
	public void openReader() throws FileNotFoundException{
		if(new File(this.file_path).exists()){
			file_input_stream = new FileInputStream(file_path);
			input_stream_reader = new InputStreamReader(file_input_stream);
			buffered_reader = new BufferedReader(input_stream_reader);
		}
		else {
			System.out.println("Please check the path of the file.");
		}
	}
	public void closeReader() throws IOException{
		this.buffered_reader.close();
		this.input_stream_reader.close();
		this.file_input_stream.close();
	}
	
	public String getLineFromFile() throws IOException{
		return buffered_reader.readLine();
	}
	public boolean hasNext() throws IOException{
		boolean next = false;
		
		next = this.buffered_reader.ready();
		return next;
	}
	

	public static void main(String[] args) throws IOException{
		DataReader df = new DataReader("F:\\taxi\\taxi_2015_01.csv");
		System.out.println(df.getLineFromFile().split(",")[0].replace("\"", ""));
		System.out.println(df.getLineFromFile().split(",").length);
	}
}
