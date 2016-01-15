def e4_slscln(s:String) = s.dropWhile(_!='/').drop(1)
def e4_pthcln(s:String) = { var r = s; while(r.exists(_=='/')) r = e4_slscln(r); r }
var e4_inputs = sc.wholeTextFiles("input/4-*").map(x => (e4_pthcln(x._1),x._2))

def e4_clean(s:String) = s.map(x => if (x.isLetterOrDigit) x else ' ')
def e4_split(s:String) = e4_clean(s).split(" ").filter(!_.isEmpty)
var e4_data = e4_inputs.flatMap{case (f,c) => e4_split(c).map(x => ((x,f), 1))}

var e4_red0 = e4_data.reduceByKey(_+_).map{case ((w,f),v) => (w,(f,v))}.groupByKey()
var e4_red1 = e4_red0.filter(x => x._2 != null && !x._2.filter(_._2 > 20).isEmpty)
var e4_red2 = e4_red1.map(x => (x._1,x._2.toList.sortBy(y => -y._2)))

var e4_result = e4_red2.sortBy({case (k,v) => v.head._2}, false)
var e4_outfile = scala.tools.nsc.io.File("output/Index.txt")
e4_outfile.writeAll(e4_result.collect().mkString("\n"))