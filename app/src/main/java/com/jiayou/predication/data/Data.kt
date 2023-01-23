package com.jiayou.predication.data

import android.graphics.Point
import androidx.compose.ui.graphics.Color

/***********************************************
 * Author: ZhaoPengpeng
 * Date: 2023/1/17
 * Describe:
 ***********************************************/
data class CharPoint(val letter: Char = ' ', val point: Point = Point(-1, -1)) {
  fun isEmpty(): Boolean = letter == ' '
}

data class Pairs constructor(
  var first: CharPoint = CharPoint(),
  var second: CharPoint = CharPoint()
) {

  var color: Color? = null

  fun toList(): List<CharPoint> {
    val list = mutableListOf<CharPoint>()
    list += first
    list += second
    return list
  }
}

data class GridData constructor(
  val red: Pairs = Pairs(),
  val blue: Pairs = Pairs()
) {
  private fun redColor() = Player.RED.color

  private fun blueColor() = Player.BLUE.color

  fun toList(): List<Pairs> {
    val list = mutableListOf<Pairs>()
    red.color = redColor()
    blue.color = blueColor()
    list += red
    list += blue
    return list
  }
}

enum class Player(val color: Color, val text: String) {
  RED(Color(0xFFF44336), "红方"),
  BLUE(Color(0xFF2196F3), "蓝方")
}

enum class PlayState {
  IDLE, CHANCE1, CHANCE2, SHOW_RESULTS
}
