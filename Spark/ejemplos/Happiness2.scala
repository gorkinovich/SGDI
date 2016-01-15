def happinessExample(inputPath:String, outputPath:String) = {
  var r = sc.textFile(inputPath).map(_.split("\t"))
            .filter(x => { x(2).toFloat < 2.0 && x(4) != "--" })
            .map(x => ("Palabras muy tristes", x(0)))
            .groupByKey().map{case (k,v) => (k, v.mkString(", "))}
            .map{case (k,v) => k + ": " + v}
  var output = scala.tools.nsc.io.File(outputPath)
  output.writeAll(r.collect().mkString("\n"))
}
happinessExample("input/3-*", "output/Happiness.txt")