package jp.satorufujiwara.kotlin.data.repository

import android.arch.lifecycle.Transformations.map
import android.view.View.Y
import io.reactivex.Flowable
import io.reactivex.internal.operators.single.SingleInternalHelper.toFlowable
import jp.satorufujiwara.kotlin.data.api.GitHubService
import jp.satorufujiwara.kotlin.data.model.Animal
import java.util.*
import java.util.stream.Collectors.toList
import javax.inject.Inject

class GitHubRepository @Inject constructor(private val gitHubService: GitHubService) {

    fun loadGitRepos(owner: String) = gitHubService.listRepos(owner)

    fun loadAnimals(): Flowable<List<Animal>> {
        val w = Flowable.fromIterable(Arrays.asList(Animal("M","Maus"), Animal("H","Hase"), Animal("I","Igel")))
        val observable = w.toList()
        return observable
                .toFlowable()

//        val integerList = ArrayList<String>()
//        return Flowable
//                .just<List<String>>(integerList)//emits the list
//                .flatMap { list -> Flowable.fromIterable(list) }//emits one by one
//                .map { item -> item.toList() }
    }

}
