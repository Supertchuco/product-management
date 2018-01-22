This is an API to management products, I developed this API using ntellij IDE and these technologies:
	- Springboot;
	- Gradle;
	- Oracle Database;
	- H2 (memory database);
	- Swagger;
	- Lombok;
	- Actuator;
	- Spring rest.
	

	
	
This API allow these operations:



You can check the all rest services and the payloads through Swagger, to do it, you need to running the application and access this url 


This project have these kind of tests:
 -Integration Tests: EndToEndImageIT and EndToEndProductIT classes;
 - Unity Tests: ImageServiceTest and ProductServiceTest classes.

Note: You can run the tests through Intellij IDE or call direct in Gradle (execute the command "gradlew clean test" in cmd prompt)

Step to running the project:

 *Intellij IDE:
    -Import the project like Gradlew Project;
    -Mark the check box Enable Annotation Processing in Settings->Build->Execution->Deployment->Compiler->Annotation Processor
    -Run the major class ProductManagementApplication.

  *Without an IDE:
    -Execute the command "gradlew clean build" inside the project in a cmd prompt;
    -Before execute command "java -jar <Jar Name>.jar" in "product-management\build\libs"

  In both of cases you can check if the application is running accessing this url "" (actuator feature);