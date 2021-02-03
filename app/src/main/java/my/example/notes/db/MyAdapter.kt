package my.example.notes.db

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import my.example.notes.NewNotes
import my.example.notes.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("NAME_SHADOWING")
class MyAdapter(listNotes: ArrayList<ListNotes>, contextM: Context) :
    RecyclerView.Adapter<MyAdapter.MyHolder>() {

    private var listArray = listNotes
    private var context: Context = contextM
    var currentDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    class MyHolder(itemView: View, contextV: Context) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById(R.id.titleTv)
        val tvDesc: TextView = itemView.findViewById(R.id.descTv)
        val tvDate: TextView = itemView.findViewById(R.id.dateTv)
        val context = contextV
        val item: CardView = itemView.findViewById(R.id.cardRecords)
        var status: String = ""

        fun setData(note: ListNotes) {

            tvTitle.text = note.title
            tvDesc.text = note.desc
            tvDate.text = note.date
            status = note.status

            itemView.setOnClickListener {

                val intent = Intent(context, NewNotes::class.java).apply {
                    putExtra(MyIntentConst.I_ID_KEY, note.id)
                    putExtra(MyIntentConst.I_TITLE_KEY, note.title)
                    putExtra(MyIntentConst.I_DESC_KEY, note.desc)
                    putExtra(MyIntentConst.I_DATE_KEY, note.date)
                    putExtra(MyIntentConst.I_STATUS_KEY, note.status)
                    putExtra("isEditDelete", true)

                }
                context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(
            inflater.inflate(
                R.layout.row,
                parent,
                false
            ), context
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        var id = listArray
        holder.setData(listArray[position])
        holder.item.setCardBackgroundColor(Color.parseColor("#E4ECE4"))
        holder.tvDate.setTextColor(Color.parseColor(getColorDate(currentDate, holder.tvDate.text.toString())))
        holder.tvTitle.text = collapseTitle(holder.tvTitle.text.toString())
        holder.tvDesc.text = collapseDesc(holder.tvDesc.text.toString())
        if(listArray[position].status == "1"){
            holder.tvTitle.paintFlags = holder.tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.tvTitle.setTextColor(Color.parseColor("#707070"))
            holder.tvDesc.setTextColor(Color.parseColor("#707070"))
            holder.tvDate.setTextColor(Color.parseColor("#707070"))
        }

    }

    override fun getItemCount(): Int {
        return listArray.size
    }

    fun updateAdapter(listItems: List<ListNotes>) {
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    private fun getColorDate(currentDate: String, dateD: String): String {

        val cDate = currentDate.split("-")
        val dateD = dateD.split("-")

        val color: String

        color =
//            if (cDate[2].toInt() >= dateD[2].toInt() && cDate[1].toInt() >= dateD[1].toInt() && cDate[0].toInt() > dateD[0].toInt()) {
            if (cDate[0].toInt() >= dateD[0].toInt() && cDate[1].toInt() >= dateD[1].toInt() && cDate[2].toInt() > dateD[2].toInt()) {
                "#d63031"
            } else {
                "#1e272e"
            }

        return color
    }

    private fun collapseTitle(text: String): String {
        return if (text.length > 15) {
            "${text.substring(0, 15)} ..."
        } else { text }
    }

    private fun collapseDesc(text: String): String {
        return if (text.length > 70) {
            "${text.substring(0, 70)} ..."
        } else { text }
    }
}
