package segithub.id.app.module

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import segithub.id.BuildConfig
import segithub.id.domain.api.GithubApi
import segithub.id.domain.api.GithubApiClient
import segithub.id.domain.repository.GithubRepository
import timber.log.Timber
import java.util.concurrent.TimeUnit

const val TOME_OUT: Long = 30

val githubApiModule = module {
    factory { provideHttpLoggingInterceptor() }
    factory { provideGson() }
    factory { provideOkHttpClient(get()) }
    factory { provideGithubApi(get()) }
    single { provideRetrofit(get(), get()) }
}

val githubApiClientModule = module {
    single<GithubApiClient> { GithubRepository(get()) }
}

fun provideGithubApi(retrofit: Retrofit): GithubApi = retrofit.create(GithubApi::class.java)

fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

fun provideGson(): Gson {
    return GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
        .setLenient()
        .create()
}

fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

    val builder = OkHttpClient.Builder()

    builder
        .connectTimeout(TOME_OUT, TimeUnit.SECONDS)
        .readTimeout(TOME_OUT, TimeUnit.SECONDS)
        .writeTimeout(TOME_OUT, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader(
                "Authorization",
                Credentials.basic("fajarsubeki06", "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")
            )
            chain.proceed(requestBuilder.build())
        }

    if (BuildConfig.DEBUG) {
        builder.addInterceptor(httpLoggingInterceptor)
    }

    return builder.build()
}

fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor { message ->
        Timber.tag("GithubAPI").i(message)
    }
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return interceptor
}