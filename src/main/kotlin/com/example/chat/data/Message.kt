package com.example.chat.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

data class Message(
    @JsonProperty("author") val author: String,
    @JsonProperty("message") val message: String,
    @JsonProperty("time") val time: String
) {
    companion object {
        private val objectMapper = jacksonObjectMapper()

        fun fromJson(json: String): Message {
            return objectMapper.readValue(json)
        }
    }

    fun toJson(): String {
        return objectMapper.writeValueAsString(this)
    }
}
