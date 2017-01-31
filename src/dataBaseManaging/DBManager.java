
package dataBaseManaging;
//@author : Yoo Sun Young e-mail : luysunyoung9@gmail.com

public class DBManager {
	private String port_number;
	private String server_url;
	private String address;

	public DBManager(){
		String msg = "created data base manager object. address of the data base must be set first.";
		System.out.println(msg);
	}
	
	public DBManager(String address){
		this.setAddress(address);
	}
	
	public DBManager(String server_url, String port_number){
		this.setServerURL(server_url);
		this.setPortNumber(port_number);
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
	public void setPortNumber(String port_number){
		this.port_number = port_number;
	}
	
	public String getPortNumber(){
		return this.port_number;
	}
	
	public void setServerURL(String server_url){
		this.server_url = server_url;
	}
	
	public String getServerURL(){
		return this.server_url;
	}
	
}
