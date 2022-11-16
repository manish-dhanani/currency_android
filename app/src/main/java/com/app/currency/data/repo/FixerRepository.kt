package com.app.currency.data.repo

import com.app.currency.data.local.ConversionData
import com.app.currency.data.local.ConvertResult
import com.app.currency.data.remote.retrofit.FixerRetrofit
import com.app.currency.data.util.Result
import com.app.currency.util.Constants
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FixerRepository @Inject constructor(private val fixerRetrofit: FixerRetrofit) {

    suspend fun getSymbols(): Flow<Result<String>> {
        return flow {
            emit(Result.loading())

            try {
                fixerRetrofit.getSymbols().let { response ->
                    if (response.isJsonObject && response.asJsonObject.has("success")) {
                        val success = response.asJsonObject["success"].asBoolean

                        when {
                            success -> {
                                when {
                                    response.asJsonObject.has("symbols") &&
                                            response.asJsonObject["symbols"].isJsonObject -> {

                                        // Emit Result success with symbols json as string.
                                        emit(Result.success(response.asJsonObject["symbols"].asJsonObject.toString()))
                                    }
                                    else -> {
                                        emit(Result.error(null))
                                    }
                                }
                            }
                            response.asJsonObject.has("error") &&
                                    !response.asJsonObject["error"].isJsonNull -> {
                                emit(Result.error(getErrorMessage(response.asJsonObject["error"].asJsonObject)))
                            }
                            else -> {
                                emit(Result.error(null))
                            }
                        }
                    } else {
                        emit(Result.error(null))
                    }
                }

            } catch (httpError: HttpException) {
                emit(Result.error(getHttpErrorMessage(httpError)))

            } catch (e: Exception) {
                emit(Result.error(e.message))
            }

        }.flowOn(Dispatchers.IO)
    }

    suspend fun convert(from: String, to: String, amount: Double): Flow<Result<ConvertResult>> {
        return flow {
            emit(Result.loading())

            try {
                fixerRetrofit.convert(from, to, amount).let { response ->
                    if (response.isJsonObject && response.asJsonObject.has("success")) {
                        val success = response.asJsonObject["success"].asBoolean

                        when {
                            success -> {
                                when {
                                    response.asJsonObject.has("info") &&
                                            response.asJsonObject.has("result") -> {

                                        val info = Gson().fromJson(
                                            response.asJsonObject["info"],
                                            ConvertResult.Info::class.java
                                        )
                                        val convertResult = ConvertResult(
                                            info, response.asJsonObject["result"].asDouble
                                        )

                                        // Emit Result success with convertResult.
                                        emit(Result.success(convertResult))
                                    }
                                    else -> {
                                        emit(Result.error(null))
                                    }
                                }
                            }
                            response.asJsonObject.has("error") &&
                                    !response.asJsonObject["error"].isJsonNull -> {
                                emit(Result.error(getErrorMessage(response.asJsonObject["error"].asJsonObject)))
                            }
                            else -> {
                                emit(Result.error(null))
                            }
                        }
                    } else {
                        emit(Result.error(null))
                    }
                }

            } catch (httpError: HttpException) {
                emit(Result.error(getHttpErrorMessage(httpError)))

            } catch (e: Exception) {
                emit(Result.error(e.message))
            }

        }.flowOn(Dispatchers.IO)
    }

    suspend fun getHistoricalData(
        base: String, symbols: String, startDate: String, endDate: String
    ): Flow<Result<List<ConversionData>>> {
        return flow {
            emit(Result.loading())

            try {
                fixerRetrofit.getHistoricalData(base, symbols, startDate, endDate).let { response ->
                    if (response.isJsonObject && response.asJsonObject.has("success")) {
                        val success = response.asJsonObject["success"].asBoolean

                        when {
                            success -> {
                                when {
                                    response.asJsonObject.has("rates") &&
                                            response.asJsonObject["rates"].isJsonObject -> {

                                        val ratesMap: MutableMap<String, JsonElement> =
                                            response.asJsonObject["rates"].asJsonObject.asMap()

                                        // Prepare historical data for single target only.
                                        val target: String = symbols.split(",")[0]

                                        val dataList: ArrayList<ConversionData> = arrayListOf()
                                        dataList.apply {
                                            ratesMap.forEach {
                                                add(
                                                    ConversionData(
                                                        base, target, date = it.key,
                                                        rate = it.value.asJsonObject[target].asDouble
                                                    )
                                                )
                                            }
                                        }

                                        // Emit Result success with dataList.
                                        emit(Result.success(dataList))
                                    }
                                    else -> {
                                        emit(Result.error(null))
                                    }
                                }
                            }
                            response.asJsonObject.has("error") &&
                                    !response.asJsonObject["error"].isJsonNull -> {
                                emit(Result.error(getErrorMessage(response.asJsonObject["error"].asJsonObject)))
                            }
                            else -> {
                                emit(Result.error(null))
                            }
                        }
                    } else {
                        emit(Result.error(null))
                    }
                }

            } catch (httpError: HttpException) {
                emit(Result.error(getHttpErrorMessage(httpError)))

            } catch (e: Exception) {
                emit(Result.error(e.message))
            }

        }.flowOn(Dispatchers.IO)
    }

    suspend fun getPopularConversions(
        base: String,
        symbols: String
    ): Flow<Result<List<ConversionData>>> {
        return flow {
            emit(Result.loading())

            try {
                fixerRetrofit.getPopularConversions(base, symbols).let { response ->
                    if (response.isJsonObject && response.asJsonObject.has("success")) {
                        val success = response.asJsonObject["success"].asBoolean

                        when {
                            success -> {
                                when {
                                    response.asJsonObject.has("rates") &&
                                            response.asJsonObject["rates"].isJsonObject -> {

                                        val date: String = when {
                                            response.asJsonObject.has("date") -> {
                                                response.asJsonObject["date"].asString
                                            }
                                            else -> {
                                                val formatter = SimpleDateFormat(
                                                    Constants.API_DATE_FORMAT, Locale.ENGLISH
                                                )

                                                val calendar = Calendar.getInstance()
                                                formatter.format(calendar.time)
                                            }
                                        }

                                        val ratesMap: MutableMap<String, JsonElement> =
                                            response.asJsonObject["rates"].asJsonObject.asMap()

                                        val dataList: ArrayList<ConversionData> = arrayListOf()
                                        dataList.apply {
                                            ratesMap.forEach {
                                                add(
                                                    ConversionData(
                                                        base, target = it.key, date,
                                                        rate = it.value.asDouble
                                                    )
                                                )
                                            }
                                        }

                                        // Emit Result success with dataList.
                                        emit(Result.success(dataList))
                                    }
                                    else -> {
                                        emit(Result.error(null))
                                    }
                                }
                            }
                            response.asJsonObject.has("error") &&
                                    !response.asJsonObject["error"].isJsonNull -> {
                                emit(Result.error(getErrorMessage(response.asJsonObject["error"].asJsonObject)))
                            }
                            else -> {
                                emit(Result.error(null))
                            }
                        }
                    } else {
                        emit(Result.error(null))
                    }
                }

            } catch (httpError: HttpException) {
                emit(Result.error(getHttpErrorMessage(httpError)))

            } catch (e: Exception) {
                emit(Result.error(e.message))
            }

        }.flowOn(Dispatchers.IO)
    }

    private fun getErrorMessage(errorJson: JsonObject): String? {
        return when {
            errorJson.has("info") && !errorJson["info"].isJsonNull -> {
                errorJson["info"].asString
            }
            else -> null
        }
    }

    private fun getHttpErrorMessage(httpError: HttpException): String? {
        val errorBody = httpError.response()?.errorBody()?.string()

        return when {
            errorBody != null -> {
                val errorJson = JsonParser.parseString(errorBody).asJsonObject

                when {
                    errorJson.has("message") && !errorJson["message"].isJsonNull -> {
                        errorJson["message"].asString
                    }
                    else -> httpError.message()
                }
            }
            else -> null
        }
    }

}