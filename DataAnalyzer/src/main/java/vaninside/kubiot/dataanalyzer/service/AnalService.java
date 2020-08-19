package vaninside.kubiot.dataanalyzer.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import scala.Tuple2;
import vaninside.kubiot.dataanalyzer.dao.AnalDao;

@Service
public class AnalService {
	
	@Autowired
	AnalDao dao;
	public void AnalService() {
		init();
	}
	
	public void init() {
		SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("JD Word Counter");
		sparkConf.set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
		sparkConf.set("spark.hadoop.fs.s3a.access.key", "ltDXrFCuHyha8pmwqTcX");
		sparkConf.set("spark.hadoop.fs.s3a.secret.key", "XFDdqJEASit4wHVFjCEEX6Yi2oD17ekIzCec2z41");
		sparkConf.set("spark.hadoop.fs.s3a.endpoint", "https://kr.object.ncloudstorage.com");
	}
	
	// 2초마다 들어오는 인풋을 검사
	@Scheduled(cron = "* * * * * *")
	public void inputCheck() {
		System.out.println("in");
		ArrayList<String> deviceList = dao.getDeviceList(); // mysql 에서 불러오기. float 클라이언트 개수.
		System.out.println(deviceList.get(0));
		for(int i=0; i<deviceList.size(); i++) {
			HashMap<String, Double> map = dao.getMinMax(deviceList.get(i));
			double min = map.get("min");
			double max = map.get("max"); // mysql 에서 불러온 최대 최소 값.
			double data = dao.getCurrentValue(deviceList.get(i)); // redis 에서 불러온 현재 값.
			System.out.println(min + " " + max + " " + data);
			if(data > max || data < min) {
				push(deviceList.get(i));
			}
		}
		
	}
	
	public void push(String deviceid) {
		System.out.println("push");
		// 푸시 서버로 요청 날리기.
		// 101.101.219.90:7878/push/pushall
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		
		String url = "http://101.101.219.90:7878/push/pushall";
		
		String msg = "{\"did\":\""+dao.getID(deviceid)+"\", \"message\":\""+deviceid+"\"}";
		JSONParser parser = new JSONParser();
		JSONObject obj = new JSONObject();
		try {
			obj = (JSONObject) parser.parse(msg);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpEntity<String> entity = new HttpEntity<>(obj.toString(),headers);
		String answer = restTemplate.postForObject(url, entity, String.class);
		//System.out.println(answer);
		
		
	}
	
	public KMeansModel kmeans(SparkConf sparkConf, String datapath) {
		
		JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
		// 입력 파일을 JavaRDD 로 변환합니다.
		//JavaRDD<String> inputFile = sparkContext.textFile("s3a://kubstudy-jimin/sample_kmeans_data.txt");
		JavaRDD<String> inputFile = sparkContext.textFile(datapath);
		
		
		
		JavaRDD<Vector> parsedData = inputFile.map(
			      new Function<String, Vector>() {
			        public Vector call(String s) {
			          String[] sarray = s.split(" ");
			          double[] values = new double[sarray.length];
			          for (int i = 0; i < sarray.length; i++)
			            values[i] = Double.parseDouble(sarray[i]);
			          return Vectors.dense(values);
			        }
			      }
			    );
			    parsedData.cache();
		
			    // Cluster the data into two classes using KMeans
			    int numClusters = 2;
			    int numIterations = 20;
			    
			    KMeansModel clusters = KMeans.train(parsedData.rdd(), numClusters, numIterations);

			   // Evaluate clustering by computing Within Set Sum of Squared Errors
			    double WSSSE = clusters.computeCost(parsedData.rdd());
			    System.out.println("Within Set Sum of Squared Errors = " + WSSSE);
			  
					 return clusters;   
	}
	
	public LogisticRegressionModel logisticRegression(SparkConf sparkConf, String datapath) {
		  SparkContext jsc = new SparkContext(sparkConf);
	        
			 // provide path to data transformed as [feature vectors]
		        //String path = "s3a://kubstudy-jimin/data_mllib_sample_libsvm_data.txt";

	        String path = datapath; 

		  JavaRDD data = MLUtils.loadLibSVMFile(jsc, path).toJavaRDD();
		 
		        // Split initial RDD into two... [80% training data, 20% testing data].
		        JavaRDD[] splits = data.randomSplit(new double[] {0.8, 0.2}, 11L);
		        JavaRDD training = splits[0].cache();
		        JavaRDD test = splits[1];
		 
		        // Run training algorithm to build the model.
		        LogisticRegressionModel model = new LogisticRegressionWithLBFGS()
		                .setNumClasses(10)
		                .run(training.rdd());
		 
		        // Compute raw scores on the test set.
		        JavaPairRDD<Object, Object> predictionAndLabels = test.mapToPair(p ->
		                new Tuple2<>(model.predict(((LabeledPoint) p).features()), ((LabeledPoint) p).label()));
		 
		        // get evaluation metrics
		        MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabels.rdd());
		        double accuracy = metrics.accuracy();
		        System.out.println("Accuracy = " + accuracy);
		 
		        
		        // After training, save model to local for prediction in future  
		        //model.save(jsc, "LogisticRegressionClassifier");
		 
		        // stop the spark context
		      jsc.stop();
		      return model;
	}
}
