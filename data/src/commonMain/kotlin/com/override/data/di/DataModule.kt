package com.override.data.di

import androidx.room.Database
import com.override.data.dao.AttendanceDao
import com.override.data.dao.ClassDao
import com.override.data.dao.StudentDao
import com.override.data.db.AppDatabase
import com.override.data.db.getDatabase
import com.override.data.getDesktopDatabaseBuilder
import com.override.data.repository.contract.AttendanceRepository
import com.override.data.repository.contract.ClassRepository
import com.override.data.repository.contract.StudentRepository
import com.override.data.repository.impl.AttendanceRepositoryImpl
import com.override.data.repository.impl.ClassRepositoryImpl
import com.override.data.repository.impl.StudentRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule: Module = module {
    single<AppDatabase> { getDatabase(getDesktopDatabaseBuilder()) }
}

val repositoryDataModule: Module = module {
    factoryOf(::ClassRepositoryImpl).bind(ClassRepository::class)
    factoryOf(::AttendanceRepositoryImpl).bind(AttendanceRepository::class)
    factoryOf(::StudentRepositoryImpl).bind(StudentRepository::class)
}

val daoModule: Module = module {
    factory<ClassDao> { get<AppDatabase>().classDao() }
    factory<AttendanceDao> { get<AppDatabase>().attendanceDao() }
    factory<StudentDao> { get<AppDatabase>().studentDao() }
}