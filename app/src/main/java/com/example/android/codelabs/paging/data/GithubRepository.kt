/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.codelabs.paging.data

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import com.example.android.codelabs.paging.api.GithubService
import com.example.android.codelabs.paging.db.GithubLocalCache
import com.example.android.codelabs.paging.model.Repo
import com.example.android.codelabs.paging.model.RepoSearchResult
import com.zhuinden.monarchy.Monarchy

/**
 * Repository class that works with local and remote data sources.
 */
class GithubRepository(
        private val monarchy: Monarchy,
    private val service: GithubService,
    private val cache: GithubLocalCache
) {

    /**
     * Search repositories whose names match the query.
     */
    fun search(query: String): RepoSearchResult {
        Log.d("GithubRepository", "New query: $query")

        // Get data source factory from the local cache
        val realmDataSourceFactory = cache.reposByName(query)

        // every new query creates a new BoundaryCallback
        // The BoundaryCallback will observe when the user reaches to the edges of
        // the list and update the database with extra datagit
        val boundaryCallback = RepoBoundaryCallback(query, service, cache)
        val networkErrors = boundaryCallback.networkErrors

        // Get the paged list
        /*val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build()
*/
        var dataSourceFactory = (realmDataSourceFactory.map { input ->
            Repo(input.id,input.name,input.fullName,input.description,
                    input.url,input.stars,input.forks,input.language)

        } ) as DataSource.Factory<Int, Repo>
        val data = monarchy.findAllPagedWithChanges(
                realmDataSourceFactory,
                LivePagedListBuilder<Int, Repo>(dataSourceFactory, DATABASE_PAGE_SIZE).setBoundaryCallback(boundaryCallback))

        // Get the network errors exposed by the boundary callback
        return RepoSearchResult(data, networkErrors)
    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}
