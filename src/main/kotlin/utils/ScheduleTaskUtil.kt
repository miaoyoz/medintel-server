package com.miaoyongzheng.utils

import dev.inmo.krontab.KronScheduler
import dev.inmo.krontab.builder.SchedulerBuilder
import dev.inmo.krontab.builder.buildSchedule
import dev.inmo.krontab.doInfinity
import io.ktor.server.application.*
import korlibs.time.DateTime
import kotlinx.coroutines.*

data class Task(
    val name: String,
    val dispatcher: CoroutineDispatcher?,
    val concurrency: Int,
    val kronSchedule: KronScheduler,
    val task: suspend Application.(DateTime) -> Unit,
)

class TaskConfiguration {
    var name: String = ANONYMOUS_TASK_NAME
    var kronSchedule: SchedulerBuilder.() -> Unit = error("KronSchedule must be provided")
    var concurrency: Int = 1
    var dispatcher: CoroutineDispatcher? = null
    var task: suspend Application.(DateTime) -> Unit = error("Task must be provided")

    private companion object {
        const val ANONYMOUS_TASK_NAME = "Anonymous_Task"
    }
}

fun schedule(application: Application, config: TaskConfiguration.() -> Unit) {
    val task = TaskConfiguration().apply(config).apply { checkNotNull(this.kronSchedule) }.let {
        Task(
            name = it.name,
            dispatcher = it.dispatcher,
            concurrency = it.concurrency,
            kronSchedule = buildSchedule(it.kronSchedule),
            task = it.task,
        )
    }

    application.launch(
        context = application.coroutineContext + (task.dispatcher ?: Dispatchers.IO) + CoroutineName(task.name)
    ) {
        task.kronSchedule.doInfinity { executionTime ->
            if (!application.isActive) return@doInfinity // 避免任务在应用关闭时继续运行
            try {
                withContext(task.dispatcher ?: Dispatchers.IO) {
                    repeat(task.concurrency) {
                        launch {
                            task.task(application, executionTime)
                        }
                    }
                }
            } catch (e: Exception) {
                application.log.error("定时任务执行失败: ${task.name}", e)
            }
        }
    }
}



