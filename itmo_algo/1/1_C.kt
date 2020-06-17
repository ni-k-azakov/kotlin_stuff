import java.io.File
 
var h: Int = 0
var w: Int = 0
var table: Array<Array<Int>> = Array(0) { Array(0) {0} }
var checker: Array<Array<Boolean>> = Array(0) { Array(0) {false} }
 
fun trail(temp_pos_x: Int, temp_pos_y: Int): Int {
    if (temp_pos_x < 0 || temp_pos_y >= h) return 0
    if (temp_pos_x == 0 && temp_pos_y == h - 1) return table[h - 1][0]
    if (checker[temp_pos_y][temp_pos_x]) return table[temp_pos_y][temp_pos_x]
    table[temp_pos_y][temp_pos_x] += maxOf(trail(temp_pos_x - 1, temp_pos_y),
            trail(temp_pos_x, temp_pos_y + 1))
    checker[temp_pos_y][temp_pos_x] = true
    return table[temp_pos_y][temp_pos_x]
}
 
fun main() {
    val input = File("turtle.in")
    val lines = input.readLines()
    val args = lines[0].split(" ")
    h = args[0].toInt()
    w = args[1].toInt()
    table = Array(h) { Array(w) {0} }
    checker = Array(h) { Array(w) {false} }
    for (i in 1..h) {
        val line = lines[i].split(" ")
        for (j in 0 until w) {
            table[i - 1][j] = line[j].toInt()
        }
    }
    trail(w - 1, 0)
    File("turtle.out").printWriter().use { out -> out.println(table[0][w - 1]) }
} 
