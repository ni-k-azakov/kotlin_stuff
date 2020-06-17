import java.io.File
 
class Vector (size: Int, content: Array<Float>){
    private var _body: Array<Float> = Array(size) {0f}
    init {
            _body = content
    }
    fun quickSort(first: Int, last: Int) {
        if (first >= last) return
        if (first + 1 == last) {
            _body.sort(first, last)
            return
        }
        quickSort(first, (last + first) / 2)
        quickSort((last + first) / 2, last)
        _body.sort(first, last)
    }
 
    operator fun get(i: Int): Any {
        return _body[i]
    }
}
 
fun main() {
    val input = File("sortland.in")
    val lines = input.readLines()
    var args = lines[0].split(" ")
    val amount = args[0].toInt()
    var values = Array(amount) {0f}
    args = lines[1].split(" ")
    for (i in 0 until amount) {
        values[i] = args[i].toFloat()
    }
    var vector = Vector(amount, values)
    vector.quickSort(0, amount)
    var lowest = 0
    var middle = 0
    var highest = 0
    for (i in 0 until amount) {
        when (args[i].toFloat()) {
            vector[0] -> lowest = i + 1
            vector[amount / 2] -> middle = i + 1
            vector[amount - 1] -> highest = i + 1
        }
    }
    File("sortland.out").printWriter().use { out -> out.print("$lowest $middle $highest") }
}
