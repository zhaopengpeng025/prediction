package com.jiayou.predication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.jiayou.predication.ui.custom.TopBar
import com.jiayou.predication.ui.screen.GameMainScreen
import com.jiayou.predication.ui.theme.PredicationTheme

class MainActivity : ComponentActivity() {
  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      PredicationTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = { TopBar(this) }
        ) {
          GameMainScreen()
        }
      }
    }
  }
}
