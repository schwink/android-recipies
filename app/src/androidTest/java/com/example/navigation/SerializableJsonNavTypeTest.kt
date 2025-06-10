package com.example.navigation

import android.os.Bundle
import kotlinx.serialization.Serializable
import org.junit.Assert.assertEquals
import org.junit.Test

// Needs to be in androidTest because of dependency on android.net.Uri
class SerializableJsonNavTypeTest {

    @Serializable
    private data class SimpleTestObject(val s: String, val i: Int)

    private val navType = SerializableJsonNavType(
        SimpleTestObject.serializer()
    )

    @Test
    fun throughBundle() {
        val bundle = Bundle()

        val original = SimpleTestObject("Sally", 5)
        navType.put(bundle, "item", original)
        val result = navType.get(bundle, "item")

        assertEquals(original, result)
    }

    @Test
    fun serializeAndParseThroughString() {
        val original = SimpleTestObject("Sally", 5)

        val serialized = navType.serializeAsValue(original)
        val actual = navType.parseValue(serialized)

        assertEquals(original, actual)
    }

}