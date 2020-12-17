package com.example.imdbviewer.data.network.tmdb.models.people

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2020 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class CastDto (
	@Expose
	@SerializedName("adult") val adult : Boolean?,
	@Expose
	@SerializedName("gender") val gender : Int?,
	@Expose
	@SerializedName("id") val id : Int,
	@Expose
	@SerializedName("known_for_department") val known_for_department : String?,
	@Expose
	@SerializedName("name") val name : String?,
	@Expose
	@SerializedName("original_name") val original_name : String?,
	@Expose
	@SerializedName("popularity") val popularity : Double?,
	@Expose
	@SerializedName("profile_path") val profile_path : String?,
	@Expose
	@SerializedName("cast_id") val cast_id : Int?,
	@Expose
	@SerializedName("character") val character : String?,
	@Expose
	@SerializedName("credit_id") val credit_id : String?,
	@Expose
	@SerializedName("order") val order : Int?
)