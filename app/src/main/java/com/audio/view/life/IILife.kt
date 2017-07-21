package com.audio.view.life

interface IILife<T> : ILife<T> {
    override fun receive(): (T, LifeOrder, Any?) -> Any {
        return {
            t, lifeOrder, any ->
            when (lifeOrder) {
                LifeOrder.ONCREATE -> onCreate(t, any)
                LifeOrder.ONCREATEVIEW -> onCreateView(t, any)
                LifeOrder.ONCREATEOPTIONSMENU -> onCreateOptionsMenu(any)
                LifeOrder.ONRESUME -> onResume()
                LifeOrder.ONSTART -> onStart()
                LifeOrder.ONPAUSE -> onPause()
                LifeOrder.ONSTOP -> onStop()
                LifeOrder.ONDESTROY -> onDestroy()
            }
        }
    }

    fun onCreate(context: T, any: Any?) {}
    fun onCreateView(context: T, any: Any?): Any {return Unit}
    fun onCreateOptionsMenu(any: Any?) {}
    fun onResume() {}
    fun onStart() {}
    fun onPause() {}
    fun onStop() {}
    fun onDestroy() {}
}
