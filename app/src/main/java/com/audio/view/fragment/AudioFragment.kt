package com.audio.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audio.util.LifeOrder
import com.audio.util.emit
import com.audio.util.log
import com.audio.util.to
import com.audio.view.life.AudioFrgToken
import com.audio.view.life.createLife
import com.audio.view.life.showToken
import org.jetbrains.anko.bundleOf

class AudioFragment : Fragment(){

    lateinit var receive : (Fragment, LifeOrder, Any?) -> Any
    companion object {
        fun newInstance(show : AudioFrgToken) : AudioFragment{
            var fragment = AudioFragment()
            fragment.arguments = bundleOf(Pair(fragment.showToken(), show))
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receive = createLife(arguments.getSerializable(showToken()).to())
        emit(LifeOrder.ONCREATE, savedInstanceState, receive)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return emit(LifeOrder.ONCREATEVIEW, savedInstanceState, receive).to()
    }

    override fun onStart() {
        super.onStart()
        emit(LifeOrder.ONSTART, null, receive)
    }

    override fun onStop() {
        super.onStop()
        emit(LifeOrder.ONSTOP, null, receive)
    }
}

