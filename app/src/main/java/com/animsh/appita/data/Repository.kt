package com.animsh.appita.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

/**
 * Created by animsh on 2/22/2021.
 */
@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource
) {
    val remote = remoteDataSource
}