package com.jiayou.predication.ui

<<<<<<< Updated upstream
=======
import android.graphics.Point
import com.jiayou.predication.data.GridData
import com.jiayou.predication.data.Pairs
import com.jiayou.predication.data.PlayState
import com.jiayou.predication.data.Player

>>>>>>> Stashed changes
/***********************************************
 * Author: ZhaoPengpeng
 * Date: 2023/1/18
 * Describe:
<<<<<<< Updated upstream
 ***********************************************/data class GridUiState()
=======
 ***********************************************/
data class GridUiState(
  val randomLetters: List<Char>,
  val round: Int = -1,
  val playerType: Player = Player.RED,
  val playState: PlayState = PlayState.IDLE,
  val chosenLetter: Char = ' ',
  val redPairs: Pairs? = null,
  val bluePairs: Pairs? = null
) {

  fun toGridDataList(): Map<Point, GridData> {
    val gridDataMap = mutableMapOf<Point, GridData>()
    convertPairs2GridDataMap(redPairs, gridDataMap)
    convertPairs2GridDataMap(bluePairs, gridDataMap)
    return gridDataMap
  }

  private fun convertPairs2GridDataMap(pairs: Pairs?, gridDataMap: MutableMap<Point, GridData>) {
    pairs?.toList()?.let {
      for (p in it) {
        gridDataMap.compute(p.point) { _, v ->
          v ?: GridData(pairs)
        }
      }
    }
  }
}
>>>>>>> Stashed changes
