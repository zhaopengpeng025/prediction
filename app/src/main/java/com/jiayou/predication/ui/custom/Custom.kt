package com.jiayou.predication.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jiayou.predication.MainActivity
import com.jiayou.predication.MainViewModel
import com.jiayou.predication.ui.theme.Purple40

/***********************************************
 * Author: Zhao Pengpeng
 * Date: 2023/1/13
 * Describe:
 ***********************************************/
@Composable
fun ActionBar() {
  Row(
    Modifier
      .fillMaxWidth()
      .background(color = Purple40)
      .padding(start = 10.dp, end = 10.dp)
      .height(50.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = "退出", color = Color.White)
    Text(text = "练习", color = Color.White)
    Text(text = "清除", Modifier.clickable { }, color = Color.White)
  }
}

@Composable
fun TopBar(mainActivity: MainActivity) {
  val viewModel = viewModel<MainViewModel>()
  CenterAlignedTopAppBar(
    title = {
      Text(text = "练习")
    },
    navigationIcon = {
      IconButton(onClick = { mainActivity.finish() }) {
        Icon(
          imageVector = Icons.Filled.ArrowBack,
          contentDescription = "Back"
        )
      }
    },
    actions = {
      IconButton(onClick = { viewModel.buttonClearLetters() }) {
        Icon(
          imageVector = Icons.Filled.Delete,
          contentDescription = "Back"
        )
      }
    }
  )
}
