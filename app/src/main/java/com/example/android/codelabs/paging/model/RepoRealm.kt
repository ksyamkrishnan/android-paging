package com.example.android.codelabs.paging.model

import androidx.recyclerview.widget.DiffUtil
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RepoRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 0L
    var name: String? = ""
    var fullName: String? = ""
    var description: String? = ""
    var url: String? = ""
    var stars: Int = 0
    var forks: Int = 0
    var language: String? = ""

    companion object {
        var ITEM_CALLBACK: DiffUtil.ItemCallback<Repo> = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}