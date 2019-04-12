How to run

Pre-steps:
	1. Sudo apt install maven

Server:
	1. CD into the server directory
	2. mvn package
	3. cd into target dir
	4. java -jar {jarname}.jar

Client:
	1. CD into the client directory
	2. mvn package
	3. cd into target dir
	4. java -jar {jarname}.jar 
	
	optionally, provide ip and port in the form of {127.0.0.1:8080} (it will default to localhost)

Note: This is written to run on a Linux distribution.
Note: If running on Windows, one must copy(cp) the rooms.json file from the src directory of the server to the target directory of the server before running.
