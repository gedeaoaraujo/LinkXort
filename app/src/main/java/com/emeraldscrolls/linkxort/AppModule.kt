package com.emeraldscrolls.linkxort

import com.emeraldscrolls.linkxort.remote.ILinkService
import com.emeraldscrolls.linkxort.remote.LinkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  private const val BASE_URL = "http://localhost:8080"

  @Provides
  @Singleton
  fun provideMainRepository(api: ILinkService): MainRepository {
    return MainRepository(api)
  }

  @Provides
  @Singleton
  fun provideApiService(): LinkService {
    return Retrofit.Builder()
      .baseUrl(BASE_URL).build()
      .create(LinkService::class.java)
  }
}

