package com.app.currency.data.repo

import com.app.currency.data.remote.retrofit.FixerRetrofit
import com.app.currency.data.util.Result
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class FixerRepository @Inject constructor(private val fixerRetrofit: FixerRetrofit) {

    suspend fun getSymbols(): Flow<Result<String>> {
        return flow<Result<String>> {
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
                                getErrorMessage(response.asJsonObject["error"].asJsonObject)
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