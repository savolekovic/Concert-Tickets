package com.example.concerttickets.services.persistence.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideTicketsDatabase(
        @ApplicationContext context: Context
    ): TicketsDatabase {
        return Room.databaseBuilder(
            context,
            TicketsDatabase::class.java,
            TicketsDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideTicketsDao(
        ticketsDatabase: TicketsDatabase
    ): TicketsDao {
        return ticketsDatabase.ticketsDao()
    }

}