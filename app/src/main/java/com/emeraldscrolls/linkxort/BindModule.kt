package com.emeraldscrolls.linkxort

import com.emeraldscrolls.linkxort.remote.ILinkService
import com.emeraldscrolls.linkxort.remote.LinkShortenerApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {
  @Binds
  abstract fun bindLinkService(api: LinkShortenerApi): ILinkService
}