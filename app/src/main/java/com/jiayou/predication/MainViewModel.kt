package com.jiayou.predication

import android.graphics.Point
import androidx.lifecycle.ViewModel
import com.jiayou.predication.data.CharPoint
import com.jiayou.predication.data.GridData
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

  private val _uiState = MutableStateFlow(GridUiState(letterList()))
  val uiState: StateFlow<GridUiState> = _uiState.asStateFlow()

  private var round = 1

  init {
    _uiState.update {
      it.copy(round = round, player = Player.RED, playState = PlayState.PLAYING)
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

  private fun letterList() = listOf('A', 'B', 'C', 'X', 'Y', 'Z')

  fun pasteChosenLetter(point: Point) {
    if(_uiState.value.chosenLetter == ' ') return
    val map = mutableMapOf<Point, GridData>()
    map.putAll(_uiState.value.gridDataMap)
    map.compute(point) { k, v ->
      v?.let {
        val pairs = if (_uiState.value.isRed) v.red else v.blue
        pairs.second = CharPoint(_uiState.value.chosenLetter, point)
        it.copy()
      } ?: if (_uiState.value.isRed) {
        GridData(Pairs(CharPoint(_uiState.value.chosenLetter, point)))
      } else {
        GridData(blue = Pairs(CharPoint(_uiState.value.chosenLetter, point)))
      }
    }
    _uiState.update { state ->
      state.copy(gridDataMap = map)
    }
  }

  fun selectLetter(letter: Char) {
    _uiState.update { state ->
      state.copy(chosenLetter = letter)
    }
  }

  fun buttonOk() {
    _uiState.value.let { state ->
      if (state.chosenLetter == ' ' || state.gridDataMap.isEmpty()) return
    }
    _uiState.update { state ->
      val nextPlayer = nextPlayer(state)
      state.saveGridDataMap()
      if (nextPlayer != null) {
        state.copy(
          player = nextPlayer,
          chosenLetter = ' ',
          gridDataMap = mutableMapOf()
        )
      } else {
        state.copy(playState = PlayState.SHOW_RESULTS, chosenLetter = ' ')
      }
    }
  }

  private fun nextPlayer(state: GridUiState): Player? {
    return if (state.isRed) {
      Player.BLUE
    } else null
  }

  fun buttonNextRound() {
    val state = defaultState()
    _uiState.value = state
  }

  fun buttonClearLetters() {
    _uiState.update { state ->
      state.copy(gridDataMap = mutableMapOf())
    }
  }

  private fun defaultState() = GridUiState(
    letterList(),
    round = ++round
  )

  companion object {
    const val COLUMN = 5
    const val ROW = 5
  }
}
