package com.example.imdbviewer.data.network.tmdb.models.item.tv
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2020 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class TmdbTVListItemDto (
	@Expose
	@SerializedName("id")  val id : Int,
	@Expose
	@SerializedName("original_name") val originalTitle : String?,
	@Expose
	@SerializedName("poster_path") val posterPath : String?,
	@Expose
	@SerializedName("name") val title : String?,
	@Expose
	@SerializedName("vote_average") val voteAverage : Double?,
	@Expose
	@SerializedName("first_air_date") val year : String?,
//	@SerializedName("overview") val overview : String,
//	@SerializedName("backdrop_path") val backdrop_path : String,
	//	@SerializedName("popularity") val popularity : Double,
//	@SerializedName("vote_count") val vote_count : Int,
//	@SerializedName("genre_ids") val genre_ids : List<Int>,
//	@SerializedName("origin_country") val origin_country : List<String>,
//	@SerializedName("original_language") val original_language : String,
)