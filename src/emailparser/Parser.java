package emailparser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This module contains a basic parser which uses regex's to read from an input
 * file and parse data in a typical e-mail.
 * 
 * @author Dhaval
 *
 */
public class Parser {
	private int lineNumber = 0;
	private String errorMessage = "";

	private Pattern startFile = Pattern.compile("^StartEmail$");
	private Pattern endFile = Pattern.compile("^EndEmail$");

	private Pattern addressTo = Pattern.compile("^To:\\w+@\\w+\\.\\w+$");
	private Pattern addressFrom = Pattern.compile("^From:\\w+@\\w+\\.\\w+$");

	private Pattern subject = Pattern.compile("^Subject:.*$");
	private Pattern body = Pattern.compile("^.*$");
	private Pattern space = Pattern.compile("^\\s*$");
	private Pattern endBody = Pattern.compile("^EndBody$");

	private void error(String message) {
		this.errorMessage = "Error in line " + this.lineNumber + ": " + message;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	/**
	 * Parse the content given in a file that follows a particular structure.
	 * As the content is parsed, it is stored into the model.
	 * 
	 * @param inputStream : File to parse data from
	 * @param em : model that stores parsed data
	 * @return true if the file is successfully parsed
	 */
	public boolean parse(BufferedReader inputStream, EmailModel em) {
		this.errorMessage = "";

		try {
			int state = 0;
			Matcher m;
			String l;

			this.lineNumber = 0;
			while ((l = inputStream.readLine()) != null) {
//				l = l.replaceAll("\\s+", "");
				System.out.println(this.lineNumber + " " + l + " state:" + state);
				this.lineNumber++;
				
				// skip empty lines if its not part of body message. Otherwise, 
				// copy the newlines when reading body message.
				if (!l.equals("") || state == 4) {

					switch (state) {
					case 0:
						// read file start
						l = l.replaceAll("\\s+", "");
						m = startFile.matcher(l);
						if (m.matches()) {
							state = 1;
							break;
						}
						error("Expected start of file");
						return false;

					case 1:
						// read address to
						l = l.replaceAll("\\s+", "");
						m = addressTo.matcher(l);
						if (m.matches()) {
							state = 2;
							// store address to
							em.setAddressTo(l.substring(3));
							break;
						}
						error("Expected addressed to");
						return false;

					case 2:
						// read address from
						l = l.replaceAll("\\s+", "");
						m = addressFrom.matcher(l);
						if (m.matches()) {
							state = 3;
							// store address from
							em.setAddressFrom(l.substring(5));
							break;
						}
						error("Expected addressed from");
						return false;

					case 3:
						// read subject
						m = subject.matcher(l);
						if (m.matches()) {
							state = 4;
							// store subject
							em.setSubject(l.substring(8));
							break;
						}
						error("Expected subject line");
						return false;

					case 4:
						// read body message
						m = space.matcher(l);
						if (m.matches()) {
							em.setBody("\n");
							break;
						}
						m = body.matcher(l);						
						if (m.matches() && !endBody.matcher(l).matches()) {
							// store message
							em.setBody(l);
							break;
						}
						state = 5; // end of body message

					case 5:
						// read end of body
						l = l.replaceAll("\\s+", "");
						m = endBody.matcher(l);
						if (m.matches()) {
							state = 6;
							break;
						}
						error("Expected end of body message");
						return false;

					case 6:
						// read end of file
						l = l.replaceAll("\\s+", "");
						m = endFile.matcher(l);
						if (m.matches()) {
							state = 7;
							break;
						}
						error("Expected end of file");
						return false;

					}
				}
			}

			return state == 7;

		} catch (Exception e) {
			error(e.toString());
			return false;
		}
	}
	
	/**
	 * Quick way to test out a file without running the entire application.
	 * 
	 */
	public static void main(String[] args) {
		EmailModel em = new EmailModel();
		Parser p = new Parser();
		BufferedReader inputStream = null;
		try {
			inputStream = new BufferedReader(new FileReader("sampletests/basic_email.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(p.getErrorMessage());
		System.out.println(p.parse(inputStream, em));
	}
}
