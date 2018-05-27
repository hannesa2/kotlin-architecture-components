package jp.satorufujiwara.kotlin.ui.main

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.satorufujiwara.kotlin.R
import jp.satorufujiwara.kotlin.data.model.Animal
import jp.satorufujiwara.kotlin.data.model.Repo
import jp.satorufujiwara.kotlin.databinding.MainFragmentBinding
import jp.satorufujiwara.kotlin.databinding.RepoAnimalItemBinding
import jp.satorufujiwara.kotlin.databinding.RepoGitItemBinding
import jp.satorufujiwara.kotlin.di.Injectable
import jp.satorufujiwara.kotlin.ui.app.UserViewModel
import jp.satorufujiwara.kotlin.util.ext.observe
import javax.inject.Inject

class MainFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userViewModel: UserViewModel
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private val adapterGit = GitAdapter()
    private val adapterAnimal = AnimalAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            DataBindingUtil.inflate<MainFragmentBinding>(inflater, R.layout.main_fragment, container, false)
                    .also {
                        binding = it
                    }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)
        binding.recyclerView.adapter = adapterGit
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        binding.recyclerAnimals.adapter = adapterAnimal
        binding.recyclerAnimals.layoutManager = LinearLayoutManager(activity)

        // ###############
        viewModel.reposGit.observe(this) {
            it ?: return@observe
            adapterGit.run {
                items.clear()
                items.addAll(it)
                notifyDataSetChanged()
            }
        }
        userViewModel.loginUserId.observe(this) {
            viewModel.ownerId.value = it
        }

        // ############
        viewModel.reposAnimals.observe(this) {
            it ?: return@observe
            adapterAnimal.run {
                items.clear()
                items.addAll(it)
                notifyDataSetChanged()
            }
        }

    }

    companion object {
        fun newInstance() = MainFragment()
    }

    inner class GitAdapter : RecyclerView.Adapter<GitAdapter.ViewHolder>() {
        val items = ArrayList<Repo>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitAdapter.ViewHolder {
            return ViewHolder(DataBindingUtil.inflate<RepoGitItemBinding>(
                    LayoutInflater.from(parent.context), R.layout.repo_git_item, parent, false))
        }

        override fun onBindViewHolder(holder: GitAdapter.ViewHolder, position: Int) {
            holder.binding.repoGit = items[position]
        }

        override fun getItemCount() = items.size

        inner class ViewHolder(val binding: RepoGitItemBinding) : RecyclerView.ViewHolder(binding.root)
    }

    inner class AnimalAdapter : RecyclerView.Adapter<AnimalAdapter.ViewHolder>() {
        val items = ArrayList<Animal>()

        override fun getItemCount() = items.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalAdapter.ViewHolder {
            return ViewHolder(DataBindingUtil.inflate<RepoAnimalItemBinding>(
                    LayoutInflater.from(parent.context), R.layout.repo_animal_item, parent, false))
        }

        override fun onBindViewHolder(holder: AnimalAdapter.ViewHolder, position: Int) {
            holder.binding.repoAnimal = items[position]
        }

        inner class ViewHolder(val binding: RepoAnimalItemBinding) : RecyclerView.ViewHolder(binding.root)
    }

}
