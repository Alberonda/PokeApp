package com.example.pokeapp.base

import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun Date.getDifferenceInMinutes(toDate: Date) =
    TimeUnit.MILLISECONDS.toMinutes(
        abs(this.time - toDate.time)
    )

