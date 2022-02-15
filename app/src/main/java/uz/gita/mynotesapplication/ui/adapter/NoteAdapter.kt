package uz.gita.mynotesapplication.ui.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.mynotesapplication.R
import uz.gita.mynotesapplication.data.entity.NoteEntity
import uz.gita.mynotesapplication.html2text
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class NoteAdapter : ListAdapter<NoteEntity, NoteAdapter.NoteViewHolder>(DiffItem) {
    private var itemClickListener : ((NoteEntity) -> Unit)? = null
    var dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yy  hh:mm")
    private var itemLongClickListener: ((NoteEntity) -> Unit)? = null

    object DiffItem : DiffUtil.ItemCallback<NoteEntity>() {
        override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem.isPinned == newItem.isPinned &&
                    oldItem.title == newItem.title &&
                    oldItem.message == newItem.message &&
                    oldItem.time == newItem.time
        }
    }

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                itemClickListener?.invoke(getItem(adapterPosition))
            }
            itemView.setOnLongClickListener {
                itemLongClickListener?.invoke(getItem(adapterPosition))
                return@setOnLongClickListener true
            }
        }

        private val title: TextView = itemView.findViewById(R.id.textTitle)
        private val text: TextView = itemView.findViewById(R.id.textNote)
        private val time: TextView = itemView.findViewById(R.id.time)
        private val pinImage: ImageView = itemView.findViewById(R.id.buttonImage)
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind() {

            val data = getItem(adapterPosition)
            val date = Date(data.time)
            title.text = data.title
            text.text = html2text(data.message)
            time.text = dateFormat.format(date)
            if (data.isPinned) pinImage.visibility = View.VISIBLE
            else pinImage.visibility = View.GONE
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        )

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind()
    }

    fun setItemClickListener(f: (NoteEntity) -> Unit) {
        itemClickListener = f
    }

    fun setItemLongClickListener(f: (NoteEntity) -> Unit) {
        itemLongClickListener = f
    }

}