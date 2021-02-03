package my.example.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import my.example.notes.db.MyAdapter
import my.example.notes.db.MyDbManager

class MainActivity : AppCompatActivity() {

    private val myDbManager = MyDbManager(this)
    private val myAdapter = MyAdapter(ArrayList(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addBtn.setOnClickListener{
            addNewNote()
        }

        init()
    }

    private fun init(){
        rcView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        rcView.adapter = myAdapter
    }

    public override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    private fun addNewNote(){
        Intent(this, NewNotes::class.java).apply {
            putExtra("isEditDelete", false)
            startActivity(this)
        }
    }

    private fun fillAdapter(){
        myAdapter.updateAdapter(myDbManager.readDbData())
    }}