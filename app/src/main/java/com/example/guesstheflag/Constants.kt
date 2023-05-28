package com.example.guesstheflag

object Constants {
    fun getQuestion(): ArrayList<Question>{

        val questionList = ArrayList<Question>()

        val que1 = Question(
            1,
            "Australia",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b9/Flag_of_Australia.svg/1200px-Flag_of_Australia.svg.png",
            listOf("Australia", "New Zealand", "Fiji", "Tuvalu")
        )
        val que2 = Question(
            2,
            "New Zealand",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Flag_of_New_Zealand.svg/1200px-Flag_of_New_Zealand.svg.png",
            listOf("Australia", "New Zealand", "Fiji", "Tuvalu")
        )
        val que3 = Question(
            3,
            "Fiji",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/Flag_of_Fiji.svg/1200px-Flag_of_Fiji.svg.png",
            listOf("Australia", "New Zealand", "Fiji", "Tuvalu")
        )

        val que4 = Question(
            4,
            "Tuvalu",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/3/38/Flag_of_Tuvalu.svg/1200px-Flag_of_Tuvalu.svg.png",
            listOf("Australia", "New Zealand", "Fiji", "Tuvalu")
        )

    questionList.add(que1)
    questionList.add(que2)
    questionList.add(que3)
    questionList.add(que4)

    return questionList
    }
}