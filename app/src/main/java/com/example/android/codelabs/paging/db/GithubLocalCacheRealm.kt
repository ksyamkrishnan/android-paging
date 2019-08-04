package com.example.android.codelabs.paging.db

import android.util.Log
import androidx.paging.DataSource
import com.example.android.codelabs.paging.model.RepoRealm
import java.util.concurrent.Executor

class GithubLocalCacheRealm( private val repoDao: RepoDaoRealm,
                             private val ioExecutor: Executor) {

    fun insert(repos: List<RepoRealm>, insertFinished: () -> Unit){
        ioExecutor.execute {
            Log.d("GithubLocalCache", "inserting ${repos.size} repos")
            repoDao.insert(repos)
            insertFinished()
        }
    }

    fun reposByName(name: String): DataSource.Factory<Int, RepoRealm> {
        // appending '%' so we can allow other characters to be before and after the query string
        return repoDao.reposByName(name)
    }
}