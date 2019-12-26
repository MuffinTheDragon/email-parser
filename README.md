# email-parser
Parser written in Java to extract and organize e-mails from an input file. User can write an e-mail using the application and save it into a file. User can also open a file and display its contents in the application.


-------------------------------------------------------------------------------------
File Format For E-mail Files.
All files must follow this general structure in order to be parsed by the application.

Whitespace can vary between files. The parser should be able to parse any file
with any kind of whitespace (assuming the general structure is followed). 

The whitespace within the body message will be copied exactly as written by the parser.

------------------------------------------------------------------------------------- 
StartEmail

To:

From:


Subject:

	

EndBody

EndEmail	
