def cleanPath(s:String) = {
  var r = s
  while(r.exists(_=='/')) {
    r = r.dropWhile(_!='/').drop(1)
  }
  r
}

def lineSplit(s:String) = {
    s.map(x => if (x.isLetterOrDigit) x else ' ')
     .split(" ").filter(!_.isEmpty)
}

def indexExample(inputPath:String, outputPath:String) = {
  var r = sc.wholeTextFiles(inputPath).map(x => (cleanPath(x._1),x._2))
            .flatMap{case (f,c) => lineSplit(c).map(x => ((x,f), 1))}
            .reduceByKey(_+_)
            .map{case ((w,f),v) => (w,(f,v))}
            .groupByKey()
            .filter(x => x._2 != null && !x._2.filter(_._2 > 20).isEmpty)
            .map(x => (x._1,x._2.toList.sortBy(y => -y._2)))
            .sortBy({case (k,v) => v.head._2}, false)
  var output = scala.tools.nsc.io.File(outputPath)
  output.writeAll(r.collect().mkString("\n"))
}
indexExample("input/4-*", "output/Index.txt")