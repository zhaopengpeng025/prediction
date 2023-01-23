package com.jiayou.predication.ui

import android.graphics.Point
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
  val redPairs: Pairs? = null,
  val bluePairs: Pairs? = null,
  val allGridDataMap: MutableMap<Point, GridData> = mutableMapOf()
) {

  fun toGridDataMap(): Map<Point, GridData> {
    val gridDataMap = mutableMapOf<Point, GridData>()
    convertPairs2GridDataMap(redPairs, gridDataMap)
    convertPairs2GridDataMap(bluePairs, gridDataMap)
    return gridDataMap
  }

  fun saveGridDataMap() {
    val map = toGridDataMap()
    if (allGridDataMap.isEmpty()) {
      allGridDataMap.putAll(map)
      return
    }
    for ((key, value) in map) {
      for (allKey in allGridDataMap.keys) {
        if (key == allKey) {
          allGridDataMap.compute(key) { _, v ->
            v?.let {
              if (player == Player.RED) v.copy(red = value.red) else v.copy(blue = value.blue)
            } ?: if (player == Player.RED) GridData(value.red) else GridData(blue = value.blue)
          }
        } else {
          allGridDataMap[key] = value
        }
      }
    }
  }

  private fun convertPairs2GridDataMap(pairs: Pairs?, gridDataMap: MutableMap<Point, GridData>) {
    pairs?.let {
      if (it.first.point == it.second.point && it.first.point.equals(-1, -1).not()) {
        gridDataMap[it.first.point] =
          if (player == Player.RED) GridData(pairs) else GridData(blue = pairs)
      } else {
        if (player == Player.RED) {
          if (it.first.isEmpty().not()) {
            gridDataMap[it.first.point] = GridData(pairs.copy(second = emptyCharPoint()))
          }
          if (it.second.isEmpty().not()) {
            gridDataMap[it.second.point] = GridData(pairs.copy(first = emptyCharPoint()))
          }
        } else {
          if (it.first.isEmpty().not()) {
            gridDataMap[it.first.point] = GridData(blue = pairs.copy(second = emptyCharPoint()))
          }
          if (it.second.isEmpty().not()) {
            gridDataMap[it.second.point] = GridData(blue = pairs.copy(first = emptyCharPoint()))
          }
        }
      }
    }
  }
}

private fun emptyCharPoint() = CharPoint()
