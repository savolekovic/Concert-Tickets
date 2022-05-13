package com.example.concerttickets.modules.concert_tickets.adapters

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdapterModule {

    @Singleton
    @Provides
    fun provideDiscountAdapter(): DiscountAdapter {
        return DiscountAdapter()
    }

    @Singleton
    @Provides
    fun provideExpiredAdapter(): ExpiredAdapter {
        return ExpiredAdapter()
    }

    @Singleton
    @Provides
    @Named("upcoming")
    fun provideUpcomingAdapter(): UpcomingAdapter {
        return UpcomingAdapter()
    }

    //We don't use any scopes because we need multiple instances of the adapter in a single fragment
    @Provides
    @Named("admin")
    fun provideAdminAdapter(): UpcomingAdapter {
        return UpcomingAdapter()
    }

}