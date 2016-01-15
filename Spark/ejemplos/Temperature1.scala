var e2_data = sc.textFile("input/2-*").map(_.split(","))
var e2_map = e2_data.map(x => (x(1) + " " + x(2), x(8).toFloat - x(12).toFloat))
var e2_result = e2_map.groupByKey().map{case (k,v) => (k, (v.max, v.min))}
var e2_sresult = e2_result.sortBy{case (k,v) => k}
var e2_outfile = scala.tools.nsc.io.File("output/Temperature.txt")
e2_outfile.writeAll(e2_sresult.collect().mkString("\n"))