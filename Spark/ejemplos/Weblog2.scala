def weblogExample(inputPath:String, outputPath:String) = {
  var r = sc.textFile(inputPath).map(_.split(" "))
            .filter(x => { x(x.length - 2) == "302" })
            .map(x => {(x(1).drop(1).takeWhile(_!=':'), 1)})
            .reduceByKey(_+_).sortBy{case (k,v) => k}
  var output = scala.tools.nsc.io.File(outputPath)
  output.writeAll(r.collect().mkString("\n"))
}
weblogExample("input/1-*", "output/Weblog.txt")