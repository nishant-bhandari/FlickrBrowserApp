package com.example.flickrbrowser30

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView, private val listener: OnRecyclerClickListener)
    : RecyclerView.SimpleOnItemTouchListener(){

    private val TAG = "RecyclerItemClickListen"

    //add the gesture detector
    private val gestureDetector = GestureDetectorCompat(context,object : GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            Log.d(TAG,"onSingleTapUp: starts")
            val childView = recyclerView.findChildViewUnder(e.x,e.y)
            Log.d(TAG,"onSingleTapUp: Calling listener .onItemClick")
            listener.onItemClick(childView,recyclerView.getChildAdapterPosition(childView!!))
            return true
        }
        override fun onLongPress(e: MotionEvent) {
            Log.d(TAG,"onLongPress: starts")
            val childView = recyclerView.findChildViewUnder(e.x,e.y)
            Log.d(TAG,"onLongPress: Calling listener .onItemLongClick")
            listener.onItemLongClick(childView,recyclerView.getChildAdapterPosition(childView!!))
        }
    })

    interface OnRecyclerClickListener {
        fun onItemClick(view: View?, position: Int)
        fun onItemLongClick(view: View?, position: Int)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(TAG,"onInterceptTouchEvent:Starts")
        val result = gestureDetector.onTouchEvent(e)
        Log.d(TAG,"onInterceptTouchEvent() returning : $result")
//        return super.onInterceptTouchEvent(rv, e)
        return result
    }
}