package my.example.notes

import androidx.appcompat.app.ActionBar
import android.app.DatePickerDialog
import android.content.Intent
import android.net.sip.SipManager.newInstance
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.text.Editable
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_new_notes.*
import java.text.SimpleDateFormat
import my.example.notes.db.MyDbManager
import my.example.notes.db.MyIntentConst
import java.util.*
import javax.xml.datatype.DatatypeFactory.newInstance

class NewNotes : AppCompatActivity() {

    private val myDbManager = MyDbManager(this)

    private lateinit var edTitle: EditText
    private lateinit var textInputTitle: TextInputLayout
    private lateinit var edDesc: EditText
    private lateinit var edDate: EditText
    private lateinit var selDate: ImageButton

    private var isEditDelete = false
    private var id: String? = ""
    private var status: String? = "0" //0 - Действующая, 1 - выполненная

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
    private var formatDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val currentDate: String = this.formatDate.format(Date()) //Текущая дата

    private var actionBar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_notes)

        selDate = findViewById(R.id.dtBtn) //Выбор даты
        edDate = findViewById(R.id.edDate) //Выбранная дала
        edTitle = findViewById(R.id.edTitle) //Заголовок
        textInputTitle = findViewById(R.id.tiLayout)
        edDesc = findViewById(R.id.edDesc) //Описание

        actionBar = supportActionBar
        actionBar?.title = "Новое"

        edDate.text = currentDate.toEditable()
        selDate.setOnClickListener { openDPicker() }

        getMyIntents()

        saveBtn.setOnClickListener{
            addNotesToDB()
        }

        deleteBtn.setOnClickListener {
            delete()
        }

        doneBtn.setOnClickListener {
            isDone()
        }

    }

    private fun delete() {

        val alert1 = androidx.appcompat.app.AlertDialog.Builder(this)
        alert1.setPositiveButton("Да") { d, i ->

            myDbManager.deleteData(id!!)
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }
        alert1.setNegativeButton("Нет") { d, i ->
            d.cancel()
        }
        alert1.setMessage("Удалить?").create()
        alert1.show()

    }

    private fun addNotesToDB() {

        myDbManager.openDb()

        val myTitle = edTitle.text.toString()
        val myDesc = edDesc.text.toString()
        val myDate = edDate.text.toString()

        if (myTitle != "" && myDate != "") {
            if (isEditDelete) {
                myDbManager.updateToDb(id!!, myTitle, myDesc, myDate, status)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                myDbManager.insertToDb(myTitle, myDesc, myDate, status)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        } else {
            textInputTitle.error = getString(R.string.invalidTitle)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    private fun openDPicker() {
        val getDate: Calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
            { _, year, month, dayOfMonth ->

                val selectDate = Calendar.getInstance()
                selectDate.set(Calendar.YEAR, year)
                selectDate.set(Calendar.MONTH, month)
                selectDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val date: String = formatDate.format(selectDate.time)
                this.edDate.text = date.toEditable()
            },
            getDate.get(Calendar.YEAR),
            getDate.get(Calendar.MONTH),
            getDate.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun getMyIntents() {

        val i = intent

        if (i.getStringExtra(MyIntentConst.I_TITLE_KEY) != "null") {
            isEditDelete = i.getBooleanExtra("isEditDelete", false)
            if (isEditDelete) {

                actionBar?.title = i.getStringExtra(MyIntentConst.I_TITLE_KEY)
                doneBtn.visibility = View.VISIBLE
                deleteBtn.visibility = View.VISIBLE


                id = i.getStringExtra(MyIntentConst.I_ID_KEY)
                status = i.getStringExtra(MyIntentConst.I_STATUS_KEY)

                if (status == "1") {

                    doneBtn.isEnabled = false
                    doneBtn.isClickable = false
                    edTitle.isEnabled = false
                    edDesc.isEnabled = false
                    edDesc.isEnabled = false
                    selDate.isEnabled = false
                    saveBtn.isVisible = false

                }

                edTitle.setText(i.getStringExtra(MyIntentConst.I_TITLE_KEY))
                edDesc.setText(i.getStringExtra(MyIntentConst.I_DESC_KEY))
                edDate.setText(i.getStringExtra(MyIntentConst.I_DATE_KEY))

            }
        }
    }

    private fun isDone() {

        val myTitle = edTitle.text.toString()
        val myDesc = edDesc.text.toString()
        val myDate = edDate.text.toString()
        val newStatus = "1"

        val alert2 = androidx.appcompat.app.AlertDialog.Builder(this)
        alert2.setPositiveButton("OK") { d, i ->

            myDbManager.updateToDb(id!!, myTitle, myDesc, myDate, newStatus)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        alert2.setMessage("Поздравляем. Задача выполнена!").create()
        alert2.show()

    }

}