package dev.eastar.coroutine.demo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class UnitTestWithCoroutineViewModel : ViewModel() {
    private var d: Disposable? = null

    val sampleData: MutableLiveData<String> = MutableLiveData()

    fun unitTestItem() {
        val resultString = StringBuilder()

        val source = getSource()
        d = source.subscribe({
            resultString.append(it)
            println("${Thread.currentThread()} $it")
        }, {

        }, {
            sampleData.value = resultString.toString()
        })
    }

    fun getSource(): Observable<String> {
        val shapes = listOf("A", "B", "C", "D", "E")
        val colorList = listOf("red", "yellow", "blue")
        return Observable.concat(
                Observable.interval(0L, 30L, TimeUnit.MILLISECONDS)
                        .map { index -> shapes[index.toInt()] }
                        .take(shapes.size.toLong()),
                Observable.interval(0L, 100L, TimeUnit.MILLISECONDS)
                        .map { index -> colorList[index.toInt()] }
                        .take(colorList.size.toLong()))
    }

    override fun onCleared() {
        super.onCleared()
        d?.dispose()
    }

}
