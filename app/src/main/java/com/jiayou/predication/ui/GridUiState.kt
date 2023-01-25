package com.jiayou.predication.ui

import android.graphics.Point
import android.util.Log
import com.jiayou.predication.data.CharPoint
import com.jiayou.predication.data.GridData
import com.jiayou.predication.data.Pairs
import com.jiayou.predication.data.PlayState
import com.jiayou.predication.data.Player

/***********************************************
 * Author: ZhaoPengpeng
 * Date: 2023/1/18
 * Describe:
 ***********************************************/
data class GridUiState(
  val randomLetters: List<Char>,
  val round: Int = -1,
  val player: Player = Player.RED,
  val playState: PlayState = PlayState.IDLE,
  val chosenLetter: Char = ' ',
  val gridDataMap: MutableMap<Point, GridData> = mutableMapOf(),
  val allGridDataMap: MutableMap<Point, GridData> = mutableMapOf()
) {

  val isRed: Boolean
    get() = player == Player.RED

  fun saveGridDataMap() {
    Log.d("zpp", "saveGrid")
    val map = gridDataMap
    if (allGridDataMap.isEmpty()) {
      allGridDataMap.putAll(map)
      return
    }
    for ((key, value) in map) {
      var hasOldKey = false
      for (allKey in allGridDataMap.keys) {
        // allKey 为 RED key; key 为 BLUE key
        if (key == allKey) {
          hasOldKey = true
          allGridDataMap.compute(key) { _, v ->
            v?.copy(blue = value.blue)
          }
          break
        }
      }
      if (hasOldKey.not()) allGridDataMap[key] = value
    }
  }

  override fun equals(other: Any?): Boolean {
    val isEquality = super.equals(other)
    other as GridUiState
    return isEquality && (other.gridDataMap == gridDataMap)
  }

  override fun hashCode(): Int {
    var result = randomLetters.hashCode()
    result = 31 * result + round
    result = 31 * result + player.hashCode()
    result = 31 * result + playState.hashCode()
    result = 31 * result + chosenLetter.hashCode()
    result = 31 * result + gridDataMap.hashCode()
    result = 31 * result + allGridDataMap.hashCode()
    return result
  }
}

private fun emptyCharPoint() = CharPoint()
