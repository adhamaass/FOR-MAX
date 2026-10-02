package com.example.formax.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        UserProfileEntity::class,
        ExerciseEntity::class,
        ExerciseMediaEntity::class,
        ExerciseAlternativeEntity::class,
        TrainingProgramEntity::class,
        TrainingDayEntity::class,
        WorkoutSessionEntity::class,
        WorkoutExerciseEntity::class,
        ExerciseSetEntity::class,
        PersonalRecordEntity::class,
        BodyMeasurementEntity::class,
        ProgressPhotoEntity::class,
        CardioSessionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ForMaxDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun mediaDao(): MediaDao
    abstract fun alternativeDao(): AlternativeDao
    abstract fun programDao(): ProgramDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun progressDao(): ProgressDao
    abstract fun cardioDao(): CardioDao

    companion object {
        @Volatile
        private var INSTANCE: ForMaxDatabase? = null

        fun getInstance(context: Context): ForMaxDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ForMaxDatabase::class.java,
                    "formax_database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
