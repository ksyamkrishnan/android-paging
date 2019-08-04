package com.example.android.codelabs.paging.db

import androidx.paging.DataSource
import com.example.android.codelabs.paging.model.RepoRealm
import com.zhuinden.monarchy.Monarchy

class RepoDaoRealm(val monarchy: Monarchy) {
    private lateinit var dataSourceFactory: DataSource.Factory<Int, RepoRealm>

    fun insert(repos: List<RepoRealm>) {
        monarchy.doWithRealm { realm ->
            realm.executeTransaction { realm ->
                realm.insert(repos)
            }
        }
    }

    fun reposByName(name: String): DataSource.Factory<Int, RepoRealm> {

        monarchy.doWithRealm { realm ->
            realm.executeTransaction { realm ->
                var query = realm.where(RepoRealm::class.java).equalTo("name",name)
                var datasource = monarchy.createDataSourceFactory({ realm -> query })
                dataSourceFactory = (datasource.map { input ->
                    var repoRealm = RepoRealm()
                    repoRealm.id = input.id
                    repoRealm.name = input.name
                    repoRealm.fullName = input.fullName
                    repoRealm.url = input.url
                    repoRealm.description = input.description
                    repoRealm.stars = input.stars
                    repoRealm.forks = input.forks
                    repoRealm.language = input.language
                } as? DataSource.Factory<Int, RepoRealm>)!!

            }

        }
        return dataSourceFactory
    }
}