import java.*
import java.io.*
import java.util.*
import java.text.*
import java.math.*
import java.util.regex.*

object Solution {

    @JvmStatic
    fun main(args: Array<String>) {
        val i = 4
        val d = 4.0
        val s = "HackerRank "

        val scan = Scanner(System.`in`)


        /* Declare second integer, double, and String variables. */
        val i2 = scan.nextInt()
        val d2 = scan.nextDouble()
        val s2 = scan.nextLine()

        /* Read and save an integer, double, and String to your variables.*/
        // Note: If you have trouble reading the entire String, please go back and review the Tutorial `closely.

        /* Print the sum of both integer variables on a new line. */
        println(i + i2)

        /* Print the sum of the double variables on a new line. */
        println(d + d2)
        /* Concatenate and print the String variables on a new line;
        	the 's' variable above should be printed first. */
        println(s + s2)

        scan.close()
    }
}