package com.example.guesstheflag.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.guesstheflag.R
import com.example.guesstheflag.UserScore
import com.example.guesstheflag.database.AppDatabase
import com.example.guesstheflag.databinding.FragmentLeaderboardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LeaderBoardFragment : Fragment() {
    private lateinit var _binding: FragmentLeaderboardBinding
    var listScores = mutableListOf<UserScore>()


private val myScope = CoroutineScope(Dispatchers.IO)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentLeaderboardBinding.bind(view)
        val database : AppDatabase by lazy { Room.databaseBuilder(requireContext(), AppDatabase::class.java,"my-database")
            .build()
        }

        myScope.launch {
            withContext(Dispatchers.IO){
                val list : MutableList<UserScore> = database.userScoreDao().getAllUserScores()
                listScores.addAll(list)
                withContext(Dispatchers.Main){
                    _binding.recyclerView.apply {
                        adapter = LeaderBoardAdapter(listScores)
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                }
            }
        }





    }

}

class LeaderBoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class LeaderBoardAdapter(listScores: MutableList<UserScore>) : RecyclerView.Adapter<LeaderBoardViewHolder>() {
    var listScores = listScores
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderBoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.leaderboard_item, parent, false)
        return LeaderBoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaderBoardViewHolder, position: Int) {
        val userScore = listScores[position]
        holder.itemView.apply {
            val name = findViewById<TextView>(R.id.userNameTv)
            val score = findViewById<TextView>(R.id.userScoreTv)
            name.text = userScore.name
            score.text = userScore.score.toString()
        }
    }

    override fun getItemCount(): Int {
        return listScores.size
    }


}
