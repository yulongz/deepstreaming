package Test

object Test {
  def main(args: Array[String]): Unit = {
//    val a = Array(1, 2, 3, 4, 5)
//    a.mkString(",").foreach(print)
    val a = 80000000
    val b = a.toFloat
    val c = (1000.0 / b).toInt
    println(b)
    println(c)
  }
  
}