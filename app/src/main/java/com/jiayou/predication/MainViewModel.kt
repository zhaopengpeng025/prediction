package com.jiayou.predication

import android.graphics.Point
import androidx.lifecycle.ViewModel
import com.jiayou.predication.data.CharPoint
import com.jiayou.predication.data.Pairs
import com.jiayou.predication.data.PlayState
import com.jiayou.predication.data.Player
import com.jiayou.predication.ui.GridUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/***********************************************
 * Author: ZhaoPengpeng
 * Date: 2023/1/18
 * Describe:
 ***********************************************/
class MainViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(GridUiState(generateRandomChar()))
  val uiState: StateFlow<GridUiState> = _uiState.asStateFlow()

  init {
    _uiState.update {
      it.copy(round = 1, playState = PlayState.CHANCE1)
    }
  }

  /**
   * 生成随机英文大写字母 [A-Z]
   */
  private fun generateRandomChar(size: Int = 6): List<Char> {
    val list = mutableListOf<Char>()
    while (list.size < size) {
      val c = ('A'..'Z').random()
      if (list.contains(c).not()) list.add(c)
    }
    return list
  }

  fun pasteChosenLetter(letter: Char, x: Int, y: Int) {
    _uiState.update { state ->
      val updatePairs = if (state.playerType == Player.RED) state.redPairs else state.redPairs
      updatePairs ?: Pairs()
      if (updatePairs!!.first.isEmpty()) {
        updatePairs.first = CharPoint(letter, Point(x, y))
      } else {
        updatePairs.second = CharPoint(letter, Point(x, y))
      }
      if (state.playerType == Player.RED) {
        state.copy(redPairs = updatePairs)
      } else {
        state.copy(bluePairs = updatePairs)
      }
    }
  }

  fun selectLetter(letter: Char) {
    _uiState.update { state ->
      state.copy(chosenLetter = letter)
    }
  }

  companion object {
    const val COLUMN = 5
    const val ROW = 5
  }
}
