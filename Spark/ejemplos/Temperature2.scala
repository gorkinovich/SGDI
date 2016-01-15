def temperatureExample(inputPath:String, outputPath:String) = {
  var r = sc.textFile(inputPath).map(_.split(","))
            .map(x => (x(1) + " " + x(2), x(8).toFloat - x(12).toFloat))
            .groupByKey().map{case (k,v) => (k, (v.max, v.min))}
            .sortBy{case (k,v) => k}
  var output = scala.tools.nsc.io.File(outputPath)
  output.writeAll(r.collect().mkString("\n"))
}
temperatureExample("input/2-*", "output/Temperature.txt")