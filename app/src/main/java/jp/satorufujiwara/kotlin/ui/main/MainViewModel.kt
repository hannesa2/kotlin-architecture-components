package jp.satorufujiwara.kotlin.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.satorufujiwara.kotlin.data.model.Animal
import jp.satorufujiwara.kotlin.data.model.Repo
import jp.satorufujiwara.kotlin.data.repository.GitHubRepository
import jp.satorufujiwara.kotlin.util.AbsentLiveData
import jp.satorufujiwara.kotlin.util.ext.switchMap
import jp.satorufujiwara.kotlin.util.ext.toLiveData
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: GitHubRepository) : ViewModel() {

    val ownerId = MutableLiveData<String>()
    val reposGit: LiveData<List<Repo>>
    val reposAnimals: LiveData<List<Animal>>

    init {
        reposGit = ownerId.switchMap {
            if (it.isEmpty()) AbsentLiveData.create()
            else
                repository.loadGitRepos(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorResumeNext(Flowable.empty())
                        .toLiveData()
        }
        reposAnimals =
            repository.loadAnimals()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(Flowable.empty())
                    .toLiveData()
    }
}