package com.akiraito.tabrss

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.squareup.picasso.Picasso

class RssListAdapter(context: Context, title: List<ItemData>, textColor: Int): ArrayAdapter<ItemData>(context,0,title) {
    //レイアウトファイルを取得するために必要となる LayoutInflater
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var itemTextColor= textColor

    /**
     * @param position 行数
     * @param convertView　レイアウトのView
     * @param parent　親View
     *
     * return View,view が null 以外だった場合には、view の tag に ViewHolder が格納されているはず
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        var holder: PageFragment.ViewHolder

        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_raw, parent, false)
            holder = PageFragment.ViewHolder(
                view?.findViewById(R.id.rss_title)!!,
                view?.findViewById(R.id.rss_text)!!,
                view?.findViewById(R.id.rss_image)!!
            )
            view.tag = holder
        } else {
            holder = view.tag as PageFragment.ViewHolder
        }

        val textShelf = getItem(position) as ItemData
        holder.rssTitle.text = textShelf.rssTitle
        holder.rssText.text = textShelf.rssText
        Picasso.get().load(textShelf.rssImage).error(R.mipmap.ic_launcher_round).into(holder.rssImage)

        holder.rssTitle.setTextColor(itemTextColor)
        holder.rssText.setTextColor(itemTextColor)

        return view
    }


}