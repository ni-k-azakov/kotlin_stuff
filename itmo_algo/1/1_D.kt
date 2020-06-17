import java.io.File
 
fun main() {
    val input = File("smallsort.in")
    val lines = input.readLines()
    var args = lines[0].split(" ")
    val amount = args[0].toInt()
    var values = Array(amount) {0}
    args = lines[1].split(" ")
    for (i in 0 until amount) {
        values[i] = args[i].toInt()
    }
    values.sort()
    File("smallsort.out").printWriter().use { out -> values.forEach { out.print("$it ") } }
}
