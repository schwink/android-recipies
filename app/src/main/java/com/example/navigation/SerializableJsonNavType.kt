package com.example.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

/**
 * Helper to create a custom [NavType] from a @[kotlinx.serialization.Serializable] data class, so
 * that custom types can be passed in [androidx.navigation.compose.NavHost] destinations.
 * https://developer.android.com/guide/navigation/design/kotlin-dsl#custom-types
 *
 * Use it like:
 * ```kotlin
 * object MyDataClassNavType :
 *     SerializableJsonNavType<MyDataClass>(MyDataClass.serializer())
 *
 * val typeMap = mapOf(
 *     Pair(typeOf<MyDataClass?>(), MyDataClassNavType)
 * )
 * ```
 */
open class SerializableJsonNavType<T>(private val serializer: KSerializer<T>) :
    NavType<T?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getString(key)?.let {
            parseValue(it)
        }
    }

    override fun put(bundle: Bundle, key: String, value: T?) {
        value?.run {
            bundle.putString(
                key,
                serializeAsValue(value)
            )
        }
    }

    override fun serializeAsValue(value: T?): String {
        return value?.let {
            // The string must be Uri encoded to be embedded in the route
            Uri.encode(
                Json.encodeToString(
                    serializer = serializer,
                    value = value
                )
            )
        } ?: "null"
    }

    override fun parseValue(value: String): T {
        return Json.decodeFromString(
            deserializer = serializer,
            string = Uri.decode(value),
        )
    }

}