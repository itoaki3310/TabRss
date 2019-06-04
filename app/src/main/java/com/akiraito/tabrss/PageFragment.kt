package com.akiraito.tabrss

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_page.*
import kotlinx.coroutines.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

class PageFragment : Fragment(){
    private var listener: OnFragmentInteractionListener? = null
    var title = ArrayList<String>()
    var text = ArrayList<String>()
    var linkUrl = ArrayList<String>()
    var imageUrl = ArrayList<String>()
    var listView: List<ItemData>? = null
    var url = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_page, container, false)
        //rssに使用するxmlファイルの探し方が不明なため、とりあえずURL直打ち
        url.add(getString(R.string.lifeHacker))
        url.add(getString(R.string.gizmode))
        url.add(getString(R.string.hamusoku))

        //TODO Drawerのなかに黒と白のリストを用意、タップで背景＆文字色の変更
        val strings = resources.getStringArray(R.array.menu_string_array)!!
        val drawerAdapter = ArrayAdapter<String>(context,R.layout.drawer_list_raw,strings)!!
        var textColor = Color.BLACK
        var navigationMenuList = view.findViewById<ListView>(R.id.navigation_menu_list)
        navigationMenuList.adapter=drawerAdapter
        navigationMenuList.setOnItemClickListener { _, _, position, _ ->
            var color = when {
                strings[position] == getString(R.string.white) ->  Color.WHITE
                strings[position] == getString(R.string.black) ->  Color.BLACK
                else -> Color.BLACK
            }
            textColor = when {
                strings[position] == getString(R.string.white) ->  Color.BLACK
                strings[position] == getString(R.string.black) ->  Color.WHITE
                else -> Color.WHITE
            }
            listview.setBackgroundColor(color)
            listView = List(title.size){i -> ItemData(title[i], text[i], imageUrl[i])}
            val adapter = RssListAdapter(context!!, listView!!, textColor)
            listview.adapter = adapter
            drawer_layout . closeDrawers ()

        }
//非同期でHTTP　GET
//        for(i in 0..url.size) {
            runBlocking {
                GlobalScope.launch {
                    val http = RssParserTask()
                    //Todo メインスレッドでNet通信できない為バックグラウンドで
                    //TODO 取得先がなかった場合の処理 -> 未完成
                    async(Dispatchers.Default) { http.httpGET(url[0]) }.await()
                        .let {
                            //TODO　持ってきたXMLをパース
                            //xmlPullParserにxmlファイルの中身が入ってる
                            var factory = XmlPullParserFactory.newInstance()
                            var xmlPullParser = factory.newPullParser()
                            xmlPullParser.setInput(StringReader(it))
                            var eventType = xmlPullParser.eventType
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                if (eventType == XmlPullParser.START_TAG) {
                                    if (getString(R.string.title).equals(xmlPullParser.name)) {
                                        title.add(xmlPullParser.nextText())
                                    } else if (getString(R.string.description).equals(xmlPullParser.name)) {
                                        text.add(xmlPullParser.nextText())
                                    } else if (getString(R.string.link).equals(xmlPullParser.name)) {
                                        linkUrl.add(xmlPullParser.nextText())
                                    } else if (getString(R.string.enclosure).equals(xmlPullParser.name)) {
                                        imageUrl.add(xmlPullParser.getAttributeValue(null, getString(R.string.url)))
                                    }
                                }
                                eventType = xmlPullParser.next()
                            }
                        }
                }.join()
            }
//        }
        title.removeAt(0)
        text.removeAt(0)
        //title.sizeの分だけItemData(title[i], text[i]を回してiに入れていく
        listView = List(title.size) { i -> ItemData(title[i], text[i], imageUrl[i]) }
        var listview: ListView = view.findViewById(R.id.listview)
        val adapter = RssListAdapter(this.context!!, listView!!, Color.BLACK)
        listview!!.adapter = adapter!!


        //TODO 遷移先がなかった場合の処理が必要、ダイアログだす？
        listview.setOnItemClickListener { adapterView, view, position, id ->
            var uri: Uri = Uri.parse(linkUrl.get(position))
            var intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent);
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is OnFragmentInteractionListener){
            listener = context
        }else{
            throw  RuntimeException()
        }
    }

    override fun onDetach() {
        super.onDetach()
        //メモリ解放
        listener = null
    }

    internal interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        private val PAGE = "PAGE"
        // PageFragment生成
        fun newInstance(page: Int): PageFragment {
            val pageFragment = PageFragment()
            val bundle = Bundle()
            bundle.putInt(PAGE, page)
            pageFragment.arguments = bundle
            return pageFragment
        }
    }

    data class ViewHolder(
        val rssTitle: TextView,
        val rssText: TextView,
        val rssImage: ImageView
    )
}



