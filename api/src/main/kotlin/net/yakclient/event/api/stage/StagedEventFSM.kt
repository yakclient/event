package net.yakclient.event.api.stage

import net.yakclient.event.api.EventData
import net.yakclient.event.api.fsm.EventFSM

public class StagedEventFSM(
    private val fsm: EventFSM
) : EventStage {
    override fun apply(t: EventData): EventData = t.apply { fsm.dispatch(t) }
}