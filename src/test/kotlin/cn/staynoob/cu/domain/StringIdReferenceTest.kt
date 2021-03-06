package cn.staynoob.cu.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class StringIdReferenceTest {

    private val objectMapper = ObjectMapper()
            .registerKotlinModule()

    @Test
    @DisplayName("deserialize object shape")
    fun test100() {
        val json = """
            {
                "id": "1"
            }
        """.trimIndent()
        val res = objectMapper.readValue<StringIdReference>(json)
        val expected = StringIdReference("1")
        assertThat(res).isEqualTo(expected)
    }

    @Test
    @DisplayName("deserialize String literal")
    fun test200() {
        val json = """"1""""
        val res = objectMapper.readValue<StringIdReference>(json)
        val expected = StringIdReference("1")
        assertThat(res).isEqualTo(expected)
    }

    data class Test300Fixture(
            val ref: StringIdReference
    )

    @Test
    @DisplayName("deserialize as property")
    fun test300() {
        val json1 = """
            {
                "ref": "1"
            }
        """.trimIndent()


        val json2 = """
            {
                "ref": { "id": "1" }
            }
        """.trimIndent()

        val expected = Test300Fixture(StringIdReference("1"))
        val res1 = objectMapper.readValue<Test300Fixture>(json1)
        val res2 = objectMapper.readValue<Test300Fixture>(json2)

        assertThat(res1).isEqualTo(expected)
        assertThat(res2).isEqualTo(expected)
    }

    data class Test400Fixture(
            val ref: List<StringIdReference>
    )

    @Test
    @DisplayName("deserialize as property collection")
    fun test400() {
        val json1 = """
            {
                "ref": ["1", "2"]
            }
        """.trimIndent()


        val json2 = """
            {
                "ref": [{ "id": "1" }, { "id": "2" }]
            }
        """.trimIndent()

        val expected = listOf(
                StringIdReference("1"),
                StringIdReference("2")
        )

        val res1 = objectMapper.readValue<Test400Fixture>(json1)
        val res2 = objectMapper.readValue<Test400Fixture>(json2)

        assertThat(res1.ref).isEqualTo(expected)
        assertThat(res2.ref).isEqualTo(expected)
    }
}