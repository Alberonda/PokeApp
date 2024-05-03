package com.example.pokeapp.base

import org.junit.Assert
import org.junit.Test


class StringUtilsKtTest {

    @Test
    fun capitalizeValue() {
        Assert.assertEquals(
            "Sample string",
            "sample string".capitalizeValue()
        )
    }
}