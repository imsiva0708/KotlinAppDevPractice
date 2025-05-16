fun main() {
    println("How many numbers will you enter: ")
    val noOfNumbers = readln().toIntOrNull() ?: 0
    var i=0
    var sum:Int = 0
    while(i<noOfNumbers){
        println("Enter number ${i+1}")
        var newNum = readln().toIntOrNull() ?: continue
        sum += newNum
        i++
    }
    println("Sum = $sum")
}