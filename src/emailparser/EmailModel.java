package emailparser;

import java.util.ArrayList;

/**
 * This module stores the main information from the parser. 
 * @author Dhaval
 *
 */
public class EmailModel {
	
	private String addressTo;
	private String addressFrom;
	private String subject;
	private String body;
	
	public EmailModel() {
		this.addressTo = "";
		this.addressFrom = "";
		this.subject = "";
		this.body = "";
	}
	
	public void setAddressTo(String addressTo) {
		this.addressTo = addressTo;
	}
	
	public void setAddressFrom(String addressFrom) {
		this.addressFrom = addressFrom;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setBody(String body) {
		this.body += body;
	}
	
	public String[] getInfo() {
		String[] array = {this.addressTo, this.addressFrom, this.subject, this.body};
		return array;
	}

}
