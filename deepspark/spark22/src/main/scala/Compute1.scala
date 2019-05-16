import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LinearRegressionModel
import org.apache.spark.mllib.regression.LinearRegressionWithSGD

object Compute1 {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("mlib").setMaster("local")
    val sc = new SparkContext(conf)
    val rdd = sc.textFile("data/prices_LinearRegression.txt")
    val rdd2=rdd.map{line=>
      val parts=line.split("\\s+")
      LabeledPoint(parts(0).toDouble,Vectors.dense(parts(1).toDouble,parts(2).toDouble))
    }
    //创建模型
    val model=LinearRegressionWithSGD.train(rdd2,1000,0.0001)
    //得到权重
    model.weights
    //样本数据进行对比
    val prediction=model.predict(rdd2.map(_.features))
    val predictionAndLabel=prediction.zip(rdd2.map(_.label))
    val print_predict=predictionAndLabel.take(50)
    for(i<-0 to print_predict.length-1){
      println(print_predict(i)._1+"\t"+print_predict(i)._2)
    }
    //房价预测，100平和200平
    model.predict(Vectors.dense(Array(1,100).map(_.toDouble)))
    model.predict(Vectors.dense(Array(1,200).map(_.toDouble)))
    //误差计算
    val loss=predictionAndLabel.map{
      case(p,l)=>
        val err=p-l
        err*err
    }.reduce(_+_)
    val rmse=math.sqrt(loss/rdd2.count)
    println(s"Test rmse=$rmse")
    //模型保存
    val ModelPath="data/LinearRegressionModel"
    model.save(sc,ModelPath)
    val sameModel=LinearRegressionModel.load(sc,ModelPath)
    sameModel.weights
  }
}
