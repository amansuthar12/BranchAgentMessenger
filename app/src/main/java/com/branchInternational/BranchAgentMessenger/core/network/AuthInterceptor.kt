package com.branchInternational.BranchAgentMessenger.core.network

import com.branchInternational.BranchAgentMessenger.core.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // Add the "X-Branch-Auth-Token" header if we have a saved token
        tokenManager.getToken()?.let { token ->
            requestBuilder.addHeader("X-Branch-Auth-Token", token)
        }

        return chain.proceed(requestBuilder.build())
    }
}