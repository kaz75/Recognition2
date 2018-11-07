package recognition2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

public class Recognition2_lib {

	IamOptions options;
	 InputStream imagesStream = null;
	 VisualRecognition service;
	 public Recognition2_lib() {
		options  = new IamOptions.Builder()
				  .apiKey("RmQsujfQEcmY92iIiT4-rwNIs7JWs0O1rOxr0896_RNS")
				  .build();

				service = new VisualRecognition("2018-03-19", options);

	 }
	 public void imgfile (File file) {
			try {
				imagesStream = new FileInputStream("img/enkai.jpg");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }
	 public ClassifiedImages keisan () {
		 ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
				  .imagesFile(imagesStream)
				  .imagesFilename("enkai.jpg")
				  .threshold((float) 0.6)
				  .owners(Arrays.asList("IBM"))
				  .build();
				ClassifiedImages result = service.classify(classifyOptions).execute();
				System.out.println(result);
				return result;
	 }
	 public void getJson(ClassifiedImages result) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = null;
			String className[] = {"-1","-1","-1"};
			double score[] = {-1,-1,-1};
			MySQL mysql = new MySQL();
			try {
				node = mapper.readTree(String.valueOf(result));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JsonNode temp = node.get("images").get(0).get("classifiers").get(0).get("classes");
			System.out.println(temp.toString());
			for(int i=0;i< temp.size() && i < 3; i++) {
				className[i] = temp.get(i).get("class").toString();
				score[i] = temp.get(i).get("score").doubleValue();	
			}
			
			mysql.updateImage(className,score);
			
}
}
