package com.example.androiddevchallenge

import java.util.concurrent.TimeUnit

class Util {

    companion object{
        fun getFormattedStopWatchTime(sec: Long): String {
            var seconds = sec
            // Convert to hours
            val hours = TimeUnit.SECONDS.toHours(seconds)
            seconds -= TimeUnit.HOURS.toSeconds(hours)

            // Convert to minutes
            val minutes = TimeUnit.SECONDS.toMinutes(seconds)
            seconds -= TimeUnit.MINUTES.toSeconds(minutes)

            val time = "${if (hours <10) "0" else ""}$hours:" +
                    "${if (minutes < 10) "0" else ""}$minutes:" +
                    "${if (seconds < 10) "0" else ""}$seconds"

            return time
        }
    }



}