import java.io.File

fun main() {
    val input = File("aplusb.in")
    var temp = 0
    for (line in input.readLines()) {
        for (word in line.split(" ")) {
            val tempInt = word.toInt()
            temp += tempInt
        }
    }
    File("aplusb.out").printWriter().use { out -> out.println(temp) }
}