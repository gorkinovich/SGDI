def wordCount(inputPath:String, outputPath:String) = {
  var r = sc.textFile(inputPath).flatMap(_.split(" "))
            .map((_, 1)).reduceByKey(_+_)
            .sortBy({case (k,v) => v}, false)
  var output = scala.tools.nsc.io.File(outputPath)
  output.writeAll(r.collect().mkString("\n"))
}
wordCount("input/4-Hamlet.txt", "output/WordCount.txt")