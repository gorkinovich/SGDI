var e1_data = sc.textFile("input/1-*").map(_.split(" "))
var e1_fdata = e1_data.filter(x => { x(x.length - 2) == "302" })
var e1_map = e1_fdata.map(x => {(x(1).drop(1).takeWhile(_!=':'), 1)})
var e1_result = e1_map.reduceByKey(_+_)
var e1_sresult = e1_result.sortBy{case (k,v) => k}
var e1_outfile = scala.tools.nsc.io.File("output/Weblog.txt")
e1_outfile.writeAll(e1_sresult.collect().mkString("\n"))