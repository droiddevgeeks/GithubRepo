package com.example.github.repositories.di

import com.example.github.repositories.data.GITHUB_URL
import com.example.github.repositories.data.GitHubEndpoints
import com.example.github.repositories.repository.GithubRepositoryImpl
import com.example.github.repositories.repository.IGithubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GITHUB_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideGithubRepository(apiService: GitHubEndpoints): IGithubRepository {
        return GithubRepositoryImpl(apiService)
    }

    @Provides
    fun provideService(retrofit: Retrofit): GitHubEndpoints {
        return retrofit.create(GitHubEndpoints::class.java)
    }
}