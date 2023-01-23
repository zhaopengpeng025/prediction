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

  private var round = 1

  init {
    _uiState.update {
      it.copy(round = round, playState = PlayState.CHANCE1)
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

  fun pasteChosenLetter(point: Point) {
    _uiState.update { state ->
      val updatePairs =
        (if (state.player == Player.RED) state.redPairs else state.bluePairs)?.copy() ?: Pairs()
      if (state.playState == PlayState.CHANCE1) {
        updatePairs.first = CharPoint(state.chosenLetter, point)
      } else if (state.playState == PlayState.CHANCE2) {
        updatePairs.second = CharPoint(state.chosenLetter, point)
      }
      if (state.player == Player.RED) {
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

  fun buttonOk() {
    _uiState.value.let { state ->
      if (state.chosenLetter == ' ') return
      val pairs = if (state.player == Player.RED) state.redPairs else state.bluePairs
      pairs ?: return
      if ((pairs.first.isEmpty() && state.playState == PlayState.CHANCE1) ||
        (pairs.second.isEmpty() && state.playState == PlayState.CHANCE2)
      ) return
    }
    _uiState.update { state ->
      val newState = nextPlayState(state)
      val newPlayer = nextPlayer(state)
      if (newPlayer != null) {
        state.saveGridDataMap()
        state.copy(
          playState = newState,
          player = newPlayer,
          chosenLetter = ' ',
          redPairs = null,
          bluePairs = null
        )
      } else {
        state.copy(playState = newState, chosenLetter = ' ')
      }
    }
  }

  private fun nextPlayState(state: GridUiState) = when (state.playState) {
    PlayState.CHANCE1 -> PlayState.CHANCE2
    PlayState.CHANCE2 -> {
      if (state.player == Player.BLUE) {
        state.saveGridDataMap()
        PlayState.SHOW_RESULTS
      } else {
        PlayState.CHANCE1
      }
    }
    PlayState.IDLE, PlayState.SHOW_RESULTS -> {
      PlayState.SHOW_RESULTS
    }
  }

  private fun nextPlayer(state: GridUiState): Player? {
    return if (state.player == Player.RED && state.playState == PlayState.CHANCE2) {
      Player.BLUE
    } else null
  }

  fun buttonNextRound() {
    val state = defaultState()
    _uiState.value = state
  }

  fun buttonClearLetters() {
    _uiState.update { state ->
      if(state.player == Player.RED){
        state.copy(redPairs = null, playState = PlayState.CHANCE1)
      }else{
        state.copy(bluePairs = null,playState = PlayState.CHANCE1)
      }
    }
  }

  private fun defaultState() = GridUiState(
    generateRandomChar(),
    round = ++round,
    playState = PlayState.CHANCE1
  )

  companion object {
    const val COLUMN = 5
    const val ROW = 5
  }
}
