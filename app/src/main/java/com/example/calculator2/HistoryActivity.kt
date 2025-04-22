package com.example.calculator2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity: AppCompatActivity() {
    private lateinit var adapter: ArrayAdapter<String>
    private var historyList: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activityhistory)

        historyList = intent.getStringArrayListExtra("HISTORY_LIST")

        val listView: ListView = findViewById(R.id.historyListView)
        val clearBtn: Button = findViewById(R.id.clearHistoryButton)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, historyList ?: arrayListOf())
        listView.adapter = adapter

        clearBtn.setOnClickListener {
            historyList?.clear()
            adapter.notifyDataSetChanged()

            // Optionally, send empty list back to MainActivity
            val resultIntent = Intent()
            resultIntent.putStringArrayListExtra("CLEARED_HISTORY", historyList)
            setResult(Activity.RESULT_OK, resultIntent)
        }
    }

    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putStringArrayListExtra("CLEARED_HISTORY", historyList)
        setResult(Activity.RESULT_OK, resultIntent)
        super.onBackPressed()
    }
}