import java.io.File

fun main() {
    val input = File("aplusbb.in")
    val line = input.readText()
    val word = line.replace("\n","").split(" ")
    var a = word[0].toInt()
    val b = word[1].toInt() * word[1].toInt()
    a += b
    File("aplusbb.out").printWriter().use { out -> out.println(a) }
}