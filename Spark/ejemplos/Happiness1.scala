var e3_data = sc.textFile("input/3-*").map(_.split("\t"))
var e3_fdata = e3_data.filter(x => { x(2).toFloat < 2.0 && x(4) != "--" })
var e3_map = e3_fdata.map(x => ("Palabras muy tristes", x(0)))
var e3_result = e3_map.groupByKey().map{case (k,v) => (k, v.mkString(", "))}
var e3_rlines = e3_result.map{case (k,v) => k + ": " + v}
var e3_outfile = scala.tools.nsc.io.File("output/Happiness.txt")
e3_outfile.writeAll(e3_rlines.collect().mkString("\n"))