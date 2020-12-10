package com.example.imdbviewer.models.tmdb.item.movie


import com.example.imdbviewer.models.tmdb.companies.ProductionCompanies
import com.example.imdbviewer.models.tmdb.countries.ProductionCountries
import com.example.imdbviewer.models.tmdb.genres.Genre
import com.example.imdbviewer.models.tmdb.languages.SpokenLanguages
import com.example.imdbviewer.models.tmdb.people.Credits
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2020 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class TmdbMovieDetails (
	@Expose
	@SerializedName("adult") val adult : Boolean?,
	@Expose
	@SerializedName("backdrop_path") val backdrop_path : String?,
	@Expose
	@SerializedName("budget") val budget : Int?,
	@Expose
	@SerializedName("genres") val genres : List<Genre>?,
	@Expose
	@SerializedName("homepage") val homepage : String?,
	@Expose
	@SerializedName("id") val id : Int,
	@Expose
	@SerializedName("imdb_id") val imdb_id : String?,
	@Expose
	@SerializedName("original_language") val original_language : String?,
	@Expose
	@SerializedName("original_title") val original_title : String?,
	@Expose
	@SerializedName("overview") val overview : String?,
	@Expose
	@SerializedName("popularity") val popularity : Double?,
	@Expose
	@SerializedName("poster_path") val poster_path : String?,
	@Expose
	@SerializedName("production_companies") val production_companies : List<ProductionCompanies>?,
	@Expose
	@SerializedName("production_countries") val production_countries : List<ProductionCountries>?,
	@Expose
	@SerializedName("release_date") val release_date : String?,
	@Expose
	@SerializedName("revenue") val revenue : Int?,
	@Expose
	@SerializedName("runtime") val runtime : Int?,
	@Expose
	@SerializedName("spoken_languages") val spoken_languages : List<SpokenLanguages>?,
	@Expose
	@SerializedName("status") val status : String?,
	@Expose
	@SerializedName("tagline") val tagline : String?,
	@Expose
	@SerializedName("title") val title : String?,
	@Expose
	@SerializedName("video") val video : Boolean?,
	@Expose
	@SerializedName("vote_average") val vote_average : Double?,
	@Expose
	@SerializedName("vote_count") val vote_count : Int?,
	@Expose
	@SerializedName("credits") val credits : Credits?,

	val isFavorite: Boolean
)