package com.jiayou.predication.ui.screen

import android.graphics.Point
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
import com.jiayou.predication.data.PlayState
import com.jiayou.predication.data.Player
import com.jiayou.predication.ui.GridUiState

/***********************************************
 * Author: Zhao Pengpeng
 * Date: 2023/1/13
 * Describe:
 ***********************************************/
@Composable
fun GameMainScreen() {
  Box(contentAlignment = Alignment.Center) {
    val viewModel: MainViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    Column(
      Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      TopTips(uiState.round, uiState.player, uiState.playState)

      GameGrid(uiState)

      RandomCharBar(uiState.randomLetters, uiState.chosenLetter, uiState.player)

      if (uiState.playState == PlayState.SHOW_RESULTS) {
        OutlinedButton(
          onClick = { viewModel.buttonNextRound() },
          Modifier.width(250.dp).padding(top = 30.dp)
        ) {
          Text(text = "NEXT")
        }
      } else {
        OutlinedButton(
          onClick = { viewModel.buttonOk() },
          Modifier
            .width(250.dp)
            .padding(top = 30.dp)
        ) {
          Text(text = "OK")
        }
      }
    }
    val isLoading = uiState.isLoading
    if (isLoading.isLoading) {
      TimeProgress(isLoading.delay.toString())
    }
  }
}

@Preview
@Composable
fun TimeProgress(delay: String = "1") {
  Surface(
    Modifier.padding(10.dp),
    shadowElevation = 5.dp
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
        Box(
          Modifier.size(30.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(text = delay, fontSize = 20.sp, textAlign = TextAlign.Center)
        }
      }
      Text(
        text = "加载中...",
        Modifier.padding(start = 10.dp, end = 20.dp),
        fontSize = 15.sp,
        textAlign = TextAlign.Center
      )
    }
  }
}

@Composable
fun GameGrid(uiState: GridUiState) {
  val gridDataMap = if (uiState.playState == PlayState.SHOW_RESULTS) {
    uiState.allGridDataMap
  } else {
    uiState.gridDataMap
  }

  Column(Modifier.padding(top = 50.dp)) {
    for (y in 0 until MainViewModel.ROW) {
      Row {
        for (x in 0 until MainViewModel.COLUMN) {
          GridItem(
            findGridData(gridDataMap, x, y),
            Point(x, y)
          )
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
fun GridItem(gridData: GridData?, point: Point, size: Dp = 50.dp, textSize: TextUnit = 20.sp) {
  val viewModel = viewModel<MainViewModel>()
  Row(
    Modifier
      .size(size)
      .border(0.5.dp, Color.LightGray)
      .clickable { viewModel.pasteChosenLetter(point) }
  ) {
    gridData?.let {
      PlayerComboText(gridData.red, Player.RED.color, size, textSize)
      PlayerComboText(it.blue, Player.BLUE.color, size, textSize)
    }
  }
}

@Composable
fun PlayerComboText(pairs: Pairs, color: Color, size: Dp = 50.dp, textSize: TextUnit = 20.sp) {
  Log.d("zpp", "Pairs: $pairs")
  Column(
    Modifier
      .width(size / 2)
      .fillMaxHeight()
  ) {
    CommonText(
      text = pairs.first.letter.toString(),
      color = color,
      height = size / 2,
      textSize = textSize
    )
    CommonText(
      text = pairs.second.letter.toString(),
      color = color,
      height = size / 2,
      textSize = textSize
    )
  }
}

@Composable
fun CommonText(text: String, color: Color, height: Dp, textSize: TextUnit) {
  Log.d("zpp", "common text:$text")
  Text(
    text = text,
    Modifier
      .fillMaxWidth()
      .height(height),
    color = color,
    fontSize = textSize,
    textAlign = TextAlign.Center
  )
}

@Preview
@Composable
fun PreviewPlayerText() {
  Row(
    Modifier
      .size(50.dp)
      .border(0.5.dp, Color.LightGray)
  ) {
    PlayerComboText(
      Pairs(CharPoint('A'), CharPoint('B')),
      Player.RED.color,
      50.dp,
      20.sp
    )
    PlayerComboText(
      Pairs(CharPoint('A'), CharPoint('B')),
      Player.RED.color,
      50.dp,
      20.sp
    )
  }
}

@Preview
@Composable
fun PreviewGridItem() {
  GridItem(
    GridData(
      Pairs(CharPoint('A'), CharPoint('B')),
      Pairs(CharPoint('C'), CharPoint('D'))
    ),
    Point(0, 0)
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
          .border(BorderStroke(0.5.dp, if (chosenLetter == l) player.color else Color.LightGray))
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

@Composable
fun TopTips(round: Int, player: Player, playState: PlayState) {
  Column(
    Modifier.padding(top = 60.dp).height(100.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    if (playState != PlayState.SHOW_RESULTS) {
      val roundString = buildAnnotatedString {
        append("第")
        withStyle(style = SpanStyle(Color(0xFFE91E63), 30.sp)) {
          append(round.toString())
        }
        append("轮")
      }
      val playerString = buildAnnotatedString {
        withStyle(style = SpanStyle(player.color, 30.sp)) {
          append("${player.text}轮次")
        }
      }
      Text(text = roundString, fontSize = 20.sp, letterSpacing = 5.sp)
      Text(text = playerString, fontSize = 20.sp, letterSpacing = 5.sp)
    } else {
      Text(text = "第${round}轮完毕 展示双方选择结果")
    }
  }
}

@Preview
@Composable
fun PrevTopTips() {
  TopTips(round = 2, player = Player.BLUE, playState = PlayState.PLAYING)
}

fun main() {
  //
}
