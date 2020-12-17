package com.example.imdbviewer.data.network.tmdb.models.item.tv

import com.example.imdbviewer.data.network.tmdb.models.companies.ProductionCompaniesDto
import com.example.imdbviewer.data.network.tmdb.models.countries.ProductionCountriesDto
import com.example.imdbviewer.data.network.tmdb.models.genres.GenreDto
import com.example.imdbviewer.data.network.tmdb.models.languages.SpokenLanguagesDto
import com.example.imdbviewer.data.network.tmdb.models.people.CreditsDto
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2020 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class TmdbTVDetailsDto(
    @Expose
    @SerializedName("backdrop_path") val backdrop_path: String?,
    @Expose
    @SerializedName("created_by") val created_by: List<CreatedByDto>?,
    @Expose
    @SerializedName("episode_run_time") val episode_run_time: List<Int>?,
    @Expose
    @SerializedName("first_air_date") val first_air_date: String?,
    @Expose
    @SerializedName("genres") val genres: List<GenreDto>?,
    @Expose
    @SerializedName("homepage") val homepage: String?,
    @Expose
    @SerializedName("id") val id: Int,
    @Expose
    @SerializedName("in_production") val in_production: Boolean?,
    @Expose
    @SerializedName("languages") val languages: List<String>?,
    @Expose
    @SerializedName("last_air_date") val last_air_date: String?,
    @Expose
    @SerializedName("last_episode_to_air") val last_episodeToAir: LastEpisodeToAirDto?,
    @Expose
    @SerializedName("name") val name: String?,
    @Expose
    @SerializedName("next_episode_to_air") val next_episodeToAir: NextEpisodeToAirDto?,
    @Expose
    @SerializedName("number_of_episodes") val number_of_episodes: Int?,
    @Expose
    @SerializedName("number_of_seasons") val number_of_seasons: Int?,
    @Expose
    @SerializedName("origin_country") val origin_country: List<String>?,
    @Expose
    @SerializedName("original_language") val original_language: String?,
    @Expose
    @SerializedName("original_name") val original_name: String?,
    @Expose
    @SerializedName("overview") val overview: String?,
    @Expose
    @SerializedName("popularity") val popularity: Double?,
    @Expose
    @SerializedName("poster_path") val poster_path: String?,
    @Expose
    @SerializedName("production_companies") val production_companyDtos: List<ProductionCompaniesDto>?,
    @Expose
    @SerializedName("production_countries") val production_countries: List<ProductionCountriesDto>?,
    @Expose
    @SerializedName("seasons") val seasons: List<SeasonsDto>?,
    @Expose
    @SerializedName("spoken_languages") val spoken_languages: List<SpokenLanguagesDto>?,
    @Expose
    @SerializedName("status") val status: String?,
    @Expose
    @SerializedName("tagline") val tagline: String?,
    @Expose
    @SerializedName("type") val type: String?,
    @Expose
    @SerializedName("vote_average") val vote_average: Double?,
    @Expose
    @SerializedName("vote_count") val vote_count: Int?,
    @Expose
    @SerializedName("credits") val credits: CreditsDto?,

    val isFavorite: Boolean
)