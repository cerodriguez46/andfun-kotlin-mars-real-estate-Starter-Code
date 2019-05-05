/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


private const val BASE_URL = "https://mars.udacity.com/"

//create a Moshi object
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        //add the moshi converter factory, delete scalars converter factory
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        //add kotlin coroutine adapter, returns something other than the default call class
        //allows us to replace call in getProperties with coroutine Deferred
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

interface MarsApiService {
    @GET("realestate")
    fun getProperties():
    // with moshi can now return a list of MarsProperty objects into a json array instead of
    // a json string
    //replace the call with a Defferred, is a coroutine job that can directly return a result
    //can cancel and determine state of a coroutine
    //has an await function that suspsends the code without blocking UI, until value is ready and then returned
            Deferred<List<MarsProperty>>
}

//this exposes the data to the rest of the application, makes retrofit object to implement retrofit service
object MarsApi{

    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}