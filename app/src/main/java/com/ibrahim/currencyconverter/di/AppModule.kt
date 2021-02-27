package com.ibrahim.currencyconverter.di

import com.ibrahim.core.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideJsonConverterFactory(): Converter.Factory {
        return JacksonConverterFactory.create()
    }

    @LoggingInterceptor
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
    }

    @APIAccessKeyInterceptor
    @Provides
    @Singleton
    fun provideApiAccessKeyInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original: Request = chain.request()
            val originalHttpUrl: HttpUrl = original.url
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("access_key", "20d9fde9b076c2c7034e156ce7b009d4")
                .build()
            val requestBuilder: Request.Builder = original.newBuilder()
                .url(url)
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @LoggingInterceptor loggingInterceptor: Interceptor,
        @APIAccessKeyInterceptor apiAccessKeyInterceptor: Interceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(apiAccessKeyInterceptor)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        factory: Converter.Factory
    ): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("http://data.fixer.io/api/")
            .client(okHttpClient)
            .addConverterFactory(factory)
            .build()
    }

}