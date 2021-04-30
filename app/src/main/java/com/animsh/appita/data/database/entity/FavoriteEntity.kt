package com.animsh.appita.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.animsh.appita.models.Result
import com.animsh.appita.util.Constants.Companion.FAVORITE_RECIPE_TABLE

/**
 * Created by animsh on 4/30/2021.
 */
@Entity(tableName = FAVORITE_RECIPE_TABLE)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)