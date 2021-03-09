/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.screen

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.Util.Companion.getFormattedStopWatchTime
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit
import android.view.WindowManager

import android.os.Build
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.R


class TimerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        setContent {
            MyTheme {
                MyApp()
            }
        }
    }

}

val gray = Color(0xFF737373)

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}

// Start building your app here!
@Composable
fun MyApp() {

    var progressPercentageState = progressPercentage.collectAsState()
    var counterValue = timeLeftInSeconds.collectAsState()
    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color.Transparent, Color(0xFFDE8500))
                )
            )
    ) {
        val (timer, progress, startBtn, stopBtn) = createRefs()
        val half = createGuidelineFromAbsoluteLeft(0.5f)


        Card(
            modifier = Modifier
                .size(220.dp)
                .constrainAs(progress) {
                    top.linkTo(parent.top, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                },
            shape = CircleShape,
            backgroundColor = Color.White,
            elevation = 8.dp,
        ) {

            ProgressIndicatorComponent(progressPercentageState.value)

        }

        Text(
            text = getFormattedStopWatchTime(counterValue.value),
            color = gray,
            modifier = Modifier.constrainAs(timer) {
                top.linkTo(progress.top, margin = 20.dp)
                start.linkTo(progress.start, margin = 20.dp)
                end.linkTo(progress.end, margin = 20.dp)
                bottom.linkTo(progress.bottom, margin = 20.dp)
            },
            style = MaterialTheme.typography.body1
        )

        Card(shape = CircleShape,
            backgroundColor = Color.White, modifier = Modifier
                .size(90.dp)
                .constrainAs(startBtn) {
                    top.linkTo(progress.bottom, margin = 20.dp)
                    start.linkTo(parent.start, margin = 20.dp)
                    end.linkTo(half)
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                }
        ) {

            Button(
                modifier = Modifier.fillMaxSize(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                onClick = {
                    currenTimer?.cancel()
                    currenTimer = myCountdownTimer(context, totalTimeSeconds.toLong())


                }) {
                Text(
                    stringResource(R.string.start),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2
                )
            }
        }


        Card(shape = CircleShape, elevation = 8.dp,
            backgroundColor = Color(0xFFA75050), modifier = Modifier
                .size(90.dp)
                .constrainAs(stopBtn) {
                    top.linkTo(progress.bottom, margin = 20.dp)
                    start.linkTo(half, margin = 20.dp)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                }) {
            Button(
                modifier = Modifier.fillMaxSize(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFA75050)),
                onClick = {
                    currenTimer?.cancel()
                    timeLeftInSeconds.value = 0L
                    progressPercentage.value = 0F
                }) {
                Text(
                    stringResource(R.string.stop),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2,
                    color = Color.White
                )
            }


        }


    }

}

private val timeLeftInSeconds = MutableStateFlow<Long>(0L)
private var progressPercentage = MutableStateFlow<Float>(0F)

private var currenTimer: Job? = null
private var totalTimeSeconds = 2 * 60
private var delayValue =  1000L

private fun myCountdownTimer(cnt: Context, totalSeconds: Long): Job? {

    var mainTimer: Job? = null

    mainTimer = CoroutineScope(Dispatchers.Main).launch {
        for (seconds in totalSeconds downTo 0) {

            progressPercentage.value = 1 - ((seconds.toFloat() / totalTimeSeconds.toFloat()))
            timeLeftInSeconds.value = seconds

            Log.d(TAG, "percentage = ${progressPercentage.value}")
            delay(delayValue) // sec
        }
        Log.d(TAG, "Finished")
    }
    return mainTimer
}
