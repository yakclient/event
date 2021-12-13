package net.yakclient.event.api

import net.yakclient.event.api.stage.EventStage

public open class EventPipeline(
    stages: List<EventStage>
) : List<EventStage> by stages {
    public fun dispatch(data: EventData): EventData = fold(data) { acc, stage -> stage.apply(acc) }

    public open class MutablePipeline(
        private val stages: MutableList<EventStage> = ArrayList()
    ) : EventPipeline(stages) {
        public fun add(stage: EventStage): Boolean = stages.add(stage)
    }
}



