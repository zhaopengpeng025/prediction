package com.jiayou.predication.ui.screen

import android.graphics.Point
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jiayou.predication.MainViewModel
import com.jiayou.predication.data.CharPoint
import com.jiayou.predication.data.GridData
import com.jiayou.predication.data.Pairs
import com.jiayou.predication.data.Player
import com.jiayou.predication.ui.GridUiState

/***********************************************
 * Author: Zhao Pengpeng
 * Date: 2023/1/13
 * Describe:
 ***********************************************/
@Composable
fun GameMainScreen() {
  Column(
    Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    val viewModel: MainViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    GameGrid(uiState)

    RandomCharBar(uiState.randomLetters, uiState.chosenLetter, uiState.playerType)

    OutlinedButton(
      onClick = { /*TODO*/ },
      Modifier
        .width(250.dp)
        .padding(top = 30.dp)
    ) {
      Text(text = "OK")
    }

    OutlinedButton(
      onClick = { /*TODO*/ },
      Modifier.width(250.dp)
    ) {
      Text(text = "NEXT")
    }
  }
}

@Composable
fun GameGrid(uiState: GridUiState) {
  Column(Modifier.padding(top = 50.dp)) {
    for (y in 0 until MainViewModel.ROW) {
      Row(Modifier.wrapContentSize()) {
        for (x in 0 until MainViewModel.COLUMN) {
          GridItem(findGridData(uiState.toGridDataList(), x, y))
        }
      }
    }
  }
}

private fun findGridData(gridMap: Map<Point, GridData>, x: Int, y: Int): GridData? {
  for (point in gridMap.keys) {
    if (point.equals(x, y)) {
      return gridMap[point]
    }
  }
  return null
}

@Composable
fun GridItem(gridData: GridData?, size: Dp = 50.dp, textSize: TextUnit = 20.sp) {
  Row(
    Modifier
      .size(size)
      .border(0.5.dp, Color.LightGray)
      .clickable { }
  ) {
    gridData?.let {
      val gridList = gridData.toList()
      for (player in gridList) {
        Column(
          Modifier
            .width(size / 2)
            .fillMaxHeight()
        ) {
          val letters = player.toList()
          for (letter in letters) {
            if (letter.c.toString().trim().isBlank()) continue
            Text(
              text = letter.c.toString(),
              Modifier
                .fillMaxWidth()
                .height(size / 2),
              color = player.color!!,
              fontSize = textSize,
              textAlign = TextAlign.Center,
              overflow = TextOverflow.Visible,
              softWrap = false
            )
          }
        }
      }
    }
  }
}

@Preview
@Composable
fun PreviewGridItem() {
  GridItem(
    GridData(
      Pairs(CharPoint('A'), CharPoint('B')),
      Pairs(CharPoint('C'), CharPoint('D'))
    )
  )
}

@Composable
fun RandomCharBar(letters: List<Char>, chosenLetter: Char, player: Player) {
  val viewModel = viewModel<MainViewModel>()
  Row(Modifier.padding(top = 30.dp)) {
    for (l in letters) {
      Box(
        Modifier
          .size(41.6.dp)
          .border(
            BorderStroke(
              0.5.dp,
              if (chosenLetter == l) player.color else Color.LightGray
            )
          )
          .clickable { viewModel.selectLetter(l) },
        contentAlignment = Alignment.Center
      ) {
        Text(text = l.toString())
      }
    }
  }
}

@Preview
@Composable
fun PreviewRandomBar() {
  val list = mutableListOf<Char>()
  while (list.size != 6) {
    val c = ('A'..'Z').random()
    if (list.contains(c).not()) list.add(c)
  }
  RandomCharBar(letters = list, 'Z', Player.RED)
}

fun main() {
  val c: Char = '1'

  println("dsf$c")
}
