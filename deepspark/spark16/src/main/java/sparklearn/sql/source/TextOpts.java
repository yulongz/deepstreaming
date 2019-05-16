package sparklearn.sql.source;

/* SimpleApp.java */
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.Function;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

//spark-submit --class "com.yulongz.sparklearn.java.SimpleApp" --master local[1] target/sparklearn-0.0.1-SNAPSHOT.jar

public class TextOpts {
	public static void main(String[] args) {
		textToDF();
		//schemaModel();
	}
	
	public static void textToDF() {
		SparkConf conf = new SparkConf().setAppName(TextOpts.class.getSimpleName()).setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);

		JavaRDD<String> lines = sc.textFile("data/userinfo.txt");
		/**
		 * ] 在RDD的基础上创建类型为Row的RDD，
		 */
		JavaRDD<Row> personRDD = lines.map(new Function<String, Row>() {

			private static final long serialVersionUID = 1L;

			public Row call(String line) throws Exception {
				String[] split = line.split("\t");
				return RowFactory.create(Integer.valueOf(split[0]), split[1], Integer.valueOf(split[2]));
			}
		});
		/**
		 * 1、 动态的构建DataFrame的元数据，一般而言，有多少列以及类的具体类型可能来源于JSON文件或者数据库
		 */
//		List<StructField> structFields = new ArrayList<StructField>();
//		
//		structFields.add(DataTypes.createStructField("id", DataTypes.IntegerType, true));
//		structFields.add(DataTypes.createStructField("name", DataTypes.StringType, true));
//		structFields.add(DataTypes.createStructField("age", DataTypes.IntegerType, true));
		/**
		 * 2、构建StructType用于DataFrame 元数据的描述
		 * 
		 */
//		StructType structType = DataTypes.createStructType(structFields);
		
		
		List<StructField> structFields = new ArrayList<StructField>();
		for(TwoTuple<String, DataType> column:schemaModel()){
			structFields.add(DataTypes.createStructField(column.name, column.type, true));
		}
		
		StructType structType = DataTypes.createStructType(structFields);
		
		/**
		 * 3、基于MeataData以及RDD<Row>来构造DataFrame
		 */
		DataFrame personsDF = sqlContext.createDataFrame(personRDD, structType);

		personsDF.show();

		/**
		 * 4、注册成为临时表以供后续的SQL查询操作
		 */
		personsDF.registerTempTable("persons");
		/**
		 * 5、进行数据的多维度分析
		 */
		DataFrame dataResults = sqlContext.sql("select * from persons where  age > 24");
		/**
		 * 6对结果进行处理，包括由DataFrame转换为RDD<Row> 以及结果的持久化
		 */
		List<Row> collect = dataResults.javaRDD().collect();
		for (Row lists : collect) {
			System.out.println(lists);
		}

	}
	
	public static List<TwoTuple<java.lang.String, org.apache.spark.sql.types.DataType>> schemaModel() {
        List<TwoTuple<String, DataType>> list = new ArrayList<TwoTuple<String, DataType>>();
        
        list.add(new TwoTuple<String, DataType>("id", DataTypes.IntegerType));
        list.add(new TwoTuple<String, DataType>("name", DataTypes.StringType));
        list.add(new TwoTuple<String, DataType>("age", DataTypes.IntegerType));
       
       return list;
    }
	
	@SuppressWarnings("hiding")
	public static class TwoTuple<String, DataType> {
	    public final String name;
	    public final DataType type;
	     
	    public TwoTuple(String a, DataType b) {
	        this.name = a;
	        this.type = b;
	    }
	}
	
}