This is an API to management products, I developed this API using Intellij IDE and these technologies:
	- Springboot;
	- Gradle;
	- Oracle Database;
	- H2 (memory database);
	- Swagger;
	- Lombok;
	- Actuator;
	- Spring rest.

This API allow these operations:
    * Product:
        - Insert a product "http://localhost:8090/productManagement/api/product/save" (you need to send a payload);
        - Delete a product "http://localhost:8090/productManagement/api/product/deleteByName/{productName}";
        - List all products "http://localhost:8090/productManagement/api/product/findAll";
        - List a specific product by name "http://localhost:8090/productManagement/api/product/findByName/{productName}";
        - Modify a product "http://localhost:8090/productManagement/api/product/editByName/{productName}" (you need to send a payload).
    * Image:
        - Insert a image "http://localhost:8090/productManagement/api/image/save" (you need to send a payload);
        - Delete a image "http://localhost:8090/productManagement/api/image/deleteByType/{imageType}";
        - List all images "http://localhost:8090/productManagement/api/image/findAll";
        - List a specific image by type "http://localhost:8090/productManagement/api/image/findByType/{imageType}";
        - Modify a image "http://localhost:8090/productManagement/api/image/editByType/{imageType}" (you need to send a payload).

You can check the all rest services and the payloads through Swagger, to do it you need to running the application and access this url
"http://localhost:8090/productManagement/api/swagger-ui.html"

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

  In both of cases you can check if the application is running accessing this url "http://localhost:8090/productManagement/api/health" (actuator feature);

  Important Note: This project is configured to create the database structure every time that the application is started with a specified
  data (you can check it in DatabaseLoader class), if you   want to disable it, you need to comment (#) or delete this line "spring.jpa.hibernate.ddl-auto=create" in application.properties.
   This project use a memory database (Spring H2) but also it is configured to use Oracle database, to use it
  you just need to configure the connection in application.properties (there is my configuration commented
  in application.properties, you can follow it like an example).