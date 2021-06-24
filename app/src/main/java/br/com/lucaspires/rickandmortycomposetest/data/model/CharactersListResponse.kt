package br.com.lucaspires.rickandmortycomposetest.data.model

import com.google.gson.annotations.SerializedName

data class CharactersListResponse(

    @SerializedName("results")
    val results: List<Character>? = null,

    @SerializedName("info")
    val info: Info? = null
)

data class Character(

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("gender")
    val gender: String? = null,

    @SerializedName("species")
    val species: String? = null,

    @SerializedName("created")
    val created: String? = null,

    @SerializedName("origin")
    val origin: Origin? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("location")
    val location: Location? = null,

    @SerializedName("episode")
    val episode: List<String?>? = null,

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("url")
    val url: String? = null,

    @SerializedName("status")
    val status: String? = null
) {
    fun toSimpleCharacterModel() = SimpleCharacterModel(
        name = name.orEmpty(),
        imageUrl = image.orEmpty(),
        specie = species.orEmpty(),
        origin = origin?.name.orEmpty(),
        location = location?.name.orEmpty()
    )
}

data class SimpleCharacterModel(
    val name: String,
    val imageUrl: String,
    val specie: String,
    val origin: String,
    val location: String
)

data class Origin(

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("url")
    val url: String? = null
)

data class Info(

    @SerializedName("next")
    val next: String? = null,

    @SerializedName("pages")
    val pages: Int? = null,

    @SerializedName("prev")
    val prev: String? = null,

    @SerializedName("count")
    val count: Int? = null
)

data class Location(

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("url")
    val url: String? = null
)
