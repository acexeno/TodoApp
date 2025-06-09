package com.example.todomanager.di

import android.content.Context
import androidx.room.Room
import com.example.todomanager.data.api.TodoApiService
import com.example.todomanager.data.db.TodoDatabase
import com.example.todomanager.data.repository.TodoRepository
import com.example.todomanager.data.repository.TodoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideTodoRepository(
        apiService: TodoApiService,
        db: TodoDatabase,
        ioDispatcher: CoroutineDispatcher
    ): TodoRepository {
        return TodoRepositoryImpl(apiService, db.todoDao(), ioDispatcher)
    }
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo_database"
        ).build()
    }
    
    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
